package com.example.androidengine.audio;

import com.example.engine.audio.AudioInterface;
import com.example.engine.audio.EngSound;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.Log;

import java.io.IOException;

public class AudioAndroid implements AudioInterface {
    static private final int MAX_BIG_SOUNDS = 10;
    private Context context;
    private SoundPool sound_pool;

    private MediaPlayer[] mediaPlayers;

    private int mediaPlayersIndex;

    public AudioAndroid(Context context) {
        this.context = context;
        this.sound_pool = new SoundPool(40, AudioManager.STREAM_MUSIC, 0);
        this.mediaPlayers = new MediaPlayer[MAX_BIG_SOUNDS];
        for (int i = 0; i < MAX_BIG_SOUNDS; i++) {
            this.mediaPlayers[i] = new MediaPlayer();
        }
        this.mediaPlayersIndex =0;
    }

    public AudioAndroid() {
        this.context = null;
        this.sound_pool = new SoundPool(12, AudioManager.STREAM_MUSIC, 0);
    }
    private int addBigAudio(AssetFileDescriptor afd,int numLoops)
    {
        if(mediaPlayersIndex<MAX_BIG_SOUNDS)
        {
            try {
                mediaPlayers[mediaPlayersIndex].setDataSource(afd.getFileDescriptor(),
                        afd.getStartOffset(),
                        afd.getLength() );
                mediaPlayers[mediaPlayersIndex].setLooping(numLoops==-1);
                mediaPlayers[mediaPlayersIndex].prepare();
                mediaPlayersIndex++;
                return mediaPlayersIndex-1;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return -1;



    }
    @Override
    public SoundAndroid addSound(String file_path, int numLoops) {
        int aux = -1;
        SoundAndroid soundAndroid = new SoundAndroid(aux,file_path,numLoops);

                AssetFileDescriptor afd = null;
                try {
                    String pathAux = "audio/" + file_path;
                    afd = context.getAssets().openFd(pathAux);
                    // Try 1: Exact path as passed (e.g. "Mineral sparkle.mp3")
                    if(afd.getLength()>1024*1024)
                    {
                        soundAndroid.isBigFile = true;
                        aux =addBigAudio(afd,numLoops);

                    }
                    else{
                    aux = sound_pool.load(afd, 1);
                    }
                    System.out.println(aux);
                    afd.close();

                }catch (Exception e)
                {
                    Log.e("SOUND", "Error al abrir el archivo: " + file_path, e);
                }
            soundAndroid.setId(aux);
        return soundAndroid ;
    }

    @Override
    public void stopSound(EngSound sound) {
        if(sound.isBigFile())
        {
            mediaPlayers[sound.getId()].stop();
        }
        else{
        SoundAndroid soundAndroid = (SoundAndroid) sound;
        sound_pool.stop(soundAndroid.streamId);
        }
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
        if(sound!=null&&sound.getId()>=0) {
            SoundAndroid sound_android = (SoundAndroid) sound;
            if (sound_android.isBigFile && sound.getId() >= 0) {
                mediaPlayers[sound.getId()].seekTo(0);
                mediaPlayers[sound.getId()].start();
                return sound.getId();
            } else {
                return sound_pool.play(sound.getId(), 1.f, 1.f, 0, sound.Loops(), 1.f);
            }

        }
        return  -1;
    }
}
