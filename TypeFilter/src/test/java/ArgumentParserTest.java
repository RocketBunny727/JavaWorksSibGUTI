import org.example.ArgumentParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

public class ArgumentParserTest {
    private ArgumentParser parser;

    @BeforeEach
    void setUp() {
        parser = new ArgumentParser();
    }

    @Test
    void testShortStatsMode() throws Exception {
        parser.parseArguments(new String[]{"-s"});
        assertTrue(parser.getIsShortStats());
        assertFalse(parser.getIsFullStats());
    }

    @Test
    void testFullStatsMode() throws Exception {
        parser.parseArguments(new String[]{"-f"});
        assertTrue(parser.getIsFullStats());
        assertFalse(parser.getIsShortStats());
    }

    @Test
    void testConflictingStatsModesThrowsException() {
        Exception exception = assertThrows(IOException.class, () -> parser.parseArguments(new String[]{"-s", "-f"}));
        assertEquals("Short and Full modes ON (only -s or -f)", exception.getMessage());
    }

    @Test
    void testOutputPathParsing() throws Exception {
        parser.parseArguments(new String[]{"-o", "outputDir"});
        assertEquals(Paths.get("outputDir"), parser.getOutputPath());
    }

    @Test
    void testPrefixParsing() throws Exception {
        parser.parseArguments(new String[]{"-p", "testPrefix"});
        assertEquals("testPrefix_", parser.getPrefix());
    }

    @Test
    void testAppendMode() throws Exception {
        parser.parseArguments(new String[]{"-a"});
        assertTrue(parser.getIsAppendMode());
    }

    @Test
    void testMultipleFlagsParsing() throws Exception {
        parser.parseArguments(new String[]{"-p", "log"});
        assertEquals("log_", parser.getPrefix());
    }

    @Test
    void testInvalidOutputPathThrowsException() {
        Exception exception = assertThrows(IOException.class, () -> parser.parseArguments(new String[]{"-o"}));
        assertEquals("Flags or arguments error", exception.getMessage());
    }

    @Test
    void testFilePathsAreParsed() throws Exception {
        parser.parseArguments(new String[]{"src/test/resources/test.txt", "src/test/resources/parseThisFile.txt"});
        Set<Path> inputPaths = parser.getInputPaths();

        assertEquals(2, inputPaths.size());
        assertTrue(inputPaths.contains(Paths.get("src/test/resources/test.txt")));
        assertTrue(inputPaths.contains(Paths.get("src/test/resources/parseThisFile.txt")));
    }
}
