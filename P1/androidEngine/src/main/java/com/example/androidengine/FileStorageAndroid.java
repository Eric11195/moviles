package com.example.androidengine;

import android.content.Context;

import com.example.engine.IFileStorage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;

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
    private String getAssetPath(String filename) throws IOException {
        File file = resolveFile(filename);

        String relativePath = context.getFilesDir().toPath()
                .relativize(file.toPath())
                .toString();

        return relativePath.replace(File.separatorChar, '/');
    }
    @Override
    public String read(String filename) throws IOException {
        File file = resolveFile(filename);

        try (InputStream input = context.getAssets().open(
                getAssetPath(filename)
        )) {
            ByteArrayOutputStream output = new ByteArrayOutputStream();

            byte[] buffer = new byte[8192];
            int count;

            while ((count = input.read(buffer)) != -1) {
                output.write(buffer, 0, count);
            }

            return output.toString("UTF-8");
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