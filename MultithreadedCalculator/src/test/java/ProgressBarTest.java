import homework2.realise.ProgressBar;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProgressBarTest {
    private final int totalSteps = 20;
    private ProgressBar progressbar;

    @BeforeEach
    void setUp() {
        this.progressbar = new ProgressBar(totalSteps);
    }

    @Test
    void progressBarReturnTest() {
        int currentSteps = 10;
        String outputBar = progressbar.showProgress(currentSteps);
        String expectedBar = "[==========----------]";
        assertEquals(expectedBar, outputBar);
    }
}
