package com.example.desktopengine.audio;
import com.example.engine.audio.AudioInterface;
import javax.sound.sampled.Clip;


public class AudioPool {
    static final int MAX_SOUNDS = 40;
    public Clip[] clips;
    int clips_used;
    public AudioPool()
    {
        clips = new Clip[MAX_SOUNDS];
        clips_used = 0;
    }

    public int addSound(Clip clip)
    {
        if(clips_used < MAX_SOUNDS)
        {
            clips[clips_used] = clip;
            clips_used++;
            return clips_used-1;
        }
        for(int i =0;i<MAX_SOUNDS;i++)
        {
            if(!clips[i].isRunning())
            {
                clips[i] = clip;
                return i;
            }
        }
        return -1;
    }
}
