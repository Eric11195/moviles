package com.example.androidengine.audio;

import com.example.engine.audio.AudioInterface;
import com.example.engine.audio.EngSound;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

public class AudioAndroid implements AudioInterface {

    private Context context;
    private SoundPool sound_pool;

    public AudioAndroid(Context context) {
        this.context = context;
        this.sound_pool = new SoundPool(40, AudioManager.STREAM_MUSIC, 0);
    }

    public AudioAndroid() {
        this.context = null;
        this.sound_pool = new SoundPool(12, AudioManager.STREAM_MUSIC, 0);
    }

    @Override
    public SoundAndroid addSound(String file_path, int numLoops) {
        int aux = -1;

                AssetFileDescriptor afd = null;
                try {
                    String pathAux = "audio/" + file_path;
                    afd = context.getAssets().openFd(pathAux);
                    // Try 1: Exact path as passed (e.g. "Mineral sparkle.mp3")
                    aux = sound_pool.load(afd, 1);
                    System.out.println(aux);
                    afd.close();

                }catch (Exception e)
                {
                    Log.e("SOUND", "Error al abrir el archivo: " + file_path, e);
                }

        return new SoundAndroid(aux, file_path,numLoops);
    }

    @Override
    public void stopSound(EngSound sound) {
        SoundAndroid soundAndroid = (SoundAndroid) sound;
        sound_pool.stop(soundAndroid.streamId);
    }

    public void setBackgroundMusic(String file_path) {
        //if (media_player != null) {
        //   media_player.stop();
        //   media_player.release();
        // }

        //media_player = new MediaPlayer(this, file_path);
        //media_player.start();
    }

    @Override
    public int playSound(EngSound sound) {
        if (sound != null && sound.getId() > 0) {
            return sound_pool.play(sound.getId(), 1.f, 1.f, 0,sound.Loops(), 1.f);
        }
        return  -1;
    }
}
