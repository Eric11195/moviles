package com.example.game.testing.utils;

import com.example.engine.Engine;
import com.example.game.testing.EC.mcd_Entity;
import com.example.game.testing.Scenes.EndGameScene;

public class StartPlayerDead {
    public static void startPlayerDead(Engine eng, mcd_Entity killedBy){
        eng.setScene(new EndGameScene(eng, killedBy.getMyScene(), false));
    }
}
