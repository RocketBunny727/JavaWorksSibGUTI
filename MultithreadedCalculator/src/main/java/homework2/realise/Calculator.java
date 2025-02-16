package homework2.realise;

public class Calculator implements ICalculator, Runnable {
    private final int threadNumber;
    private final int calculationLength;
    private final int complexity;
    private final int INDENTATION = 2;
    private static final Object lock = new Object();
    private int threadCount;

    public Calculator(
            int threadCount,
            int threadNumber,
            int calculationLength,
            int complexity) {
        this.threadCount = threadCount;
        this.threadNumber = threadNumber;
        this.calculationLength = calculationLength;
        this.complexity = complexity;
    }

    @Override
    public void run() {
        this.calculate();
    }

    @Override
    public void calculate() {
        ProgressBar progressBar = new ProgressBar(calculationLength);
        Timer timer = new Timer();
        timer.start();

        for (int i = 1; i <= calculationLength; i++) {
            synchronized (lock) {
                System.out.println(String.format("\033[%d;1HThread %d [%d]: %s\033[%d;1H",
                        threadNumber + 1 + INDENTATION,
                        threadNumber,
                        Thread.currentThread().threadId(),
                        progressBar.showProgress(i),
                        threadCount + 3));
            }

            try {
                Thread.sleep(complexity);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        long elapsedTime = timer.stop();
        synchronized (lock) {
            System.out.println(String.format("\033[%d;1HThread <%d> [%d] completed in %d ms.\033[%d;1H",
                    threadNumber + 1 + INDENTATION,
                    threadNumber,
                    Thread.currentThread().threadId(),
                    elapsedTime,
                    threadCount + 3
            ));
        }
    }
}
