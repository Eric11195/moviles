package com.example.game.testing.Systems;

import com.example.engine.Engine;
import com.example.engine.GraphicsInterface;
import com.example.game.testing.Scenes.EndGameScene;
import com.example.game.testing.Scenes.mcd_Scene;

public class EndGameAfterXDistance implements mcd_System{
    float maxDistance;
    boolean win;
    public EndGameAfterXDistance(float maxDistance, boolean win){
        this.maxDistance = maxDistance;
        this.win = win;
    }

    @Override
    public void update(mcd_Scene scene, float t, float dt) {
        if(DistanceSystem.getInstance().getDistance() >= maxDistance){
            Engine eng = scene.getEngine();
            eng.setScene(new EndGameScene(eng, this.win));
        }
    }

    @Override
    public void render(GraphicsInterface g) {

    }
}
