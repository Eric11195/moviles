package com.example.desktopengine.audio;
import com.example.engine.audio.EngSound;

import javax.sound.sampled.SourceDataLine;

public class SoundDesktop implements  EngSound {

    String audioPath;
    int id;

    int numLoops;

    boolean isBigFile;

    SourceDataLine line;
    @Override
    public String getAudioPath(){return audioPath;}
    @Override
    public int getId(){return id;}

    @Override
    public void setStreamId(int n) { }

    public int Loops()
    {
        return numLoops;
    }
    public SoundDesktop()
    {
        id=-1;
        audioPath="";
        numLoops = -1;
    }
    public SoundDesktop(String path, int n, int loops)
    {
        id=n;
        audioPath=path;
        numLoops = loops;
    }
    public void setName(int n)
    {
        id =n;
    }
    public void setFilePath(String s)
    {
        audioPath =s;
    }

    public void setLoops(int n)
    {
        numLoops = n;
    }

    public void setLine(SourceDataLine l){line = l;}

    @Override
    public boolean isBigFile() {
        return isBigFile;
    }
}
