package cat.itacademy.solid.d.repository;

import cat.itacademy.solid.d.model.Booking;
import cat.itacademy.solid.d.model.BookingStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class H2BookingRepositoryIntegrationTest {

    private static final LocalDateTime STARTS_AT = LocalDateTime.of(2026, 10, 20, 18, 0);

    private H2BookingRepository repository;

    @BeforeEach
    void setUp() {
        repository = new H2BookingRepository("jdbc:h2:mem:" + UUID.randomUUID());
    }

    @AfterEach
    void tearDown() {
        repository.close();
    }

    @Test
    void shouldGenerateIdWhenSavingNewBooking() {
        Booking saved = repository.save(newBooking());

        assertNotNull(saved.getId());
    }

    @Test
    void shouldGenerateDifferentIdsForDifferentBookings() {
        Booking first = repository.save(newBooking());
        Booking second = repository.save(newBooking());

        assertNotEquals(first.getId(), second.getId());
    }

    @Test
    void shouldSaveAndFindConfirmedBooking() {
        Long id = repository.save(newBooking()).getId();

        Booking found = repository.findById(id).orElseThrow();

        assertEquals(id, found.getId());
        assertEquals("CUSTOMER-001", found.getCustomerId());
        assertEquals("ROOM-1", found.getRoomId());
        assertEquals(STARTS_AT, found.getStartsAt());
        assertEquals(STARTS_AT.plusHours(1), found.getEndsAt());
        assertEquals(BookingStatus.CONFIRMED, found.getStatus());
        assertNull(found.getCancelledAt());
    }

    @Test
    void shouldReturnEmptyWhenBookingDoesNotExist() {
        assertEquals(Optional.empty(), repository.findById(999L));
    }

    @Test
    void shouldPersistCancellationWhenSavingAgain() {
        Booking booking = repository.save(newBooking());

        LocalDateTime cancelledAt = STARTS_AT.minusHours(30);
        booking.cancel(cancelledAt);
        repository.save(booking);

        Booking found = repository.findById(booking.getId()).orElseThrow();
        assertEquals(BookingStatus.CANCELLED, found.getStatus());
        assertEquals(cancelledAt, found.getCancelledAt());
    }

    @Test
    void shouldFailWhenUpdatingNonExistentBooking() {
        Booking ghost = new Booking(
                999L, "CUSTOMER-001", "ROOM-1", STARTS_AT, STARTS_AT.plusHours(1),
                BookingStatus.CONFIRMED, null
        );

        assertThrows(IllegalStateException.class, () -> repository.save(ghost));
    }

    @Test
    void shouldFindAllBookingsOrderedByStart() {
        Booking late = repository.save(bookingStartingAt(STARTS_AT.plusDays(2)));
        Booking early = repository.save(bookingStartingAt(STARTS_AT));
        Booking middle = repository.save(bookingStartingAt(STARTS_AT.plusDays(1)));

        var ids = repository.findAllOrderedByStart().stream().map(Booking::getId).toList();

        assertEquals(java.util.List.of(early.getId(), middle.getId(), late.getId()), ids);
    }

    @Test
    void shouldReturnEmptyListWhenThereAreNoBookings() {
        assertTrue(repository.findAllOrderedByStart().isEmpty());
    }

    private Booking bookingStartingAt(LocalDateTime startsAt) {
        return new Booking("CUSTOMER-001", "ROOM-1", startsAt, startsAt.plusHours(1));
    }

    private Booking newBooking() {
        return new Booking("CUSTOMER-001", "ROOM-1", STARTS_AT, STARTS_AT.plusHours(1));
    }
}
