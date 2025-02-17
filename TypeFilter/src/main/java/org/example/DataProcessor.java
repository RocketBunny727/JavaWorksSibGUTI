package org.example;

import java.nio.file.Path;
import java.util.List;

public class DataProcessor {
    private final ArgumentParser parser;
    private final IFileService fileService;
    private final DataClassifier classifier;
    private final StatisticsPrinter printer;

    public DataProcessor(
            ArgumentParser parser,
            IFileService fileService,
            DataClassifier classifier,
            StatisticsPrinter printer) {
        this.parser = parser;
        this.fileService = fileService;
        this.classifier = classifier;
        this.printer = printer;
    }

    public void process(String[] args) throws Exception {
        this.parser.parseArguments(args);

        for (Path inputPath : this.parser.getInputPaths()) {
            List<String> lines = this.fileService.readFile(inputPath);
            this.classifier.classifyData(lines);
        }

        String outputPath = this.parser.getOutputPath().toString() + "/" + this.parser.getPrefix();

        if (!this.classifier.getIntegers().isEmpty()) {
            this.fileService.writeFile(outputPath + "integers.txt", this.classifier.getIntegers(), this.parser.getIsAppendMode());
        }
        if (!this.classifier.getFloats().isEmpty()) {
            this.fileService.writeFile(outputPath + "floats.txt", this.classifier.getFloats(), this.parser.getIsAppendMode());
        }
        if (!this.classifier.getStrings().isEmpty()) {
            this.fileService.writeFile(outputPath + "strings.txt", this.classifier.getStrings(), this.parser.getIsAppendMode());
        }

        System.out.println("Processing complete.");
        this.printer.printStats(this.parser.getIsShortStats(), this.parser.getIsFullStats(), this.classifier);
    }
}
