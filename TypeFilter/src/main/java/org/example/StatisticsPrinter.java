package org.example;

public class StatisticsPrinter {
    public void printStats(boolean isShortStats,
                           boolean isFullStats,
                           DataClassifier classifier) {
        String result;

        if (isFullStats) {
            result = """
                    Count of strings: %d, Max length: %d, Min length: %d.
                    Count of integers: %d, Sum: %d, Max: %d, Min: %d.
                    Count of floats: %d, Sum: %f, Max: %f, Min: %f.
                    """.formatted(classifier.getStringCount(), classifier.getMaxStringLength(), classifier.getMinStringLength(),
                    classifier.getIntCount(), classifier.getIntSum(), classifier.getMaxInt(), classifier.getMinInt(),
                    classifier.getFloatCount(), classifier.getFloatSum(), classifier.getMaxFloat(), classifier.getMinFloat());
            System.out.println(result);
        } else if (isShortStats){
            result = """
                    Count of strings: %d
                    Count of integers: %d
                    Count of floats: %d
                    """.formatted(classifier.getStringCount(), classifier.getIntCount(), classifier.getFloatCount());
            System.out.println(result);
        }
    }
}
