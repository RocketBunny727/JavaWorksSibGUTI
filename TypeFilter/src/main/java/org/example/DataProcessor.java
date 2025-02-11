package org.example;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class DataProcessor {
    private final ArgumentParser parser;
    private final IFileService fileService;
    private final DataClassifier classifier;
    private final StatisticsPrinter printer;

    public DataProcessor(ArgumentParser parser, IFileService fileService, DataClassifier classifier, StatisticsPrinter printer) {
        this.parser = parser;
        this.fileService = fileService;
        this.classifier = classifier;
        this.printer = printer;
    }

    public void process(String[] args) throws Exception {
        parser.parseArguments(args);

        for (Path inputPath : parser.getInputPaths()) {
            List<String> lines = fileService.readFile(inputPath);
            classifier.classifyData(lines);
        }

        String outputPath = parser.getOutputPath().toString() + "/" + parser.getPrefix();

        if (!classifier.getIntegers().isEmpty()) {
            fileService.writeFile(outputPath + "integers.txt", classifier.getIntegers(), parser.isAppendMode());
        }
        if (!classifier.getFloats().isEmpty()) {
            fileService.writeFile(outputPath + "floats.txt", classifier.getFloats(), parser.isAppendMode());
        }
        if (!classifier.getStrings().isEmpty()) {
            fileService.writeFile(outputPath + "strings.txt", classifier.getStrings(), parser.isAppendMode());
        }

        System.out.println("Processing complete.");
        printer.printStats(parser.isShortStats(), parser.isFullStats(), classifier);
    }
}
