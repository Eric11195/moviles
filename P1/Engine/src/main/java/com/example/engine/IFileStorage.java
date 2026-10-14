package com.example.engine;

import java.io.IOException;

public interface IFileStorage {
    String getCurrentPath();
    void setRelativePath(String relativePath);
    void write(String filename, String content) throws IOException;
    String read(String filename) throws IOException;
}
