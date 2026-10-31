package com.example.androidengine;

import android.content.Context;

import com.example.engine.IFileStorage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileStorageAndroid implements IFileStorage {

    private final Context context;
    private String baseDirectory = "";
    public FileStorageAndroid(Context context) {
        this.context = context.getApplicationContext();

    }

    @Override
    public String getCurrentPath() {
        return baseDirectory;
    }

    @Override
    public void setRelativePath(String relativePath) {
        if (relativePath == null || relativePath.isEmpty()) {
            baseDirectory = "";
            return;
        }

        baseDirectory = relativePath;
    }

    @Override
    public void write(String filename, String content) throws IOException {
        File file = resolveFile(filename);

        File parent = file.getParentFile();
        if (parent != null && !parent.exists() && !parent.mkdirs()) {
            throw new IOException("Could not create directory: " + parent);
        }

        try (FileOutputStream output = new FileOutputStream(file)) {
            output.write(content.getBytes(StandardCharsets.UTF_8));
        }
    }

    @Override
    public String read(String filename) throws IOException {
        String path = resolveAssetPath(filename);

        try (InputStream input = context.getAssets().open(path)) {
            return readFromStream(input);
        }
    }

    private String readFromStream(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[8192];
        int count;
        while ((count = input.read(buffer)) != -1) {
            output.write(buffer, 0, count);
        }
        return output.toString(StandardCharsets.UTF_8.name());
    }

    private File resolveFile(String filename) throws IOException {
        File base = new File(context.getFilesDir(), baseDirectory);

        Path basePath = base.getAbsoluteFile().toPath().normalize();
        Path filePath = basePath.resolve(filename).normalize();

        if (!filePath.startsWith(basePath)) {
            throw new IOException("Invalid filename: " + filename);
        }

        return filePath.toFile();
    }

    private String resolveAssetPath(String filename) throws IOException {
        Path basePath = Paths.get(baseDirectory).normalize();
        Path filePath = basePath.resolve(filename).normalize();

        if (filePath.isAbsolute() || !filePath.startsWith(basePath)) {
            throw new IOException("Invalid filename: " + filename);
        }

        return filePath.toString().replace("\\", "/");
    }
}