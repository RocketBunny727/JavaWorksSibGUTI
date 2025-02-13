import org.example.DataClassifier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DataClassifierTest {
    private DataClassifier classifier;
    private final List<String> lines = new ArrayList<>(Arrays.asList("qwerty", "sad",  "25", "2", "15.25", "2.3"));

    @BeforeEach
    void setUp() {
        classifier = new DataClassifier();
        classifier.classifyData(lines);
    }

    @Test
    void ClassifyTest() {
        assertEquals(2, classifier.getStrings().size());
        assertEquals(2, classifier.getIntegers().size());
        assertEquals(2, classifier.getFloats().size());
    }

    @Test
    void calculateTypesCountTest() {
        assertEquals(2, classifier.getStringCount());
        assertEquals(2, classifier.getIntCount());
        assertEquals(2, classifier.getFloatCount());
    }

    @Test
    void calculateMinMaxStringLengthTest() {
        assertEquals(3, classifier.getMinStringLength());
        assertEquals(6, classifier.getMaxStringLength());
    }

    @Test
    void calculateMinMaxIntTest() {
        assertEquals(2, classifier.getMinInt());
        assertEquals(25, classifier.getMaxInt());
    }

    @Test
    void calculateMinMaxFloatTest() {
        assertEquals(2.3, classifier.getMinFloat());
        assertEquals(15.25, classifier.getMaxFloat());
    }

    @Test
    void calculateIntFloatSum() {
        assertEquals(27, classifier.getIntSum());
        assertEquals(17.55, classifier.getFloatSum());
    }
}
