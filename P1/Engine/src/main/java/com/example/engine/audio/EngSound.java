package com.example.engine.audio;

public interface EngSound {
  public String getAudioPath();
  public int getId();

  public int Loops();

  public void setStreamId(int n);
}
