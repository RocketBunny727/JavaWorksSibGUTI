package homework2.realise;

public class Printer implements IThreadInfoPrinter{
    private final ProgressBar progressBar;

    public Printer(int CALCULATION_LENGTH) {
        progressBar = new ProgressBar(CALCULATION_LENGTH);
    }

    @Override
    public void printRunThreadInfo(int posY,
                                   int threadNumber,
                                   int step,
                                   int indentation) {
        System.out.println(String.format("\033[%d;1HThread %d [%d]: %s\033[%d;1H",
                posY,
                threadNumber,
                Thread.currentThread().threadId(),
                progressBar.showProgress(step),
                indentation));
    }

    @Override
    public void printClosedThreadInfo(int posY,
                                      int threadNumber,
                                      int indentation,
                                      long elapsedTime) {
        System.out.println(String.format("\033[%d;1HThread <%d> [%d] completed in %d ms.\033[%d;1H",
                posY,
                threadNumber,
                Thread.currentThread().threadId(),
                elapsedTime,
                indentation));
    }
}
