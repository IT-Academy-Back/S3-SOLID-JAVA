package cat.itacademy.solid.d.repository;

import cat.itacademy.solid.d.model.Booking;
import cat.itacademy.solid.d.model.BookingStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryBookingRepositoryTest {

    private static final LocalDateTime STARTS_AT = LocalDateTime.of(2026, 10, 20, 18, 0);

    private InMemoryBookingRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryBookingRepository();
    }

    @Test
    void shouldGenerateIdWhenSavingNewBooking() {
        Booking original = newBooking();

        Booking saved = repository.save(original);

        assertNotNull(saved.getId());
        assertNull(original.getId());
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
    void shouldPersistCancellationOnlyWhenSavingAgain() {
        Booking booking = repository.save(newBooking());
        LocalDateTime cancelledAt = STARTS_AT.minusHours(30);

        booking.cancel(cancelledAt);

        assertEquals(BookingStatus.CONFIRMED,
                repository.findById(booking.getId()).orElseThrow().getStatus());

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
        assertTrue(repository.findAllOrderedByStart().isEmpty());
    }

    @Test
    void shouldFindAllBookingsOrderedByStartAndId() {
        Booking late = repository.save(bookingStartingAt(STARTS_AT.plusDays(2)));
        Booking early = repository.save(bookingStartingAt(STARTS_AT));
        Booking middle = repository.save(bookingStartingAt(STARTS_AT.plusDays(1)));
        Booking tied = repository.save(bookingStartingAt(STARTS_AT));

        List<Long> ids = repository.findAllOrderedByStart().stream()
                .map(Booking::getId)
                .toList();

        assertEquals(List.of(early.getId(), tied.getId(), middle.getId(), late.getId()), ids);
    }

    @Test
    void shouldReturnEmptyListWhenThereAreNoBookings() {
        assertTrue(repository.findAllOrderedByStart().isEmpty());
    }

    @Test
    void shouldKeepRepositoryInstancesIndependent() {
        Booking saved = repository.save(newBooking());
        InMemoryBookingRepository other = new InMemoryBookingRepository();

        assertTrue(other.findById(saved.getId()).isEmpty());
        assertTrue(other.findAllOrderedByStart().isEmpty());

        other.save(newBooking());

        assertEquals(1, repository.findAllOrderedByStart().size());
        assertEquals(1, other.findAllOrderedByStart().size());
    }

    @Test
    void changingRetrievedBookingShouldNotUpdateStoredBooking() {
        Long id = repository.save(newBooking()).getId();
        Booking retrieved = repository.findById(id).orElseThrow();

        retrieved.cancel(STARTS_AT.minusHours(30));

        Booking stored = repository.findById(id).orElseThrow();
        assertEquals(BookingStatus.CONFIRMED, stored.getStatus());
        assertNull(stored.getCancelledAt());
    }

    @Test
    void changingListedBookingShouldNotUpdateStoredBooking() {
        Long id = repository.save(newBooking()).getId();
        Booking listed = repository.findAllOrderedByStart().get(0);

        listed.cancel(STARTS_AT.minusHours(30));

        Booking stored = repository.findById(id).orElseThrow();
        assertEquals(BookingStatus.CONFIRMED, stored.getStatus());
        assertNull(stored.getCancelledAt());
    }

    @Test
    void changingBookingAfterUpdateShouldRequireAnotherSave() {
        Booking booking = repository.save(newBooking());
        LocalDateTime cancelledAt = STARTS_AT.minusHours(30);
        booking.cancel(cancelledAt);

        Booking saved = repository.save(booking);
        booking.cancel(STARTS_AT.minusHours(29));
        saved.cancel(STARTS_AT.minusHours(28));

        assertEquals(cancelledAt,
                repository.findById(booking.getId()).orElseThrow().getCancelledAt());
    }

    private Booking bookingStartingAt(LocalDateTime startsAt) {
        return new Booking("CUSTOMER-001", "ROOM-1", startsAt, startsAt.plusHours(1));
    }

    private Booking newBooking() {
        return bookingStartingAt(STARTS_AT);
    }
}
