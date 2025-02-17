package homework2.realise;

public class Calculator implements Runnable {
    private final int THREAD_COUNT;
    private final int THREAD_NUMBER;
    private final int CALCULATION_LENGTH;
    private final int COMPLEXITY;
    private static final Object lock = new Object();

    public Calculator(
            int threadCount,
            int threadNumber,
            int calculationLength,
            int complexity) {
        this.THREAD_COUNT = threadCount;
        this.THREAD_NUMBER = threadNumber;
        this.CALCULATION_LENGTH = calculationLength;
        this.COMPLEXITY = complexity;
    }

    @Override
    public void run() { this.calculate(); }

    public void calculate() {
        IThreadInfoPrinter printer = new Printer(CALCULATION_LENGTH);
        Timer timer = new Timer();
        timer.start();

        final int INDENTATION = 2;
        for (int i = 1; i <= CALCULATION_LENGTH; i++) {
            synchronized (lock) {
                printer.printRunThreadInfo(THREAD_NUMBER + 1 + INDENTATION, THREAD_NUMBER, i, THREAD_COUNT + 3);
            }

            try {
                Thread.sleep(COMPLEXITY);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        long elapsedTime = timer.stop();
        synchronized (lock) {
            printer.printClosedThreadInfo(THREAD_NUMBER + 1 + INDENTATION, THREAD_NUMBER, THREAD_COUNT + 3, elapsedTime);
        }
    }
}
