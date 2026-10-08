package com.example.game.testing.Scenes;

import com.example.engine.Engine;
import com.example.game.testing.EC.Prefabs.LaserPrefab;
import com.example.game.testing.EC.Prefabs.PlayerPrefab;
import com.example.game.testing.Systems.SceneGenerator;
import com.example.utils.Vec2;

public class MainGameScene extends mcd_Scene{
    @Override
    public void start(Engine eng) {
        System.out.println("Quiero llorar Toni");
        addSystem(new SceneGenerator(this, 2));
        PlayerPrefab.createPlayer(this,new Vec2(100,100));
        LaserPrefab.createLaser(this,new Vec2(0,0));
    }

    @Override
    public void update(Engine eng,float t, float dt){
        super.update(eng, t, dt);
    }
}
