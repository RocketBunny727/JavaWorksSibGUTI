package homework2.realise;

public class ProgressBar implements IProgressBar {
    private final int totalSteps;

    public ProgressBar(int totalSteps) {
        this.totalSteps = totalSteps;
    }

    @Override
    public String showProgress(int currentSteps) {
        return "[" + "=".repeat(currentSteps) + "-".repeat(totalSteps - currentSteps) + "]";
    }
}
