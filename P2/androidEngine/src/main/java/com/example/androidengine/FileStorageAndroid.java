package com.example.androidengine;

import android.content.Context;
import android.util.Log;

import com.example.engine.IFileStorage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileStorageAndroid implements IFileStorage {

    private static final String TAG = "FileStorageAndroid";
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

        Path root = Paths.get("").normalize();
        Path resolved = root.resolve(relativePath).normalize();

        if (resolved.isAbsolute() || resolved.startsWith("..")) {
            throw new IllegalArgumentException("Invalid relative path: " + relativePath);
        }

        baseDirectory = resolved.toString().replace("\\", "/");
    }

    @Override
    public void writeModifiable(String filename, String content) throws IOException {
        File file = resolveFile(filename);

        Log.d(TAG, "[WRITE] Writing file to absolute device path: " + file.getAbsolutePath());

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

        Log.d(TAG, "[READ] Reading APK asset: " + path);

        try (InputStream input = context.getAssets().open(path)) {
            return readFromStream(input);
        } catch (IOException e) {
            Log.e(TAG, "[READ FAILED] Asset not found inside APK at path: assets/" + path, e);
            throw e;
        }
    }

    @Override
    public String readModifiable(String filename) throws IOException {
        File internalFile = resolveFile(filename);

        if (!internalFile.exists() || !internalFile.isFile()) {
            Log.d(TAG, "[READ] Modifiable file not found: " + internalFile.getAbsolutePath());
            return null;
        }

        Log.d(TAG, "[READ] Reading internal file: " + internalFile.getAbsolutePath());

        try (InputStream input = new FileInputStream(internalFile)) {
            return readFromStream(input);
        } catch (IOException e) {
            Log.w(TAG, "[READ FAILED] Could not open modifiable file: " + internalFile.getAbsolutePath(), e);
            return null;
        }
    }

    @Override
    public boolean forEachFileOnDirectory(ForEachFileOnDirectoryFunc f) {
        try {
            String path = resolveAssetPath("");
            String[] entries = context.getAssets().list(path);
            if (entries == null) {
                return false;
            }

            for (String fileName : entries) {
                if (!fileName.startsWith(".")) {
                    f.func(fileName);
                }
            }
            return true;
        } catch (IOException ex) {
            return false;
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