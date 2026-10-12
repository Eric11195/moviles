package com.example.game.testing.Scenes;

import static com.example.game.testing.EC.Prefabs.ButtonPrefab.createButton;
import static com.example.game.testing.constants.Constants.DEFAULT_BUTTON_SIZE;
import static com.example.game.testing.constants.Constants.LOGIC_WINDOW_SIZE;

import com.example.engine.Engine;
import com.example.game.testing.EC.Component.ColliderComponent;
import com.example.game.testing.EC.ComponentId;
import com.example.game.testing.EC.Prefabs.ButtonPrefab;
import com.example.game.testing.EC.Prefabs.PlayerPrefab;
import com.example.game.testing.EC.Prefabs.LaserPrefab;
import com.example.game.testing.EC.mcd_Entity;
import com.example.game.testing.Systems.SceneGenerator;
import com.example.utils.Vec2;

public class MainMenuScene extends mcd_Scene{
    public MainMenuScene(){
        super();
    }
    @Override
    public void start(Engine eng) {
        createButton(
            this,
            LOGIC_WINDOW_SIZE.mul(0.5f).add(DEFAULT_BUTTON_SIZE.mul(-0.5f)),
            DEFAULT_BUTTON_SIZE,
            "START GAME",
            () ->{
                eng.setScene(new MainGameScene());
            }
        );
    }
}
