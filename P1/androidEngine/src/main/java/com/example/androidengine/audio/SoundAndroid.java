package com.example.androidengine.audio;

import com.example.engine.audio.EngSound;
public class SoundAndroid implements EngSound {

    private int id;

    private String file_path;

    int numLoops;

    int streamId = -1;
    boolean isBigFile = false;
    int streamid;
    public SoundAndroid(int n,String path,int loops)
    {
        id=n;
        file_path=path;
        numLoops=loops;
    }
    public void setId(int n)
    {id = n;}
    public SoundAndroid()
    {
        id =-1;
        file_path="";
    }
    public void setStreamId(int n){streamId = n;}
    @Override
    public String getAudioPath() {
        return file_path;
    }
    @Override
    public int getId() {
        return id;
    }
    @Override
    public boolean isBigFile()
    {
        return isBigFile;
    }
    @Override
    public int Loops()
    {
        return numLoops;
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
