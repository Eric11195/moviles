package com.example.game.testing.Scenes;

import static com.example.game.testing.EC.Prefabs.ButtonPrefab.createButton;
import static com.example.game.testing.constants.Constants.DEFAULT_BUTTON_SIZE;
import static com.example.game.testing.constants.Constants.LOGIC_WINDOW_SIZE;

import com.example.engine.Engine;
import com.example.engine.GraphicsInterface;
import com.example.engine.Scene;
import com.example.utils.Vec2;

public class PauseMenu extends mcd_Scene{
    Scene subScene;
    public PauseMenu(Engine myEngine, Scene scene){
        super(myEngine);
        subScene = scene;
    }
    @Override
    public void start(Engine eng) {
        Vec2 pos = new Vec2(LOGIC_WINDOW_SIZE.x/2.0f-DEFAULT_BUTTON_SIZE.x/2.0f,LOGIC_WINDOW_SIZE.y/3.0f-DEFAULT_BUTTON_SIZE.y/2.0f);
        //Spawn resume button
        createButton(this, pos, DEFAULT_BUTTON_SIZE, "RESUME", ()->{eng.setScene(subScene);});
        //Spawn exit button
        Vec2 secondButtonPos = pos.clone();
        secondButtonPos.y*=2;
        createButton(this, secondButtonPos, DEFAULT_BUTTON_SIZE, "EXIT", ()->{eng.setScene(new MainMenuScene(eng));});
    }
    @Override
    public void render(GraphicsInterface g){
        subScene.render(g);
        super.render(g);
    }

}
