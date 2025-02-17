package homework2.realise;

import java.util.ArrayList;
import java.util.List;

public class ThreadsMaker {
    final int CALCULATION_LENGTH;
    List<Thread> threads = new ArrayList<>();

    public ThreadsMaker(int CALCULATION_LENGTH) {
        this.CALCULATION_LENGTH = CALCULATION_LENGTH;
    }

    public void createThreads(int threadCount) throws InterruptedException {
        for (int i = 0; i < threadCount; i++) {
            int complexity = (int) (Math.random() * 10) + 1;
            complexity *= 100;
            Runnable calculator = new Calculator(threadCount, i, CALCULATION_LENGTH, complexity);
            Thread thread = new Thread(calculator);
            threads.add(thread);
            thread.start();
        }

        for (Thread thread : threads) {
            thread.join();
        }
        System.out.println("\nAll threads completed and closed.");
    }
}
