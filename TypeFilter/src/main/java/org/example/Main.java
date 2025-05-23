package org.example;

public class Main {
    public static void main(String[] args) throws Exception {
        String greeting = """
                ___________                      ___________.__.__   __                  ____    _______  \s
                \\__    ___/__.__.______   ____   \\_   _____/|__|  |_/  |_  ___________  /_   |   \\   _  \\ \s
                  |    | <   |  |\\____ \\_/ __ \\   |    __)  |  |  |\\   __\\/ __ \\_  __ \\  |   |   /  /_\\  \\\s
                  |    |  \\___  ||  |_> >  ___/   |     \\   |  |  |_|  | \\  ___/|  | \\/  |   |   \\  \\_/   \\
                  |____|  / ____||   __/ \\___  >  \\___  /   |__|____/__|  \\___  >__|     |___| /\\ \\_____  /
                          \\/     |__|        \\/       \\/                      \\/               \\/       \\/\s
                """;
        System.out.println(greeting);

        ArgumentParser argumentParser = new ArgumentParser();
        FileService fileService = new FileService();
        DataClassifier dataClassifier = new DataClassifier();
        StatisticsPrinter printer = new StatisticsPrinter();
        DataProcessor processor = new DataProcessor(argumentParser, fileService, dataClassifier, printer);

        processor.process(args);
    }
}