package com.example.engine.audio;


public interface AudioInterface {
    public EngSound addSound(String file_path, int loops);

    public void playSound(EngSound sound);

    public void stopSound(EngSound sound);

}
