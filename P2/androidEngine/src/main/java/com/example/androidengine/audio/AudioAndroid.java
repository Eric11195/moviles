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

/**
 * Android implementation of {@link AudioInterface} using {@link SoundPool} and {@link MediaPlayer}.
 */
public class AudioAndroid implements AudioInterface {

    /** Maximum number of concurrent MediaPlayer instances for large audio files. */
    static private final int MAX_BIG_SOUNDS = 10;

    /** Android Context. */
    private Context context;

    /** SoundPool instance for short sound effects. */
    private SoundPool sound_pool;

    /** Array of MediaPlayer instances for music tracks. */
    private MediaPlayer[] mediaPlayers;

    /** Current allocation index in mediaPlayers array. */
    private int mediaPlayersIndex;

    /**
     * Constructs AudioAndroid initializing SoundPool and MediaPlayer pool.
     *
     * @param context Android Context
     */
    public AudioAndroid(Context context) {
        this.context = context;
        this.sound_pool = new SoundPool(40, AudioManager.STREAM_MUSIC, 0);
        this.mediaPlayers = new MediaPlayer[MAX_BIG_SOUNDS];
        for (int i = 0; i < MAX_BIG_SOUNDS; i++) {
            this.mediaPlayers[i] = new MediaPlayer();
        }
        this.mediaPlayersIndex = 0;
    }

    /**
     * Default constructor creating uninitialized AudioAndroid.
     */
    public AudioAndroid() {
        this.context = null;
        this.sound_pool = new SoundPool(12, AudioManager.STREAM_MUSIC, 0);
    }

    /**
     * Configures a MediaPlayer instance for a large audio file (music track).
     *
     * @param afd AssetFileDescriptor of audio file
     * @param numLoops loop count (-1 for continuous)
     * @return index in mediaPlayers array or -1 on failure
     */
    private int addBigAudio(AssetFileDescriptor afd, int numLoops) {
        if (mediaPlayersIndex < MAX_BIG_SOUNDS) {
            try {
                mediaPlayers[mediaPlayersIndex].setDataSource(
                        afd.getFileDescriptor(),
                        afd.getStartOffset(),
                        afd.getLength()
                );
                mediaPlayers[mediaPlayersIndex].setLooping(numLoops == -1);
                mediaPlayers[mediaPlayersIndex].prepare();
                mediaPlayersIndex++;
                return mediaPlayersIndex - 1;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return -1;
    }

    /**
     * Loads an audio file asset from assets/audio/ into SoundPool or MediaPlayer.
     *
     * @param file_path relative path inside assets/audio/
     * @param numLoops loop count (-1 for continuous loop)
     * @return loaded SoundAndroid instance
     */
    @Override
    public SoundAndroid addSound(String file_path, int numLoops) {
        int aux = -1;
        SoundAndroid soundAndroid = new SoundAndroid(aux, file_path, numLoops);

        AssetFileDescriptor afd = null;
        try {
            String pathAux = "audio/" + file_path;
            afd = context.getAssets().openFd(pathAux);
            if (afd.getLength() > 1024 * 1024) {
                soundAndroid.isBigFile = true;
                aux = addBigAudio(afd, numLoops);
            } else {
                aux = sound_pool.load(afd, 1);
            }
            System.out.println(aux);
            afd.close();

        } catch (Exception e) {
            Log.e("SOUND", "Error al abrir el archivo: " + file_path, e);
        }
        soundAndroid.setId(aux);
        return soundAndroid;
    }

    /**
     * Stops playback of an active sound or music track.
     *
     * @param sound EngSound instance to stop
     */
    @Override
    public void stopSound(EngSound sound) {
        if (sound.isBigFile()) {
            mediaPlayers[sound.getId()].stop();
        } else {
            SoundAndroid soundAndroid = (SoundAndroid) sound;
            sound_pool.stop(soundAndroid.streamId);
        }
    }

    /**
     * Sets background music file path.
     *
     * @param file_path file path string
     */
    public void setBackgroundMusic(String file_path) {
    }

    /**
     * Plays a sound or music track.
     *
     * @param sound EngSound instance to play
     * @return stream ID or player index (-1 on failure)
     */
    @Override
    public int playSound(EngSound sound) {
        if (sound != null && sound.getId() >= 0) {
            SoundAndroid sound_android = (SoundAndroid) sound;
            if (sound_android.isBigFile && sound.getId() >= 0) {
                mediaPlayers[sound.getId()].seekTo(0);
                mediaPlayers[sound.getId()].start();
                return sound.getId();
            } else {
                return sound_pool.play(sound.getId(), 1.f, 1.f, 0, sound.Loops(), 1.f);
            }
        }
        return -1;
    }
}
