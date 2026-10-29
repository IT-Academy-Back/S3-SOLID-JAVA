package cat.itacademy.solid.s;

import java.math.BigDecimal;

public record InvoiceLine(
        String description,
        int quantity,
        BigDecimal unitPrice
) {
}
