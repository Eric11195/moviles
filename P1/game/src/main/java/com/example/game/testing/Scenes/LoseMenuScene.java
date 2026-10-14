package com.example.game.testing.Scenes;

import static com.example.game.testing.EC.Prefabs.ButtonPrefab.createButton;
import static com.example.game.testing.constants.Constants.DEFAULT_BUTTON_SIZE;
import static com.example.game.testing.constants.Constants.LOGIC_WINDOW_SIZE;

import com.example.engine.Engine;

public class LoseMenuScene extends mcd_Scene{
    public LoseMenuScene(Engine myEngine) {
        super(myEngine);
    }

    @Override
    public void start(Engine eng) {
        createButton(
            this,
            LOGIC_WINDOW_SIZE.mul(0.5f).add(DEFAULT_BUTTON_SIZE.mul(-0.5f)),
            DEFAULT_BUTTON_SIZE,
            "Return to Main Menu",
            () ->{
                eng.setScene(new MainMenuScene(eng));
            });
    }
}
