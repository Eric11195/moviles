package com.example.androidengine;

import android.content.Context;

import com.example.engine.IFileStorage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class FileStorageAndroid implements IFileStorage {

    private final Context context;
    private File baseDirectory;

    public FileStorageAndroid(Context context) {
        this.context = context.getApplicationContext();
        this.baseDirectory = this.context.getFilesDir();
    }

    @Override
    public String getCurrentPath() {
        return baseDirectory.getPath();
    }

    @Override
    public void setRelativePath(String relativePath) {
        baseDirectory = new File(context.getFilesDir(), relativePath)
                .getAbsoluteFile()
                .toPath()
                .normalize()
                .toFile();
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
        File file = resolveFile(filename);

        try (FileInputStream input = new FileInputStream(file)) {
            byte[] bytes = new byte[(int) file.length()];
            int offset = 0;

            while (offset < bytes.length) {
                int count = input.read(bytes, offset, bytes.length - offset);
                if (count == -1) {
                    break;
                }
                offset += count;
            }

            return new String(bytes, 0, offset, StandardCharsets.UTF_8);
        }
    }

    private File resolveFile(String filename) throws IOException {
        File file = new File(baseDirectory, filename)
                .getAbsoluteFile()
                .toPath()
                .normalize()
                .toFile();

        if (!file.toPath().startsWith(baseDirectory.toPath().normalize())) {
            throw new IOException("Invalid filename: " + filename);
        }

        return file;
    }
}