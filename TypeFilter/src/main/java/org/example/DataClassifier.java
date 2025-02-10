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

            intCount++;
            intSum += intValue;
            minInt = Math.min(minInt, intValue);
            maxInt = Math.max(maxInt, intValue);

            integers.add(line);
            return;
        } catch (NumberFormatException ignored) {}

        try {
            float floatValue = Float.parseFloat(line);

            floatCount++;
            floatSum += floatValue;
            minFloat = Math.min(minFloat, floatValue);
            maxFloat = Math.max(maxFloat, floatValue);

            floats.add(line);
            return;
        } catch (NumberFormatException ignored) {}

        stringCount++;
        int length = line.length();
        minStringLength = Math.min(minStringLength, length);
        maxStringLength = Math.max(maxStringLength, length);

        strings.add(line);
    }

    public List<String> getIntegers() { return integers; }
    public List<String> getFloats() { return floats; }
    public List<String> getStrings() { return strings; }

    public int getIntCount() { return intCount; }
    public int getFloatCount() { return floatCount; }
    public int getStringCount() { return stringCount; }

    public long getIntSum() { return intSum; }
    public double getFloatSum() { return floatSum; }

    public int getMinInt() { return intCount > 0 ? minInt : 0; }
    public int getMaxInt() { return intCount > 0 ? maxInt : 0; }

    public double getMinFloat() { return floatCount > 0 ? minFloat : 0; }
    public double getMaxFloat() { return floatCount > 0 ? maxFloat : 0; }

    public int getMinStringLength() { return stringCount > 0 ? minStringLength : 0; }
    public int getMaxStringLength() { return stringCount > 0 ? maxStringLength : 0; }
}
