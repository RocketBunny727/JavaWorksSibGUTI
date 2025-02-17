package homework2.realise;

public interface IThreadInfoPrinter {
    void printRunThreadInfo(int posY,
                            int threadNumber,
                            int step,
                            int indentation);

    void printClosedThreadInfo(int posY,
                               int threadNumber,
                               int indentation,
                               long elapsedTime);
}
