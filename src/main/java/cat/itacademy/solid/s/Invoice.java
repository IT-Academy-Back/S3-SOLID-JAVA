package cat.itacademy.solid.s;

import java.util.List;

public record Invoice(
        String number,
        String customerName,
        List<InvoiceLine> lines
) {

    public Invoice {
        lines = List.copyOf(lines);
    }
}
