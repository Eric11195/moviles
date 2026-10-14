package com.example.game.testing.Scenes;

import static com.example.game.testing.EC.Prefabs.ButtonPrefab.createButton;
import static com.example.game.testing.constants.Constants.DEFAULT_BUTTON_SIZE;
import static com.example.game.testing.constants.Constants.LOGIC_WINDOW_SIZE;

import com.example.engine.Engine;
import com.example.engine.GraphicsInterface;
import com.example.game.testing.utils.FontLibrary;
import com.example.utils.Vec2;
import com.sun.java.accessibility.util.TopLevelWindowListener;

import java.awt.Button;

public class MainMenuScene extends mcd_Scene{
    public MainMenuScene(){
        super();
    }
    @Override
    public void start(Engine eng) {
        GraphicsInterface g = eng.getGraphics();
        
        FontLibrary.createFont(g, FontLibrary.FontName.STANDARD,16);
        FontLibrary.createFont(g, FontLibrary.FontName.HEADER,72);
        FontLibrary.createFont(g, FontLibrary.FontName.SUBHEADER,32);
        g.setFont(FontLibrary.getFont(FontLibrary.FontName.SUBHEADER));

        float screen_width_third = LOGIC_WINDOW_SIZE.x/3.0f;
        float screen_height_quart =LOGIC_WINDOW_SIZE.y/4.0f;
        float screen_height_three_quarts = LOGIC_WINDOW_SIZE.y*3/4.0f;
        Vec2 [] ButtonCentralPos = {
            new Vec2(screen_width_third, screen_height_quart),
            new Vec2(2*screen_width_third, screen_height_quart),
            new Vec2(screen_width_third, screen_height_three_quarts)
        };
        for(int i = 0; i < ButtonCentralPos.length; ++i){
            ButtonCentralPos[i] = ButtonCentralPos[i].add(DEFAULT_BUTTON_SIZE.mul(-0.5f));
        }

        createButton(
            this,
            ButtonCentralPos[0],
            DEFAULT_BUTTON_SIZE,
            "INFINITE MODE",
            () ->{
                eng.setScene(new MainGameScene());
            }
        );
        createButton(
            this,
            ButtonCentralPos[1],
            DEFAULT_BUTTON_SIZE,
            "RETOS",
            () ->{
                eng.setScene(new MainGameScene());
            }
        );
        createButton(
            this,
            ButtonCentralPos[2],
            DEFAULT_BUTTON_SIZE,
            "GENERATE LEVELS",
            () ->{
                eng.setScene(new LevelGeneratorToJSON());
            }
        );
    }
}
