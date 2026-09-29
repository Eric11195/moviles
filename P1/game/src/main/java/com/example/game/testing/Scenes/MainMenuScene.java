package com.example.game.testing.Scenes;

import com.example.engine.Engine;
import com.example.game.testing.EC.Prefabs.ButtonPrefab;
import com.example.game.testing.EC.mcd_Entity;
import com.example.utils.Vec2;

public class MainMenuScene extends mcd_Scene{
    @Override
    public void start(Engine eng) {
        //Start menu
        ButtonPrefab.createButton(
            this,
            new Vec2(100,100),
            "Start",
            ()->{
                  eng.setScene(new MainGameScene());
            }
        );
    }
}
