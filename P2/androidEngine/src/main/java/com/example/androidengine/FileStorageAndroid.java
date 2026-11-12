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
import java.util.Objects;

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

        baseDirectory = relativePath;
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

        // Si no existe, leer desde assets
        Log.d(TAG, "[READ] Reading APK asset: " + path);

        try (InputStream input = context.getAssets().open(path)) {
            return readFromStream(input);
        }
        catch (IOException e) {
            Log.e(TAG, "[READ FAILED] Asset not found inside APK at path: assets/" + path, e);
            throw e;
        }
        //Log.d(TAG, "[READ] Attempting to open asset path inside APK: assets/" + path)
    }

    @Override
    public String readModifiable(String filename) throws IOException {

        String path = resolveAssetPath(filename);

        File internalFile = new File(context.getFilesDir(), path);

        // Crear el fichero si no existe
        if (!internalFile.exists()) {
            File parent = internalFile.getParentFile();

            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }

            internalFile.createNewFile();
            try (FileOutputStream output = new FileOutputStream(internalFile)) {
                output.write("{}".getBytes(StandardCharsets.UTF_8));
            }
            Log.d(TAG, "[CREATE] Created empty file: " + path);
        }

        Log.d(TAG, "[READ] Reading internal file: " + path);

        try (InputStream input = new FileInputStream(internalFile)) {
            return readFromStream(input);
        }
        //Log.d(TAG, "[READ] Attempting to open asset path inside APK: assets/" + path)
    }

    /**
     * @param f the func to be applied
     * @return false on error (non valid directoy)
     */
    @Override
    public boolean forEachFileOnDirectory(ForEachFileOnDirectoryFunc f) {
        try {
            String path = resolveAssetPath("");
            for(String fileName : Objects.requireNonNull(context.getAssets().list(path))){
                f.func(fileName);
            }
        }catch(IOException ex){
            return false;
        }
        return true;
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