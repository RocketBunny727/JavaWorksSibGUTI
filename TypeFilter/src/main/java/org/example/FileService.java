package org.example;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileService implements IFileService {
    public List<String> readFile(Path path) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(path.toString()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            System.err.println("File read error: " + e.getMessage());
        }
        return lines;
    }

    public void writeFile(String filePath, List<String> lines, boolean append) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, append))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Write to file error: " + filePath);
        }
    }
}
