package com.example.androidengine.audio;

import com.example.engine.audio.AudioInterface;
import com.example.engine.audio.EngSound;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

public class AudioAndroid  implements  AudioInterface {

    MediaPlayer media_player;
    SoundPool sound_pool;
    public AudioAndroid(String bgMusicPath)
    {
        bgMusicPath=bgMusicPath;
        sound_pool = new SoundPool(12, AudioManager.STREAM_MUSIC,0);

    }
    @Override
    public SoundAndroid addSound(String file_path) {

        int aux=sound_pool.load(file_path,1);
        return new SoundAndroid(aux,file_path);
    }
    public void setBackgroundMusic(String file_path)
    {
        //add the bg music to the mediaplayer
    }
    @Override
    public void playSound(EngSound sound, Boolean looping) {
        int aux =0;
        if(looping) {
            aux =1;
        }
        sound_pool.play(sound.getId(),1.f,1.f,0,aux,1);
    }
}
