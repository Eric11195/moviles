package com.example.engine;

import java.io.IOException;

public interface IFileStorage {
    String getCurrentPath();
    void setRelativePath(String relativePath);
    void write(String filename, String content) throws IOException;
    String read(String filename) throws IOException;
    interface ForEachFileOnDirectoryFunc{
        void func(String s);
    }

    /**
     * @param f the parameter this function receives is the name of the file
     * @return false on error
     */
    boolean forEachFileOnDirectory(ForEachFileOnDirectoryFunc f);
}
