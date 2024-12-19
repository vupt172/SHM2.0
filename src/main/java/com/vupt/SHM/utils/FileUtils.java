package com.vupt.SHM.utils;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.util.stream.Stream;

public class FileUtils {
    public static String readFileToString(String filePath) {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(filePath, new String[0]), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s).append("\n"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        String fileContent = contentBuilder.toString();
        return fileContent;
    }

    public static void createFolder(String folderPath) throws IOException {
        Path path = Paths.get(folderPath, new String[0]);
        if (!Files.exists(path, new java.nio.file.LinkOption[0]))
            Files.createDirectories(path, (FileAttribute<?>[]) new FileAttribute[0]);
    }

    public static void openFile(String filePath) {
        try {
            File file = new File(filePath);
            Desktop desktop = Desktop.getDesktop();

            if (file.exists()) {
                desktop.open(file);
            } else {
                System.out.println("File not found.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Path getTempFolderPath() {
        String tempDir = System.getProperty("java.io.tmpdir");
        Path tempPath = Paths.get(tempDir, new String[0]);
        System.out.println("Temporary folder path: " + tempPath);
        return tempPath;
    }
}
