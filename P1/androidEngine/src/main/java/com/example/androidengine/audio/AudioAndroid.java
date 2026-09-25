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
        this.sound_pool = new SoundPool(12, AudioManager.STREAM_MUSIC, 0);

        this.sound_pool.setOnLoadCompleteListener((soundPool, sampleId, status) -> {
            if (status == 0) {
                this.sound_pool.play(sampleId, 1.f, 1.f, 0, 0, 1.f);
                Log.d("AudioAndroid", "Sonido cargado: " + sampleId);
            } else {
                Log.e("AudioAndroid", "Error cargando sonido: " + sampleId);
            }
        });
    }

    public AudioAndroid() {
        this.context = null;
        this.sound_pool = new SoundPool(12, AudioManager.STREAM_MUSIC, 0);
    }

    @Override
    public SoundAndroid addSound(String file_path) {
        int aux = -1;
        if (context != null) {
            try {
                AssetFileDescriptor afd = null;
                // Try 1: Exact path as passed (e.g. "Mineral sparkle.mp3")
                try {
                    afd = context.getAssets().openFd(file_path);
                    Log.d("AudioAndroid", "Found sound at root: " + file_path);
                } catch (Exception e1) {
                    // Try 2: Prepend "audio/" if not already present
                    try {
                        afd = context.getAssets().openFd("audio/" + file_path);
                        Log.d("AudioAndroid", "Found sound in audio/: " + file_path);
                    } catch (Exception e2) {
                        Log.e("AudioAndroid", "Sound file not found in assets: " + file_path);
                    }
                }

                if (afd != null) {
                    aux = sound_pool.load(afd, 1);
                    afd.close();
                }
            } catch (Exception e) {
                Log.e("AudioAndroid", "Error loading sound: " + file_path, e);
            }
        } else {
            // Fallback if context is not available
            try {
                aux = sound_pool.load(file_path, 1);
            } catch (Exception e) {
                Log.e("AudioAndroid", "Error loading sound without context: " + file_path, e);
            }
        }
        return new SoundAndroid(aux, file_path);
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
    public void playSound(EngSound sound, int looping) {
        if (sound != null && sound.getId() > 0) {
            sound_pool.play(sound.getId(), 1.f, 1.f, 0, looping, 1.f);
        }
    }
}
