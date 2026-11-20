package com.example.engine.audio;

/**
 * Platform-independent representation of a loaded sound asset.
 */
public interface EngSound {

    /**
     * Retrieves the relative asset path of the audio file.
     *
     * @return audio file path string
     */
    public String getAudioPath();

    /**
     * Retrieves the internal sound ID.
     *
     * @return sound ID
     */
    public int getId();

    /**
     * Retrieves the number of loops configured for this sound.
     *
     * @return loop count (-1 for continuous loop, 0 for play once)
     */
    public int Loops();

    /**
     * Sets active stream ID for playing sound.
     *
     * @param n active stream ID
     */
    public void setStreamId(int n);

    /**
     * Checks if the sound file is classified as a large audio file (music track).
     *
     * @return true if large file, false otherwise
     */
    public boolean isBigFile();
}
