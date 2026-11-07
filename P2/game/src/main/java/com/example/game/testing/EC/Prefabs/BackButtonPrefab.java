package com.example.game.testing.EC.Prefabs;

import static com.example.game.testing.EC.Prefabs.ButtonPrefab.createButton;
import static com.example.game.testing.constants.Constants.BACK_BUTTON_SQUARE_SIZE;
import static com.example.game.testing.constants.Constants.LOGIC_WINDOW_SIZE;

import com.example.engine.Engine;
import com.example.game.testing.Scenes.MainMenuScene;
import com.example.game.testing.Scenes.mcd_Scene;
import com.example.utils.Vec2;

public class BackButtonPrefab {
    public static void createBackButton(Engine eng, mcd_Scene scene){
        createButton(
                scene,
                new Vec2(0,LOGIC_WINDOW_SIZE.y-BACK_BUTTON_SQUARE_SIZE),
                new Vec2(BACK_BUTTON_SQUARE_SIZE,BACK_BUTTON_SQUARE_SIZE),
                "Back",
                () ->{
                    eng.setScene(new MainMenuScene(eng));
                }
        );
    }
}
