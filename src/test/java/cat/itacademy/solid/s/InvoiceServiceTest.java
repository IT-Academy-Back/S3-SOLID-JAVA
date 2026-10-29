package cat.itacademy.solid.s;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.UncheckedIOException;
import java.math.BigDecimal;
import java.nio.file.FileSystemException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InvoiceServiceTest {

    private InvoiceService invoiceService;
    private Invoice invoice;

    @BeforeEach
    void setUp() {
        invoiceService = new InvoiceService();

        invoice = new Invoice(
                "INV-2026-001",
                "Ada Lovelace",
                List.of(
                        new InvoiceLine(
                                "Mechanical keyboard",
                                2,
                                new BigDecimal("50.00")
                        ),
                        new InvoiceLine(
                                "Wireless mouse",
                                1,
                                new BigDecimal("25.00")
                        )
                )
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"invoice.txt", "invoice.csv", "invoice.CSV", "invoice"})
    void savedInvoiceShouldHaveExpectedFormat(String fileName, @TempDir Path tempDirectory)
            throws Exception {
        Path destination = tempDirectory.resolve(fileName);
        invoiceService.saveToFile(invoice, destination);

        String lineSeparator = System.lineSeparator();

        String expected =
                "INVOICE" + lineSeparator +
                        "Number: INV-2026-001" + lineSeparator +
                        "Customer: Ada Lovelace" + lineSeparator +
                        lineSeparator +
                        "Mechanical keyboard - 2 x 50.00 = 100.00" + lineSeparator +
                        "Wireless mouse - 1 x 25.00 = 25.00" + lineSeparator +
                        lineSeparator +
                        "TOTAL: 125.00";

        assertEquals(
                expected,
                Files.readString(destination)
        );
    }

    @Test
    void shouldSaveCalculatedInvoiceTotal(@TempDir Path tempDirectory)
            throws Exception {
        Path destination = tempDirectory.resolve("invoice.txt");
        invoiceService.saveToFile(invoice, destination);

        assertTrue(Files.readAllLines(destination).contains("TOTAL: 125.00"));
    }

    @Test
    void shouldSaveInvoiceText(@TempDir Path tempDirectory)
            throws Exception {
        Path destination = tempDirectory.resolve("invoice.txt");
        invoiceService.saveToFile(invoice, destination);
        String result = Files.readString(destination);

        assertTrue(result.contains("INV-2026-001"));
        assertTrue(result.contains("Ada Lovelace"));
        assertTrue(result.contains("Mechanical keyboard"));
        assertTrue(result.contains("Wireless mouse"));
        assertTrue(result.contains("TOTAL: 125.00"));
    }

    @Test
    void shouldSaveInvoiceToFile(@TempDir Path tempDirectory) throws Exception {

        Path destination =
                tempDirectory.resolve("invoice.txt");

        invoiceService.saveToFile(
                invoice,
                destination
        );

        assertTrue(Files.exists(destination));

        String savedContent =
                Files.readString(destination);

        assertTrue(
                savedContent.contains("INV-2026-001")
        );

        assertTrue(
                savedContent.contains("TOTAL: 125.00")
        );
    }

    @Test
    void shouldPreserveWriteFailureDetails(@TempDir Path tempDirectory) {
        UncheckedIOException exception = assertThrows(
                UncheckedIOException.class,
                () -> invoiceService.saveToFile(invoice, tempDirectory)
        );

        assertEquals("Could not save invoice: INV-2026-001", exception.getMessage());

        FileSystemException cause = assertInstanceOf(
                FileSystemException.class,
                exception.getCause()
        );
        assertEquals(tempDirectory.toString(), cause.getFile());
    }

    @Test
    void shouldSaveEmptyInvoiceWithZeroTotal(@TempDir Path tempDirectory)
            throws Exception {
        Invoice emptyInvoice = new Invoice("INV-2026-001", "Ada Lovelace", List.of());
        Path destination = tempDirectory.resolve("invoice.txt");

        invoiceService.saveToFile(emptyInvoice, destination);

        String expected = String.join(
                System.lineSeparator(),
                "INVOICE",
                "Number: INV-2026-001",
                "Customer: Ada Lovelace",
                "",
                "",
                "TOTAL: 0.00"
        );

        assertEquals(expected, Files.readString(destination));
    }
}
