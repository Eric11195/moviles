package com.example.desktopengine;

import com.example.engine.IFileStorage;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.stream.Stream;

public class FileStorageDesktop implements IFileStorage {

    private final Path rootPath = Paths.get("./assets").toAbsolutePath().normalize();
    private Path basePath = rootPath;

    @Override
    public String getCurrentPath() {
        return rootPath.relativize(basePath).toString().replace("\\", "/");
    }

    @Override
    public void setRelativePath(String relativePath) {
        if (relativePath == null || relativePath.isEmpty()) {
            basePath = rootPath;
            return;
        }

        Path resolved = rootPath.resolve(relativePath).normalize();

        if (!resolved.startsWith(rootPath)) {
            throw new IllegalArgumentException("Invalid relative path: " + relativePath);
        }

        basePath = resolved;
    }

    @Override
    public void writeModifiable(String filename, String content) throws IOException {
        Path filePath = resolveFile(filename);

        if (filePath.getParent() != null) {
            Files.createDirectories(filePath.getParent());
        }

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
        Path filePath = resolveFile(filename);
        return Files.readString(filePath, StandardCharsets.UTF_8);
    }

    @Override
    public String readModifiable(String filename) throws IOException {
        Path filePath = resolveFile(filename);

        if (!Files.exists(filePath) || !Files.isRegularFile(filePath)) {
            return null;
        }

        try {
            return Files.readString(filePath, StandardCharsets.UTF_8);
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public boolean forEachFileOnDirectory(ForEachFileOnDirectoryFunc f) {
        if (!Files.isDirectory(basePath)) {
            return false;
        }

        try (Stream<Path> entries = Files.list(basePath)) {
            for (Path entry : (Iterable<Path>) entries::iterator) {
                String fileName = entry.getFileName().toString();
                if (!fileName.startsWith(".")) {
                    f.func(fileName);
                }
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private Path resolveFile(String filename) throws IOException {
        Path filePath = basePath.resolve(filename).normalize();

        if (!filePath.startsWith(basePath)) {
            throw new IOException("Invalid filename: " + filename);
        }

        return filePath;
    }
}