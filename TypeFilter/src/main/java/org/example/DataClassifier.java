package org.example;

import java.util.ArrayList;
import java.util.List;

public class DataClassifier {
    private final List<String> integers = new ArrayList<>();
    private final List<String> floats = new ArrayList<>();
    private final List<String> strings = new ArrayList<>();

    private int intCount = 0;
    private int floatCount = 0;
    private int stringCount = 0;
    private long intSum = 0;
    private double floatSum = 0.0;
    private int minInt = Integer.MAX_VALUE;
    private int maxInt = Integer.MIN_VALUE;
    private double minFloat = Double.MAX_VALUE;
    private double maxFloat = Double.MIN_VALUE;
    private int minStringLength = Integer.MAX_VALUE;
    private int maxStringLength = Integer.MIN_VALUE;

    public void classifyData(List<String> lines) {
        for (String line : lines) {
            analyzeInputFileLine(line);
        }
    }

    private void analyzeInputFileLine(String line) {
        if (line == null || line.isEmpty()) {
            System.out.println("Line is empty or NULL");
            return;
        }

        try {
            int intValue = Integer.parseInt(line);

            this.intCount++;
            this.intSum += intValue;
            this.minInt = Math.min(minInt, intValue);
            this.maxInt = Math.max(maxInt, intValue);

            this.integers.add(line);
            return;
        } catch (NumberFormatException ignored) {}

        try {
            double floatValue = Double.parseDouble(line);

            this.floatCount++;
            this.floatSum += floatValue;
            this.minFloat = Math.min(minFloat, floatValue);
            this.maxFloat = Math.max(maxFloat, floatValue);

            floats.add(line);
            return;
        } catch (NumberFormatException ignored) {}

        this.stringCount++;
        int length = line.length();
        this.minStringLength = Math.min(this.minStringLength, length);
        this.maxStringLength = Math.max(this.maxStringLength, length);

        this.strings.add(line);
    }

    public List<String> getIntegers() { return this.integers; }
    public List<String> getFloats() { return this.floats; }
    public List<String> getStrings() { return this.strings; }

    public int getIntCount() { return this.intCount; }
    public int getFloatCount() { return this.floatCount; }
    public int getStringCount() { return this.stringCount; }

    public long getIntSum() { return this.intSum; }
    public double getFloatSum() { return this.floatSum; }

    public int getMinInt() { return this.intCount > 0 ? this.minInt : 0; }
    public int getMaxInt() { return this.intCount > 0 ? this.maxInt : 0; }

    public double getMinFloat() { return this.floatCount > 0 ? this.minFloat : 0; }
    public double getMaxFloat() { return this.floatCount > 0 ? this.maxFloat : 0; }

    public int getMinStringLength() { return this.stringCount > 0 ? this.minStringLength : 0; }
    public int getMaxStringLength() { return this.stringCount > 0 ? this.maxStringLength : 0; }
}
