package com.example.game.testing.utils;

import com.example.engine.Engine;
import com.example.game.testing.EC.mcd_Entity;
import com.example.game.testing.Scenes.EndGameScene;

public class StartPlayerDead {
    static boolean infinite=false;
    public static boolean getInfinite(){
        return infinite;
    }
    public static void setInfinite(boolean isInfinite){
        infinite = isInfinite;
    }
    public static void startPlayerDead(Engine eng, mcd_Entity killedBy){
        eng.setScene(new EndGameScene(eng, killedBy.getMyScene(), false, infinite));
    }
}
