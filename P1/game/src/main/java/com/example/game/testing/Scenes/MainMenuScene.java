package com.example.game.testing.Scenes;

import static com.example.game.testing.EC.Prefabs.ButtonPrefab.createButton;
import static com.example.game.testing.constants.Constants.DEFAULT_BUTTON_SIZE;
import static com.example.game.testing.constants.Constants.LOGIC_WINDOW_SIZE;
import static com.example.game.testing.constants.Constants.getDefaultLevelGeneratorParams;

import com.example.engine.Engine;
import com.example.engine.GraphicsInterface;
import com.example.game.testing.EC.Prefabs.TextPrefab;
import com.example.game.testing.Systems.levelGenerator.CoinGenerator;
import com.example.game.testing.Systems.levelGenerator.HazardGenerator;
import com.example.game.testing.Systems.levelGenerator.LevelGeneratorParams;
import com.example.game.testing.Systems.levelGenerator.ProceduralLevelGenerator;
import com.example.game.testing.constants.Constants;
import com.example.game.testing.utils.ColorLibrary;
import com.example.game.testing.utils.FontLibrary;
import com.example.utils.Vec2;

/**
 * Main menu scene contains buttons for playing infinite level, preset level or generate new level files
 */
public class MainMenuScene extends mcd_Scene{
    Vec2 textSize = new Vec2(LOGIC_WINDOW_SIZE.x/3.0f*2,LOGIC_WINDOW_SIZE.y/6.0f);
    float yOffset = 30.0f;
    String[] text = {"MOCHILA", "COHETONA", "DISFRUTONA"};
    public MainMenuScene(Engine eng){
        super(eng);
    }
    @Override
    public void start(Engine eng) {
        GraphicsInterface g = eng.getGraphics();
        g.setWindowLogicSize((int)LOGIC_WINDOW_SIZE.x, (int)LOGIC_WINDOW_SIZE.y);
        
        FontLibrary.createFont(g, FontLibrary.FontName.STANDARD,16, "FascinateInline-Regular.ttf");
        FontLibrary.createFont(g, FontLibrary.FontName.HEADER,72, "FascinateInline-Regular.ttf");
        FontLibrary.createFont(g, FontLibrary.FontName.SUBHEADER,32, "FascinateInline-Regular.ttf");
        g.setFont(FontLibrary.getFont(FontLibrary.FontName.SUBHEADER));

        float screen_width_third = LOGIC_WINDOW_SIZE.x/3.0f;
        float screen_width_sixth = LOGIC_WINDOW_SIZE.x/6.0f;
        float screen_height_half =LOGIC_WINDOW_SIZE.y/2.0f;
        float screen_height_eighth =LOGIC_WINDOW_SIZE.y/8.0f;
        Vec2 [] ButtonCentralPos = {
            new Vec2(screen_width_third, screen_height_half),
            new Vec2(2*screen_width_third, screen_height_half),
        };
        for(int i = 0; i < ButtonCentralPos.length; ++i){
            ButtonCentralPos[i] = ButtonCentralPos[i].add(DEFAULT_BUTTON_SIZE.mul(-0.5f));
        }

        for (int i = 0; i<text.length;i++){
            TextPrefab.createText(this,new Vec2(screen_width_sixth,screen_height_eighth+yOffset*i),textSize,text[i], ColorLibrary.Black);
        }
        
        createButton(
            this,
            ButtonCentralPos[0],
            DEFAULT_BUTTON_SIZE,
            "INFINITE MODE",
            () ->{
                MainGameScene mgs = new MainGameScene(eng);
                ProceduralLevelGenerator plg = new ProceduralLevelGenerator(mgs,getDefaultLevelGeneratorParams(), false);
                mgs.setLevelGen(plg);
                eng.setScene(mgs);
            }
        );
        createButton(
            this,
            ButtonCentralPos[1],
            DEFAULT_BUTTON_SIZE,
            "RETOS",
            () ->{
                eng.setScene(new LevelSelectorScene(eng));
            }
        );
    }
}
