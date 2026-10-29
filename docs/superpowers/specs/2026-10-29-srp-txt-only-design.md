# Exercici S amb un únic format TXT

Proposta aprovada durant la conversa del 29 d'octubre de 2026.

L'objectiu és reduir la dificultat del codi inicial que l'alumnat ha de refactoritzar. Es conserva una aplicació funcional amb càlcul, format i escriptura barrejats en un únic mètode.

## Alternatives considerades

- Només TXT: elimina la selecció de format i l'escapament de camps. És l'opció escollida.
- TXT i CSV: conserva més funcionalitat, però afegeix branques i regles de format al primer exercici.

## Contracte

- `saveToFile(Invoice invoice, Path destination)` genera sempre TXT, independentment de l'extensió de la destinació.
- Es conserven el contingut TXT, els salts de línia, els imports amb `BigDecimal` i el total `0.00` d'una factura buida.
- Es conserva `UncheckedIOException`, amb la causa original i el missatge que inclou el número de factura.
- `InvoiceApp` conserva la destinació per defecte `output/invoices/invoice.txt` i la possibilitat de rebre una altra ruta.
- El starter manté les responsabilitats juntes, sense mètodes auxiliars ni classes noves que anticipin la solució.
- Els tests i la documentació descriuen l'exercici TXT. La resta de principis queda fora d'aquest canvi.
- Es continua treballant a `main`, amb els canvis pendents de commit, i es respecta l'eliminació prèvia de `User.java`.
