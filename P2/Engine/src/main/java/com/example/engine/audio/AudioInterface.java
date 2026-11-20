package com.example.engine.audio;

/**
 * Platform-independent audio subsystem interface for loading, playing, and stopping sounds.
 */
public interface AudioInterface {

    /**
     * Loads an audio file asset and creates an EngSound instance.
     *
     * @param file_path relative path to audio file asset
     * @param loops number of loop repetitions (-1 for continuous loop, 0 for play once)
     * @return created EngSound instance
     */
    public EngSound addSound(String file_path, int loops);

    /**
     * Plays a sound asset.
     *
     * @param sound EngSound instance to play
     * @return stream ID or status code (-1 on failure)
     */
    public int playSound(EngSound sound);

    /**
     * Stops playback of a sound asset.
     *
     * @param sound EngSound instance to stop
     */
    public void stopSound(EngSound sound);
}
