package cat.itacademy.solid.d.console_menu;

import cat.itacademy.solid.d.exception.BookingAccessDeniedException;
import cat.itacademy.solid.d.exception.CancellationPeriodExpiredException;
import cat.itacademy.solid.d.model.Booking;
import cat.itacademy.solid.d.repository.H2BookingRepository;
import cat.itacademy.solid.d.service.BookingService;

import java.io.PrintStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.NoSuchElementException;

public class BookingConsole {

    private static final int MENU_WIDTH = 48;
    private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final H2BookingRepository repository;
    private final BookingService service;
    private final ConsoleInput input;
    private final PrintStream out;

    public BookingConsole(
            H2BookingRepository repository,
            BookingService service,
            ConsoleInput input,
            PrintStream out
    ) {
        this.repository = repository;
        this.service = service;
        this.input = input;
        this.out = out;
    }

    public void run() {
        try {
            while (true) {
                printMenu();

                switch (input.readIntInRange("Tria una opció (0-4): ", 0, 4)) {
                    case 1 -> listBookings();
                    case 2 -> showBooking();
                    case 3 -> createBooking();
                    case 4 -> cancelBooking();
                    default -> {
                        out.println("Adéu!");
                        return;
                    }
                }

                out.println();
                input.waitForEnter("Prem Enter per continuar...");
            }
        } catch (NoSuchElementException e) {
            out.println();
            out.println("Adéu!");
        }
    }

    private void printMenu() {
        out.println();
        out.println("╔" + "═".repeat(MENU_WIDTH) + "╗");
        out.println("║" + center("RESERVES DE SALES") + "║");
        out.println("╠" + "═".repeat(MENU_WIDTH) + "╣");
        printMenuLine("Persistència : H2 (base de dades en memòria)");
        printMenuLine("Hora actual  : LocalDateTime.now()");
        out.println("╠" + "═".repeat(MENU_WIDTH) + "╣");
        printMenuLine("[1] Llistar totes les reserves");
        printMenuLine("[2] Veure reserva per id");
        printMenuLine("[3] Crear reserva nova");
        printMenuLine("[4] Cancel·lar reserva");
        out.println("╟" + "─".repeat(MENU_WIDTH) + "╢");
        printMenuLine("[0] Sortir");
        out.println("╚" + "═".repeat(MENU_WIDTH) + "╝");
    }

    private void printMenuLine(String text) {
        out.println("║" + String.format("  %-" + (MENU_WIDTH - 2) + "s", text) + "║");
    }

    private String center(String text) {
        int left = (MENU_WIDTH - text.length()) / 2;
        return " ".repeat(left) + text + " ".repeat(MENU_WIDTH - text.length() - left);
    }

    private void listBookings() {
        List<Booking> bookings = repository.findAllOrderedByStart();

        if (bookings.isEmpty()) {
            out.println("No hi ha cap reserva.");
            return;
        }

        out.println("Reserves ordenades per data d'inici:");
        printTable(bookings);
    }

    private void printTable(List<Booking> bookings) {
        String[] headers = {"ID", "CLIENT", "SALA", "INICI", "FI", "ESTAT"};
        List<String[]> rows = bookings.stream()
                .map(b -> new String[]{
                        String.valueOf(b.getId()),
                        b.getCustomerId(),
                        b.getRoomId(),
                        b.getStartsAt().format(FORMAT),
                        b.getEndsAt().format(FORMAT),
                        b.getStatus().name()
                })
                .toList();

        int[] widths = new int[headers.length];
        for (int i = 0; i < headers.length; i++) {
            widths[i] = headers[i].length();
            for (String[] row : rows) {
                widths[i] = Math.max(widths[i], row[i].length());
            }
        }

        out.println(border('┌', '┬', '┐', widths));
        out.println(row(headers, widths));
        out.println(border('├', '┼', '┤', widths));
        rows.forEach(r -> out.println(row(r, widths)));
        out.println(border('└', '┴', '┘', widths));
        out.println(rows.size() + (rows.size() == 1 ? " reserva" : " reserves"));
    }

    private String border(char left, char middle, char right, int[] widths) {
        StringBuilder line = new StringBuilder().append(left);
        for (int i = 0; i < widths.length; i++) {
            line.append("─".repeat(widths[i] + 2));
            line.append(i < widths.length - 1 ? middle : right);
        }
        return line.toString();
    }

    private String row(String[] cells, int[] widths) {
        StringBuilder line = new StringBuilder("│");
        for (int i = 0; i < cells.length; i++) {
            line.append(String.format(" %-" + widths[i] + "s │", cells[i]));
        }
        return line.toString();
    }

    private void createBooking() {
        String customerId = input.readText("Client: ");
        String roomId = input.readText("Sala: ");
        long hoursFromNow = input.readLong("Inicia d'aquí a quantes hores? ", 0);
        long durationHours = input.readLong("Durada en hores: ", 1);

        LocalDateTime startsAt = LocalDateTime.now().plusHours(hoursFromNow);
        Booking saved = repository.save(
                new Booking(customerId, roomId, startsAt, startsAt.plusHours(durationHours))
        );

        out.println("Reserva creada amb id " + saved.getId());
    }

    private void showBooking() {
        long id = input.readLong("Id de la reserva: ", 1);

        repository.findById(id).ifPresentOrElse(
                this::print,
                () -> out.println("No existeix la reserva " + id)
        );
    }

    private void cancelBooking() {
        long id = input.readLong("Id de la reserva: ", 1);
        String customerId = input.readText("Client: ");

        try {
            service.cancelBooking(customerId, id);
            out.println("Reserva " + id + " cancel·lada.");
        } catch (BookingAccessDeniedException | CancellationPeriodExpiredException
                 | IllegalStateException e) {
            out.println("No s'ha pogut cancel·lar: " + e.getMessage());
        }
    }

    private void print(Booking booking) {
        out.println("Reserva " + booking.getId());
        out.println("  Client : " + booking.getCustomerId());
        out.println("  Sala   : " + booking.getRoomId());
        out.println("  Inici  : " + booking.getStartsAt().format(FORMAT));
        out.println("  Fi     : " + booking.getEndsAt().format(FORMAT));
        out.println("  Estat  : " + booking.getStatus());
        if (booking.getCancelledAt() != null) {
            out.println("  Cancel·lada: " + booking.getCancelledAt().format(FORMAT));
        }
    }
}
