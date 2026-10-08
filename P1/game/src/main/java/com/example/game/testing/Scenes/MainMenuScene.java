package com.example.game.testing.Scenes;

import com.example.engine.Engine;
import com.example.game.testing.EC.Component.ColliderComponent;
import com.example.game.testing.EC.ComponentId;
import com.example.game.testing.EC.Prefabs.ButtonPrefab;
import com.example.game.testing.EC.Prefabs.PlayerPrefab;
import com.example.game.testing.EC.mcd_Entity;
import com.example.game.testing.Systems.SceneGenerator;
import com.example.utils.Vec2;

public class MainMenuScene extends mcd_Scene{
    public MainMenuScene(){
        super();
    }
    @Override
    public void start(Engine eng) {
        //Start menu
        var button = ButtonPrefab.createButton(
            this,
            new Vec2(100,100),
            "Start",
            ()->{
                  eng.setScene(new MainGameScene());
            }
        );
        PlayerPrefab.createPlayer(this,new Vec2(100,100));
//        button.addComponent(ComponentId.COLLIDER_COMPONENT,new ColliderComponent(30,75,5,(float)Math.PI/4,new Vec2(100,100)));
//        ((ColliderComponent)button.getComponent(ComponentId.COLLIDER_COMPONENT)).rotate((float)Math.PI/4);
    }
}
