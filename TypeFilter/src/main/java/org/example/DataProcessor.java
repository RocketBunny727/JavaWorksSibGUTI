package org.example;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class DataProcessor {
    List<Path> inputPaths = new ArrayList<>();
    private Path outputPath = Paths.get("src/main/java/org/example/files/results");
    private String prefix = "";
    private boolean isPrefixMode = false;
    private boolean isAppendMode = false;
    private boolean isShortStats = false;
    private boolean isFullStats = false;
    private boolean isOutMode = false;

    private int intCount = 0;
    private int floatCount = 0;
    private int stringCount = 0;
    private long intSum = 0;
    private double floatSum = 0.0;
    private int minInt = 0;
    private int maxInt = 0;
    private double minFloat = 0;
    private double maxFloat = 0;
    private int minStringLength = 0;
    private int maxStringLength = 0;

    public void filterFiles(String[] arguments) throws Exception {
        analiseArgs(arguments);
        String outputPathLine = this.outputPath.toString() + "/" + prefix;

        List<String> integers = new ArrayList<>();
        List<String> floats = new ArrayList<>();
        List<String> strings = new ArrayList<>();

        for (Path inputPath : inputPaths) {
            String inputPathLine = inputPath.toString();
            try (BufferedReader reader = new BufferedReader(new FileReader(inputPathLine))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    analiseInputFileLine(line, integers, floats, strings);
                }
            } catch (IOException e) {
                System.err.println("File error: " + e.getMessage());
            }
        }

        writeToFile(outputPathLine + "integers.txt", integers);
        writeToFile(outputPathLine + "floats.txt", floats);
        writeToFile(outputPathLine + "strings.txt", strings);

        displayResults();
    }

    private void analiseArgs(String[] arguments) throws Exception {
        if (arguments.length == 0) {
            System.out.println("No following console arguments.");
            throw new IOException("No following console arguments.");
        }

        for (String argument : arguments) {
            switch (argument) {
                case "-o":
                    this.isOutMode = true;
                    break;

                case "-p":
                    this.isPrefixMode = true;
                    break;

                case "-a":
                    this.isAppendMode = true;
                    break;

                case "-s":
                    if (isFullStats) {
                        System.out.println("Short and Full modes ON (only -s or -f)");
                        throw new IOException("Short and Full modes ON (only -s or -f)");
                    }
                    this.isShortStats = true;
                    break;

                case "-f":
                    if (isShortStats) {
                        System.out.println("Short and Full modes ON (only -s or -f)");
                        throw new IOException("Short and Full modes ON (only -s or -f)");
                    }
                    this.isFullStats = true;
                    break;
            }
        }

        int i = 0;
        while (i < arguments.length && !isEndOfInputPaths(arguments[i])) {
            Path path = Paths.get(arguments[i]);

            if (Files.exists(path)) {
                try (BufferedReader reader = new BufferedReader(new FileReader(path.toString()))) {
                    this.inputPaths.add(path);
                } catch (IOException e) {
                    System.err.println("Error with file (" + arguments[i] + "): " + e.getMessage());
                }
            } else {
                System.err.println("File not found: " + arguments[i]);
            }
            i++;
        }

        if (isOutMode) {
            if (i < arguments.length - 1 && arguments[i].equals("-o")) {
                outputPath = Paths.get(arguments[i + 1]);
                if (!Files.exists(outputPath)) {
                    Files.createDirectories(outputPath);
                }
                if (!Files.isWritable(outputPath)) {
                    throw new IOException("Invalid results path: " + outputPath);
                }
                i += 2;
            } else {
                throw new IOException("Flags or arguments error");
            }
        }

        if (isPrefixMode) {
            if (i < arguments.length - 1 && arguments[i].equals("-p")) {
                this.prefix = arguments[i + 1];
                if (!this.prefix.endsWith("_")) {
                    this.prefix += "_";
                }
                i++;
            }
        }
    }

    private boolean isEndOfInputPaths(String argument) {
        return switch (argument) {
            case "-o", "-p", "-a", "-s", "-f" -> true;
            default -> false;
        };
    }

    private void analiseInputFileLine(String line, List<String> integers, List<String> floats, List<String> strings) {
        if (line == null || line.isEmpty()) {
            System.out.println("Line is empty or NULL");
            return;
        }

        try {
            int intValue = Integer.parseInt(line);

            if (this.intCount == 0) {
                this.maxInt = intValue;
                this.minInt = intValue;
            }

            this.intCount++;
            if (intValue > this.maxInt) { this.maxInt = intValue; }
            if (intValue < this.minInt) { this.minInt = intValue; }
            this.intSum += intValue;

            integers.add(line);
            return;
        } catch (NumberFormatException e) {
            // UNLUCKY
        }

        try {
            float floatValue = Float.parseFloat(line);

            if (this.floatCount == 0) {
                this.maxFloat = floatValue;
                this.minFloat = floatValue;
            }

            this.floatCount++;
            if (floatValue > this.maxFloat) { this.maxFloat = floatValue; }
            if (floatValue < this.minFloat) { this.minFloat = floatValue; }
            this.floatSum += floatValue;

            floats.add(line);
            return;
        } catch (NumberFormatException e) {
            // UNLUCKY
        }

        if (stringCount == 0) {
            this.maxStringLength = line.length();
            this.minStringLength = line.length();
        }

        this.stringCount++;
        if (line.length() > this.maxStringLength) { this.maxStringLength = line.length(); }
        if (line.length() < this.minStringLength) { this.minStringLength = line.length(); }

        strings.add(line);
    }

    private void writeToFile(String filePath, List<String> lines) {
        boolean append = isAppendMode; // Управляем режимом
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, append))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Write to file error: " + filePath);
            e.printStackTrace();
        }
    }

    private void displayResults() {
        String result;

        if (isShortStats) {
            result = """
                    Count of strings: %d
                    Count of integers: %d
                    Count of floats: %d
                    """.formatted(this.stringCount, this.intCount, this.floatCount);
            System.out.println(result);
        } else if (isFullStats) {
            result = """
                    Count of strings: %d, Max length: %d, Min length: %d.
                    Count of integers: %d, Sum: %d, Max: %d, Min: %d.
                    Count of floats: %d, Sum: %f, Max: %f, Min: %f.
                    """.formatted(this.stringCount, this.maxStringLength, this.minStringLength, this.intCount, this.intSum, this.maxInt,
                    this.minInt, this.floatCount, this.floatSum, this.maxFloat, this.minFloat);
            System.out.println(result);
        } else {
            System.out.println("User did not select the parameters for viewing the results");
        }
    }
}

// call: Main.java inputPath#1 ... inputPath#n -o outputPath -p prefix -a -s/f