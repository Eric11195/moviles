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

/**
 * Android implementation of {@link IFileStorage} accessing internal app storage and APK assets.
 */
public class FileStorageAndroid implements IFileStorage {

    /** Logcat tag string. */
    private static final String TAG = "FileStorageAndroid";

    /** Application Context. */
    private final Context context;

    /** Current base relative directory path. */
    private String baseDirectory = "";

    /**
     * Constructs FileStorageAndroid with Application context.
     *
     * @param context Application context
     */
    public FileStorageAndroid(Context context) {
        this.context = context.getApplicationContext();
    }

    /**
     * Retrieves current relative base directory path.
     *
     * @return relative directory path
     */
    @Override
    public String getCurrentPath() {
        return baseDirectory;
    }

    /**
     * Sets current relative base directory path.
     *
     * @param relativePath relative directory path
     */
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

    /**
     * Writes content string to internal app storage.
     *
     * @param filename file name
     * @param content text content string
     * @throws IOException if writing fails
     */
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

    /**
     * Reads text content from APK assets directory.
     *
     * @param filename asset file name
     * @return file content string
     * @throws IOException if asset file is missing or unreadable
     */
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

    /**
     * Reads text content from internal app storage.
     *
     * @param filename file name
     * @return content string or null if file does not exist
     * @throws IOException if reading fails
     */
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

    /**
     * Iterates over files in assets directory.
     *
     * @param f callback function
     * @return true on success, false otherwise
     */
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

    /**
     * Reads entire InputStream into a String.
     *
     * @param input InputStream
     * @return content string
     * @throws IOException if read fails
     */
    private String readFromStream(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[8192];
        int count;
        while ((count = input.read(buffer)) != -1) {
            output.write(buffer, 0, count);
        }
        return output.toString(StandardCharsets.UTF_8.name());
    }

    /**
     * Resolves file path relative to internal app storage.
     *
     * @param filename file name
     * @return File instance
     * @throws IOException if path escapes base directory
     */
    private File resolveFile(String filename) throws IOException {
        File base = new File(context.getFilesDir(), baseDirectory);

        Path basePath = base.getAbsoluteFile().toPath().normalize();
        Path filePath = basePath.resolve(filename).normalize();

        if (!filePath.startsWith(basePath)) {
            throw new IOException("Invalid filename: " + filename);
        }

        return filePath.toFile();
    }

    /**
     * Resolves asset path string for Android AssetManager.
     *
     * @param filename file name
     * @return asset path string
     * @throws IOException if path escapes base directory
     */
    private String resolveAssetPath(String filename) throws IOException {
        Path basePath = Paths.get(baseDirectory).normalize();
        Path filePath = basePath.resolve(filename).normalize();

        if (filePath.isAbsolute() || !filePath.startsWith(basePath)) {
            throw new IOException("Invalid filename: " + filename);
        }

        return filePath.toString().replace("\\", "/");
    }
}
