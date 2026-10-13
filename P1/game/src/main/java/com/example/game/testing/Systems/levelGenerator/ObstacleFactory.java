package com.example.game.testing.Systems.levelGenerator;

import com.example.game.testing.EC.mcd_Entity;
import com.example.game.testing.Scenes.mcd_Scene;

public interface ObstacleFactory {
    public void instantiate(mcd_Scene scene, mcd_Entity ent, float levelSpeed, float t);
}
