package cat.itacademy.solid.d.exception;

public class CancellationPeriodExpiredException extends RuntimeException {

    public CancellationPeriodExpiredException(Long bookingId) {
        super("Booking %d can no longer be cancelled".formatted(bookingId));
    }
}
