package com.example.game.testing.Scenes;

import static com.example.game.testing.EC.Prefabs.ButtonPrefab.createButton;
import static com.example.game.testing.constants.Constants.DEFAULT_BUTTON_SIZE;
import static com.example.game.testing.constants.Constants.LOGIC_WINDOW_SIZE;

import com.example.engine.Engine;
import com.example.engine.GraphicsInterface;
import com.example.game.testing.utils.FontLibrary;
import com.example.utils.Vec2;

public class MainMenuScene extends mcd_Scene{
    public MainMenuScene(){
        super();
    }
    @Override
    public void start(Engine eng) {
        float quartY = LOGIC_WINDOW_SIZE.y/4;
        GraphicsInterface g = eng.getGraphics();
        
        FontLibrary.createFont(g, FontLibrary.FontName.STANDARD,16);
        FontLibrary.createFont(g, FontLibrary.FontName.HEADER,72);
        FontLibrary.createFont(g, FontLibrary.FontName.SUBHEADER,32);
        g.setFont(FontLibrary.getFont(FontLibrary.FontName.SUBHEADER));

        createButton(
            this,
            LOGIC_WINDOW_SIZE.mul(0.5f).add(new Vec2(0,-quartY)).add(DEFAULT_BUTTON_SIZE.mul(-0.5f)),
            DEFAULT_BUTTON_SIZE,
            "START GAME",
            () ->{
                eng.setScene(new MainGameScene());
            }
        );
        createButton(
                this,
                LOGIC_WINDOW_SIZE.mul(0.5f).add(new Vec2(0,quartY)).add(DEFAULT_BUTTON_SIZE.mul(-0.5f)),
                DEFAULT_BUTTON_SIZE,
                "GENERATE_LEVEL",
                () ->{
                    eng.setScene(new LevelGeneratorToJSON());
                }
        );
    }
}
