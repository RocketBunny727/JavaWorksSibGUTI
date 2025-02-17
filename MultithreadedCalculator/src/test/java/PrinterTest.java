import homework2.realise.Printer;
import homework2.realise.Timer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PrinterTest {
    private Printer printer;
    private Timer timer;
    private final int THREAD_COUNT = 2;
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp() {
        final int CALCULATION_LENGTH = 20;
        printer = new Printer(CALCULATION_LENGTH);
        timer = new Timer();
        System.setOut(new PrintStream(outputStream));
    }

    @Test
    void printRunThreadInfoTest() {
        for (int i = 0; i < THREAD_COUNT; i++) {
            printer.printRunThreadInfo(i + 1, i, (i + 1) * 5, THREAD_COUNT + 3);
        }

        String actualOutput = outputStream.toString()
                .replaceAll("\033\\[\\d+;1H", "")
                .replaceAll("\\[\\d+\\]", "[ID]")
                .trim();

        String expectedOutput = String.join(System.lineSeparator(),
                "Thread 0 [ID]: [=====---------------]",
                "Thread 1 [ID]: [==========----------]"
        );

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    void printClosedThreadInfoTest() {
        timer.start();
        for (int i = 0; i < THREAD_COUNT; i++) {
            long elapsedTime = timer.stop();
            printer.printClosedThreadInfo(i + 1, i, THREAD_COUNT + 3, elapsedTime);
        }

        String actualOutput = outputStream.toString()
                .replaceAll("\033\\[\\d+;1H", "")
                .replaceAll("\\[\\d+\\]", "[ID]")
                .replaceAll("\\' in '\\d+\\' ms'", "")
                .trim();

        String expectedOutput = String.join(System.lineSeparator(),
                "Thread <0> [ID] completed.",
                "Thread <1> [ID] completed."
        );
    }
}
