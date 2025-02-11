package org.example;

import java.nio.file.Path;
import java.util.List;

public interface IFileService {
    List<String> readFile(Path path);
    
    void writeFile(
            String filePath,
            List<String> lines,
            boolean append);
}
