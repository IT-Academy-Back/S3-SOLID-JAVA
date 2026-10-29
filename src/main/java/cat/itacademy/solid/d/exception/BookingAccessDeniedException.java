package cat.itacademy.solid.d.exception;

public class BookingAccessDeniedException extends RuntimeException {

    public BookingAccessDeniedException(String customerId, Long bookingId) {
        super("Customer '%s' cannot cancel booking %d".formatted(customerId, bookingId));
    }
}
