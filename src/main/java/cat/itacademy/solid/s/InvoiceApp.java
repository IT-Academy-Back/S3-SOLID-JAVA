package cat.itacademy.solid.s;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class InvoiceApp {

    public static void main(String[] args) throws IOException {
        Invoice invoice = new Invoice(
                "INV-2026-001",
                "Ada Lovelace",
                List.of(
                        new InvoiceLine("Mechanical keyboard", 2, new BigDecimal("50.00")),
                        new InvoiceLine("Wireless mouse", 1, new BigDecimal("25.00"))
                )
        );

        Path destination = args.length > 0
                ? Path.of(args[0])
                : Path.of("output", "invoices", "invoice.txt");
        Files.createDirectories(destination.toAbsolutePath().getParent());

        InvoiceService invoiceService = new InvoiceService();
        invoiceService.saveToFile(invoice, destination);

        System.out.println("Invoice saved to: " + destination.toAbsolutePath());
    }
}
