package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ArgumentParser {
    private final List<Path> inputPaths = new ArrayList<>();
    private Path outputPath = Paths.get("src/main/java/org/example/files/results");
    private String prefix = "";
    private boolean appendMode = false;
    private boolean isOutMode = false;
    private boolean isPrefixMode = false;
    private boolean isShortStats = false;
    private boolean isFullStats = false;

    public void parseArguments(String[] arguments) throws Exception {
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
                    this.appendMode = true;
                    break;

                case "-s":
                    if (isFullStats) {
                        throw new IOException("Short and Full modes ON (only -s or -f)");
                    }
                    this.isShortStats = true;
                    break;

                case "-f":
                    if (isShortStats) {
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

    public List<Path> getInputPaths() {
        return inputPaths;
    }

    public Path getOutputPath() {
        return outputPath;
    }

    public String getPrefix() {
        return prefix;
    }

    public boolean isAppendMode() {
        return appendMode;
    }

    public boolean isShortStats() {
        return isShortStats;
    }

    public boolean isFullStats() {
        return isFullStats;
    }
}
