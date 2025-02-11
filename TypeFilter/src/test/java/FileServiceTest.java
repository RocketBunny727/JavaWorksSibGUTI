import org.example.FileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileServiceTest {
    private FileService fileService;
    private final Path testPath = Paths.get("src/test/resources/test.txt");
    private final List<String> testFileLines = new ArrayList<>(Arrays.asList("qwerty", "sad", "25", "2", "15.25", "2.3"));

    @BeforeEach
    void setUp() {
        fileService = new FileService();
    }

    @Test
    void readFileTest() {
        assertEquals(testFileLines, fileService.readFile(testPath));
    }
}
