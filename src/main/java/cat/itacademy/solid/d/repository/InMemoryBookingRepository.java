package cat.itacademy.solid.d.repository;

import cat.itacademy.solid.d.model.Booking;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InMemoryBookingRepository {

    private final Map<Long, Booking> bookings = new HashMap<>();
    private long nextId = 1;

    public Booking save(Booking booking) {
        Long id = booking.getId();
        if (id == null) {
            id = nextId++;
        } else if (!bookings.containsKey(id)) {
            throw new IllegalStateException("Booking not found: " + id);
        }

        Booking saved = copy(booking, id);
        bookings.put(id, saved);
        return copy(saved, id);
    }

    public Optional<Booking> findById(Long bookingId) {
        return Optional.ofNullable(bookings.get(bookingId))
                .map(booking -> copy(booking, booking.getId()));
    }

    public List<Booking> findAllOrderedByStart() {
        return bookings.values().stream()
                .sorted(Comparator.comparing(Booking::getStartsAt)
                        .thenComparing(Booking::getId))
                .map(booking -> copy(booking, booking.getId()))
                .toList();
    }

    // Igual que amb H2, modificar una reserva llegida no la desa automàticament.
    private Booking copy(Booking booking, Long id) {
        return new Booking(
                id,
                booking.getCustomerId(),
                booking.getRoomId(),
                booking.getStartsAt(),
                booking.getEndsAt(),
                booking.getStatus(),
                booking.getCancelledAt()
        );
    }
}
