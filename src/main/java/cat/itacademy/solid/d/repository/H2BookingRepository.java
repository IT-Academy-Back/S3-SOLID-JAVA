package cat.itacademy.solid.d.repository;

import cat.itacademy.solid.d.model.Booking;
import cat.itacademy.solid.d.model.BookingStatus;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class H2BookingRepository implements AutoCloseable {

    // Una única conexió oberta: una base H2 en memòria desapareix quan es tanca l'última connexió.
    private final Connection connection;

    public H2BookingRepository(String jdbcUrl) {
        try {
            this.connection = DriverManager.getConnection(jdbcUrl);
        } catch (SQLException e) {
            throw new IllegalStateException("Could not connect to database", e);
        }
        createSchema();
    }

    @Override
    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new IllegalStateException("Could not close connection", e);
        }
    }

    public Booking save(Booking booking) {
        return booking.getId() == null ? insert(booking) : update(booking);
    }

    private Booking insert(Booking booking) {
        String sql = """
                INSERT INTO bookings
                    (customer_id, room_id, starts_at, ends_at, status, cancelled_at)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (PreparedStatement statement =
                     connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, booking.getCustomerId());
            statement.setString(2, booking.getRoomId());
            statement.setTimestamp(3, Timestamp.valueOf(booking.getStartsAt()));
            statement.setTimestamp(4, Timestamp.valueOf(booking.getEndsAt()));
            statement.setString(5, booking.getStatus().name());
            statement.setTimestamp(6, toTimestamp(booking.getCancelledAt()));
            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                keys.next();
                return new Booking(
                        keys.getLong(1),
                        booking.getCustomerId(),
                        booking.getRoomId(),
                        booking.getStartsAt(),
                        booking.getEndsAt(),
                        booking.getStatus(),
                        booking.getCancelledAt()
                );
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Could not save booking", e);
        }
    }

    private Booking update(Booking booking) {
        String sql = """
                UPDATE bookings
                SET customer_id = ?, room_id = ?, starts_at = ?, ends_at = ?,
                    status = ?, cancelled_at = ?
                WHERE id = ?
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, booking.getCustomerId());
            statement.setString(2, booking.getRoomId());
            statement.setTimestamp(3, Timestamp.valueOf(booking.getStartsAt()));
            statement.setTimestamp(4, Timestamp.valueOf(booking.getEndsAt()));
            statement.setString(5, booking.getStatus().name());
            statement.setTimestamp(6, toTimestamp(booking.getCancelledAt()));
            statement.setLong(7, booking.getId());

            if (statement.executeUpdate() == 0) {
                throw new IllegalStateException("Booking not found: " + booking.getId());
            }
            return booking;
        } catch (SQLException e) {
            throw new IllegalStateException("Could not save booking", e);
        }
    }

    private Timestamp toTimestamp(LocalDateTime dateTime) {
        return dateTime == null ? null : Timestamp.valueOf(dateTime);
    }

    public List<Booking> findAllOrderedByStart() {
        String sql = "SELECT * FROM bookings ORDER BY starts_at, id";

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
            List<Booking> bookings = new ArrayList<>();
            while (rs.next()) {
                bookings.add(toBooking(rs));
            }
            return bookings;
        } catch (SQLException e) {
            throw new IllegalStateException("Could not read bookings", e);
        }
    }

    public Optional<Booking> findById(Long bookingId) {
        String sql = "SELECT * FROM bookings WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, bookingId);

            try (ResultSet rs = statement.executeQuery()) {
                if (!rs.next()) {
                    return Optional.empty();
                }
                return Optional.of(toBooking(rs));
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Could not read booking", e);
        }
    }

    private Booking toBooking(ResultSet rs) throws SQLException {
        Timestamp cancelledAt = rs.getTimestamp("cancelled_at");

        return new Booking(
                rs.getLong("id"),
                rs.getString("customer_id"),
                rs.getString("room_id"),
                rs.getTimestamp("starts_at").toLocalDateTime(),
                rs.getTimestamp("ends_at").toLocalDateTime(),
                BookingStatus.valueOf(rs.getString("status")),
                cancelledAt == null ? null : cancelledAt.toLocalDateTime()
        );
    }

    private void createSchema() {
        String sql = """
                CREATE TABLE IF NOT EXISTS bookings (
                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                    customer_id VARCHAR(50) NOT NULL,
                    room_id VARCHAR(50) NOT NULL,
                    starts_at TIMESTAMP NOT NULL,
                    ends_at TIMESTAMP NOT NULL,
                    status VARCHAR(20) NOT NULL,
                    cancelled_at TIMESTAMP
                )
                """;

        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
        } catch (SQLException e) {
            throw new IllegalStateException("Could not create schema", e);
        }
    }
}
