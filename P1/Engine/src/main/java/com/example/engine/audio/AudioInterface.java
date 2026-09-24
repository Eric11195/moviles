package com.example.engine.audio;


public interface AudioInterface {
    public EngSound addSound(String file_path);

    public void playSound(EngSound sound, Boolean looping);

}
