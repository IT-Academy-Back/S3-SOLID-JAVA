package cat.itacademy.solid.d;

import cat.itacademy.solid.d.console_menu.BookingConsole;
import cat.itacademy.solid.d.console_menu.ConsoleInput;
import cat.itacademy.solid.d.model.BookingStatus;
import cat.itacademy.solid.d.repository.H2BookingRepository;
import cat.itacademy.solid.d.service.BookingService;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BookingConsoleTest {

    @Test
    void shouldCreateAndCancelBookingFromMenu() {
        try (H2BookingRepository repository =
                     new H2BookingRepository("jdbc:h2:mem:" + UUID.randomUUID())) {

            String output = run(repository, """
                    3
                    CUSTOMER-001
                    ROOM-1
                    48
                    2
                    
                    4
                    1
                    CUSTOMER-001
                    
                    2
                    1
                    
                    0
                    """);

            assertTrue(output.contains("Reserva creada amb id 1"));
            assertTrue(output.contains("Reserva 1 cancel·lada."));
            assertEquals(
                    BookingStatus.CANCELLED,
                    repository.findById(1L).orElseThrow().getStatus()
            );
        }
    }

    @Test
    void shouldShowErrorWhenCancellingTooLate() {
        try (H2BookingRepository repository =
                     new H2BookingRepository("jdbc:h2:mem:" + UUID.randomUUID())) {

            String output = run(repository, """
                    3
                    CUSTOMER-001
                    ROOM-1
                    2
                    1
                    
                    4
                    1
                    CUSTOMER-001
                    
                    0
                    """);

            assertTrue(output.contains("No s'ha pogut cancel·lar"));
        }
    }

    @Test
    void shouldListBookingsOrderedByStart() {
        try (H2BookingRepository repository =
                     new H2BookingRepository("jdbc:h2:mem:" + UUID.randomUUID())) {

            String output = run(repository, """
                    3
                    C-LATE
                    R1
                    72
                    1
                    
                    3
                    C-EARLY
                    R1
                    10
                    1
                    
                    1
                    
                    0
                    """);

            assertTrue(output.indexOf("C-EARLY") < output.indexOf("C-LATE", output.indexOf("ordenades")));
        }
    }

    @Test
    void shouldSayWhenThereAreNoBookings() {
        try (H2BookingRepository repository =
                     new H2BookingRepository("jdbc:h2:mem:" + UUID.randomUUID())) {

            assertTrue(run(repository, "1\n\n0\n").contains("No hi ha cap reserva."));
        }
    }

    @Test
    void shouldRepeatQuestionsUntilValidValuesAreEntered() {
        try (H2BookingRepository repository =
                     new H2BookingRepository("jdbc:h2:mem:" + UUID.randomUUID())) {

            String output = run(repository, """
                    9
                    abc
                    3
                    
                    CUSTOMER-001
                    ROOM-1
                    xyz
                    -4
                    48
                    0
                    2
                    
                    2
                    0
                    1
                    
                    0
                    """);

            assertTrue(output.contains("Opció no vàlida"));
            assertTrue(output.contains("El valor no pot estar buit"));
            assertTrue(output.contains("'xyz' no és un número vàlid"));
            assertTrue(output.contains("El número ha de ser com a mínim 0"));
            assertTrue(output.contains("El número ha de ser com a mínim 1"));
            assertTrue(output.contains("Reserva creada amb id 1"));
            assertTrue(repository.findById(1L).isPresent());
        }
    }

    @Test
    void shouldExitGracefullyWhenInputEnds() {
        try (H2BookingRepository repository =
                     new H2BookingRepository("jdbc:h2:mem:" + UUID.randomUUID())) {

            assertTrue(run(repository, "").contains("Adéu!"));
        }
    }

    private String run(H2BookingRepository repository, String input) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(bytes);

        new BookingConsole(
                repository,
                new BookingService(repository),
                new ConsoleInput(new ByteArrayInputStream(input.getBytes()), out),
                out
        ).run();

        return bytes.toString();
    }
}
