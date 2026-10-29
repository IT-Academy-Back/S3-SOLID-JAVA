package cat.itacademy.solid.d.model;

import cat.itacademy.solid.d.exception.CancellationPeriodExpiredException;

import java.time.Duration;
import java.time.LocalDateTime;

public class Booking {

    private static final Duration CANCELLATION_LIMIT = Duration.ofHours(24);

    private final Long id;
    private final String customerId;
    private final String roomId;
    private final LocalDateTime startsAt;
    private final LocalDateTime endsAt;
    private BookingStatus status;
    private LocalDateTime cancelledAt;

    public Booking(
            String customerId,
            String roomId,
            LocalDateTime startsAt,
            LocalDateTime endsAt
    ) {
        this(null, customerId, roomId, startsAt, endsAt, BookingStatus.CONFIRMED, null);
    }

    public Booking(
            Long id,
            String customerId,
            String roomId,
            LocalDateTime startsAt,
            LocalDateTime endsAt,
            BookingStatus status,
            LocalDateTime cancelledAt
    ) {
        this.id = id;
        this.customerId = customerId;
        this.roomId = roomId;
        this.startsAt = startsAt;
        this.endsAt = endsAt;
        this.status = status;
        this.cancelledAt = cancelledAt;
    }

    public boolean belongsTo(String customerId) {
        return this.customerId.equals(customerId);
    }

    public void cancel(LocalDateTime now) {
        if (now.isAfter(startsAt.minus(CANCELLATION_LIMIT))) {
            throw new CancellationPeriodExpiredException(id);
        }
        this.status = BookingStatus.CANCELLED;
        this.cancelledAt = now;
    }

    public Long getId() {
        return id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getRoomId() {
        return roomId;
    }

    public LocalDateTime getStartsAt() {
        return startsAt;
    }

    public LocalDateTime getEndsAt() {
        return endsAt;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public LocalDateTime getCancelledAt() {
        return cancelledAt;
    }
}
