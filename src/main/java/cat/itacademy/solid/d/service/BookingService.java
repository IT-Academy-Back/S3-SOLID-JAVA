package cat.itacademy.solid.d.service;

import cat.itacademy.solid.d.exception.BookingAccessDeniedException;
import cat.itacademy.solid.d.model.Booking;
import cat.itacademy.solid.d.repository.H2BookingRepository;

import java.time.LocalDateTime;

public class BookingService {

    private final H2BookingRepository repository;

    public BookingService(H2BookingRepository repository) {
        this.repository = repository;
    }

    public void cancelBooking(String customerId, Long bookingId) {
        Booking booking = repository.findById(bookingId)
                .orElseThrow(() -> new IllegalStateException("Booking not found"));

        if (!booking.belongsTo(customerId)) {
            throw new BookingAccessDeniedException(customerId, bookingId);
        }

        LocalDateTime now = LocalDateTime.now();

        booking.cancel(now);
        repository.save(booking);
    }
}
