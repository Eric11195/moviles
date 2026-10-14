package com.example.desktopengine;

import com.example.engine.IFileStorage;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class FileStorageDesktop implements IFileStorage {

    private Path basePath = Paths.get(".");

    @Override
    public String getCurrentPath() {
        return basePath.toString();
    }

    @Override
    public void setRelativePath(String relativePath) {
        basePath = Paths.get(relativePath).toAbsolutePath().normalize();
    }

    @Override
    public void write(String filename, String content) throws IOException {
        Path filePath = basePath.resolve(filename).normalize();

        // Prevent paths from escaping the base directory.
        if (!filePath.startsWith(basePath)) {
            throw new IOException("Invalid filename: " + filename);
        }

        // Create the base directory if necessary.
        Files.createDirectories(filePath.getParent());

        Files.writeString(
                filePath,
                content,
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING,
                StandardOpenOption.WRITE
        );
    }

    @Override
    public String read(String filename) throws IOException {
        Path filePath = basePath.resolve(filename).normalize();

        // Prevent paths from escaping the base directory.
        if (!filePath.startsWith(basePath)) {
            throw new IOException("Invalid filename: " + filename);
        }

        return Files.readString(filePath, StandardCharsets.UTF_8);
    }
}