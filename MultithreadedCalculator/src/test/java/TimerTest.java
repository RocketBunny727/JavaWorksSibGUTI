import homework2.realise.Timer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TimerTest {
    private Timer timer;

    @BeforeEach
    void setUp() {
        this.timer = new Timer();
    }

    @Test
    void TimeTest() throws InterruptedException {
        long expectedTime = 1000;
        timer.start();
        Thread.sleep(expectedTime);
        long elapsedTime = timer.stop();
        long errorRate = Math.abs(elapsedTime - expectedTime);
        assertTrue(errorRate <= 50);
    }
}
