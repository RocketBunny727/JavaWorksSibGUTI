package homework2.realise;

public class Calculator implements ICalculator, Runnable {
    private final int threadNumber;
    private final int calculationLength;
    private final int complexity;

    public Calculator(
            int threadNumber,
            int calculationLength,
            int complexity) {
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
        // String posY = "\n".repeat(threadNumber - 1);
        ProgressBar progressBar = new ProgressBar(calculationLength);
        Timer timer = new Timer();
        timer.start();

        for (int i = 1; i <= calculationLength; i++) {
            System.out.printf("\rThread %d [%d]: %s",
                    threadNumber,
                    Thread.currentThread().threadId(),
                    progressBar.showProgress(i));

            try {
                Thread.sleep(complexity);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        long elapsedTime = timer.stop();
        System.out.printf("\rThread %d [%d] completed in %d ms.%n",
                threadNumber, Thread.currentThread().threadId(), elapsedTime);
    }
}
