package com.example.game.testing.utils;

import com.example.engine.audio.EngSound;
import com.example.game.testing.Scenes.mcd_Scene;

public class SoundLibrary {

    static private EngSound[] sounds = new EngSound[SoundsId.COUNT.ordinal()];
    public SoundLibrary()
    {
    //sounds[SoundsId.PLAYNG] =
    }
    public static void createSounds(mcd_Scene scene)
    {
        sounds[SoundsId.PLAYING.ordinal()] = scene.getEngine().getAudio().addSound("gameloop.wav",-1);
        sounds[SoundsId.BUTTON.ordinal()] = scene.getEngine().getAudio().addSound("button.wav",0);
        sounds[SoundsId.LOSE.ordinal()] = scene.getEngine().getAudio().addSound("gameover.wav",0);
        sounds[SoundsId.WIN.ordinal()] = scene.getEngine().getAudio().addSound("win.wav",0);
        sounds[SoundsId.COIN.ordinal()] = scene.getEngine().getAudio().addSound("coin.wav",0);
        sounds[SoundsId.WARNING.ordinal()] = scene.getEngine().getAudio().addSound("warning.wav",0);
    }
    public static  EngSound getSound(SoundsId id)
    {
        return sounds[id.ordinal()];
    }
    public enum SoundsId
    {
        MAINMENU,
        PLAYING,
        WARNING,
        COIN,
        LOSE,
        WIN,
        BUTTON,
        COUNT
    }

}
