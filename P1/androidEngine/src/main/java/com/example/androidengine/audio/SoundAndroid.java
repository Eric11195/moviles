package com.example.androidengine.audio;

import com.example.engine.audio.EngSound;
public class SoundAndroid implements EngSound {

    private int id;

    private String file_path;

    public SoundAndroid(int n,String path)
    {
        id=n;
        file_path=path;
    }
    public SoundAndroid()
    {
        id =-1;
        file_path="";
    }
    @Override
    public String getAudioPath() {
        return file_path;
    }
    @Override
    public int getId() {
        return id;
    }
    public void setName(int n)
    {
        id =n;
    }
    public void setFilePath(String s)
    {
        file_path =s;
    }
}
