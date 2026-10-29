package cat.itacademy.solid.d.console_menu;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Llegeix dades de la consola. Si el valor no és vàlid, mostra un missatge
 * i torna a preguntar fins obtenir-ne un de correcte.
 */
public class ConsoleInput {

    private final Scanner in;
    private final PrintStream out;

    public ConsoleInput(InputStream in, PrintStream out) {
        this.in = new Scanner(in);
        this.out = out;
    }

    public String readText(String prompt) {
        while (true) {
            String value = readLine(prompt);
            if (!value.isEmpty()) {
                return value;
            }
            out.println("El valor no pot estar buit. Torna-ho a provar.");
        }
    }

    public long readLong(String prompt, long min) {
        while (true) {
            String value = readLine(prompt);
            try {
                long number = Long.parseLong(value);
                if (number >= min) {
                    return number;
                }
                out.println("El número ha de ser com a mínim " + min + ". Torna-ho a provar.");
            } catch (NumberFormatException e) {
                out.println("'" + value + "' no és un número vàlid. Torna-ho a provar.");
            }
        }
    }

    public int readIntInRange(String prompt, int min, int max) {
        while (true) {
            String value = readLine(prompt);
            try {
                int number = Integer.parseInt(value);
                if (number >= min && number <= max) {
                    return number;
                }
            } catch (NumberFormatException ignored) {
                // es tracta igual que un número fora de rang
            }
            out.println("Opció no vàlida. Introdueix un número entre " + min + " i " + max + ".");
        }
    }

    public void waitForEnter(String prompt) {
        readLine(prompt);
    }

    private String readLine(String prompt) {
        out.print(prompt);
        if (!in.hasNextLine()) {
            throw new NoSuchElementException("No hi ha més dades d'entrada");
        }
        return in.nextLine().trim();
    }
}
