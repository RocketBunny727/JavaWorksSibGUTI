import org.example.DataClassifier;
import org.example.StatisticsPrinter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StatisticsPrinterTest {
    private StatisticsPrinter printer;
    private DataClassifier classifier;
    private final List<String> lines = new ArrayList<>(Arrays.asList("qwrefe", "25", "2.3"));
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    @BeforeEach
    void setUp() {
        printer = new StatisticsPrinter();
        classifier = new DataClassifier();
        classifier.classifyData(lines);
        System.setOut(new PrintStream(outputStream));
    }

    @Test
    void testPrintShortStats() {
        String expectedRes = """
                    Count of strings: %d
                    Count of integers: %d
                    Count of floats: %d
                    """.formatted(classifier.getStringCount(), classifier.getIntCount(), classifier.getFloatCount());
        printer.printStats(true, false, classifier);
        assertEquals(expectedRes.trim(), outputStream.toString().trim());
    }

    @Test
    void testPrintFullStats() {
        String expectedRes = """
                    Count of strings: %d, Max length: %d, Min length: %d.
                    Count of integers: %d, Sum: %d, Max: %d, Min: %d.
                    Count of floats: %d, Sum: %f, Max: %f, Min: %f.
                    """.formatted(classifier.getStringCount(), classifier.getMaxStringLength(), classifier.getMinStringLength(),
                classifier.getIntCount(), classifier.getIntSum(), classifier.getMaxInt(), classifier.getMinInt(),
                classifier.getFloatCount(), classifier.getFloatSum(), classifier.getMaxFloat(), classifier.getMinFloat());
        printer.printStats(false, true, classifier);
        assertEquals(expectedRes.trim(), outputStream.toString().trim());
    }
}
