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
    private final Path rootPath =
            Paths.get("./assets").toAbsolutePath().normalize();

    private Path basePath = rootPath;

    @Override
    public void setRelativePath(String relativePath) {
        basePath = rootPath.resolve(relativePath).normalize();

        if (!basePath.startsWith(rootPath)) {
            throw new IllegalArgumentException("Invalid relative path");
        }
    }

    @Override
    public String readModifiable(String filename) throws IOException {
        return read(filename);
    }

    @Override
    public String getCurrentPath() {
        return basePath.toString();
    }

    @Override
    public void writeModifiable(String filename, String content) throws IOException {
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

    @Override
    public boolean forEachFileOnDirectory(ForEachFileOnDirectoryFunc f) {
        try (Stream<Path> entries = Files.list(basePath)) {
            for (Path entry : (Iterable<Path>) entries::iterator) {
                String fileName = entry.getFileName().toString();

                // Ignore files/folders that start with a dot
                if (!fileName.startsWith(".")) {
                    f.func(fileName);
                }else{
                    System.out.println(">>>>>File ignored in forEachFileOnDirectory: "+fileName);
                }
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}