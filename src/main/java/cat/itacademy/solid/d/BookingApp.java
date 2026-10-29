package cat.itacademy.solid.d;

import cat.itacademy.solid.d.console_menu.BookingConsole;
import cat.itacademy.solid.d.console_menu.ConsoleInput;
import cat.itacademy.solid.d.repository.H2BookingRepository;
import cat.itacademy.solid.d.service.BookingService;

public class BookingApp {

    public static void main(String[] args) {
        ConsoleInput input = new ConsoleInput(System.in, System.out);

        System.out.println("Persistències disponibles:");
        System.out.println("  1. H2 (base de dades en memòria)");
        System.out.println("  2. En memòria (Map)");

        if (input.readIntInRange("Tria persistència (1-2): ", 1, 2) == 2) {
            printPendingExercise();
            System.out.println();
            System.out.println("Es continua amb H2.");
        }

        try (H2BookingRepository repository = new H2BookingRepository("jdbc:h2:mem:bookings")) {
            BookingService service = new BookingService(repository);

            new BookingConsole(repository, service, input, System.out).run();
        }
    }

    // EXERCICI (DIP): la persistència triada ha de ser la que utilitzi realment l'aplicació.
    private static void printPendingExercise() {
        System.out.println();
        System.out.println("La persistència en memòria ja està implementada.");
        System.out.println("Falta connectar-la a l'aplicació com a part de l'exercici.");
    }
}
