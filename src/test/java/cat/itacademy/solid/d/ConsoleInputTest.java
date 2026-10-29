package cat.itacademy.solid.d;

import cat.itacademy.solid.d.console_menu.ConsoleInput;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class ConsoleInputTest {

    private final ByteArrayOutputStream bytes = new ByteArrayOutputStream();

    @Test
    void readTextShouldSkipBlankLines() {
        assertEquals("ok", input("\n   \nok\n").readText("> "));
    }

    @Test
    void readLongShouldRejectTextAndValuesBelowMinimum() {
        assertEquals(5, input("abc\n0\n5\n").readLong("> ", 1));
        assertTrue(bytes.toString().contains("no és un número vàlid"));
    }

    @Test
    void readIntInRangeShouldRepeatUntilValid() {
        assertEquals(3, input("7\nx\n3\n").readIntInRange("> ", 0, 5));
    }

    @Test
    void shouldThrowWhenInputEnds() {
        assertThrows(NoSuchElementException.class, () -> input("").readText("> "));
    }

    private ConsoleInput input(String text) {
        return new ConsoleInput(new ByteArrayInputStream(text.getBytes()), new PrintStream(bytes));
    }
}
