package com.example.androidengine.audio;

import com.example.engine.audio.EngSound;

/**
 * Android implementation of {@link EngSound}.
 */
public class SoundAndroid implements EngSound {

    /** Internal sound ID or MediaPlayer index. */
    private int id;

    /** Relative file path to audio asset. */
    private String file_path;

    /** Number of loops (-1 for continuous). */
    int numLoops;

    /** Stream ID for active SoundPool playback. */
    int streamId = -1;

    /** Flag indicating if sound is a large audio file. */
    boolean isBigFile = false;

    /** Stream ID variable. */
    int streamid;

    /**
     * Constructs SoundAndroid with ID, path, and loop count.
     *
     * @param n internal sound ID
     * @param path audio file path
     * @param loops loop count
     */
    public SoundAndroid(int n, String path, int loops) {
        id = n;
        file_path = path;
        numLoops = loops;
    }

    /**
     * Sets internal sound ID.
     *
     * @param n sound ID
     */
    public void setId(int n) {
        id = n;
    }

    /**
     * Default constructor creating uninitialized SoundAndroid.
     */
    public SoundAndroid() {
        id = -1;
        file_path = "";
    }

    /**
     * Sets active stream ID for playback.
     *
     * @param n active stream ID
     */
    @Override
    public void setStreamId(int n) {
        streamId = n;
    }

    /**
     * Retrieves audio file path.
     *
     * @return audio file path string
     */
    @Override
    public String getAudioPath() {
        return file_path;
    }

    /**
     * Retrieves internal sound ID.
     *
     * @return sound ID
     */
    @Override
    public int getId() {
        return id;
    }

    /**
     * Checks if audio file is classified as a large file.
     *
     * @return true if large file, false otherwise
     */
    @Override
    public boolean isBigFile() {
        return isBigFile;
    }

    /**
     * Retrieves loop count.
     *
     * @return loop count
     */
    @Override
    public int Loops() {
        return numLoops;
    }

    /**
     * Sets internal sound ID.
     *
     * @param n sound ID
     */
    public void setName(int n) {
        id = n;
    }

    /**
     * Sets audio file path.
     *
     * @param s audio file path string
     */
    public void setFilePath(String s) {
        file_path = s;
    }
}
