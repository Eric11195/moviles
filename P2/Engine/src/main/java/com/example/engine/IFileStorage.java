package com.example.engine;

import java.io.IOException;

/**
 * Platform-independent file storage abstraction interface.
 */
public interface IFileStorage {

    /**
     * Retrieves the current base relative directory path.
     *
     * @return relative path string
     */
    String getCurrentPath();

    /**
     * Sets the base relative directory path for file operations.
     *
     * @param relativePath directory path relative to asset/storage root
     */
    void setRelativePath(String relativePath);

    /**
     * Writes text content to a modifiable file.
     *
     * @param filename target file name
     * @param content text string content to write
     * @throws IOException if writing fails
     */
    void writeModifiable(String filename, String content) throws IOException;

    /**
     * Reads text content from a read-only asset file.
     *
     * @param filename asset file name
     * @return content string
     * @throws IOException if reading fails
     */
    String read(String filename) throws IOException;

    /**
     * Reads text content from a modifiable file.
     *
     * @param filename file name
     * @return content string or null if file does not exist
     * @throws IOException if reading fails
     */
    String readModifiable(String filename) throws IOException;

    /**
     * Functional callback interface for iterating over directory files.
     */
    interface ForEachFileOnDirectoryFunc {
        /**
         * Invoked for each file found in the directory.
         *
         * @param s file name
         */
        void func(String s);
    }

    /**
     * Iterates over all entries in the current relative directory.
     *
     * @param f callback function invoked for each file name
     * @return true on success, false on error
     */
    boolean forEachFileOnDirectory(ForEachFileOnDirectoryFunc f);
}
