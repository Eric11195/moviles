package com.example.game.testing.Scenes;

import com.example.engine.Engine;
import com.example.game.testing.Systems.SceneGenerator;

public class MainGameScene extends mcd_Scene{
    @Override
    public void start(Engine eng) {
        System.out.println("Quiero llorar Toni");
        addSystem(new SceneGenerator(this, 2));
    }

    @Override
    public void update(Engine eng,float t, float dt){
        super.update(eng, t, dt);
    }
}
