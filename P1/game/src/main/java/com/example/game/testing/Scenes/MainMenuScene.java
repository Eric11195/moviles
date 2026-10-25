package com.example.game.testing.Scenes;

import static com.example.game.testing.EC.Prefabs.ButtonPrefab.createButton;
import static com.example.game.testing.constants.Constants.DEFAULT_BUTTON_SIZE;
import static com.example.game.testing.constants.Constants.LOGIC_WINDOW_SIZE;

import com.example.engine.Engine;
import com.example.engine.GraphicsInterface;
import com.example.game.testing.Systems.levelGenerator.CoinGenerator;
import com.example.game.testing.Systems.levelGenerator.HazardGenerator;
import com.example.game.testing.Systems.levelGenerator.LevelGeneratorParams;
import com.example.game.testing.Systems.levelGenerator.ProceduralLevelGenerator;
import com.example.game.testing.utils.FontLibrary;
import com.example.utils.Vec2;

public class MainMenuScene extends mcd_Scene{
    public MainMenuScene(Engine eng){
        super(eng);
    }
    @Override
    public void start(Engine eng) {
        GraphicsInterface g = eng.getGraphics();
        g.setWindowLogicSize((int)LOGIC_WINDOW_SIZE.x, (int)LOGIC_WINDOW_SIZE.y);
        
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
                LevelGeneratorParams lgp = new LevelGeneratorParams();
                lgp.levelDurationInSeconds = -1;
                lgp.levelInitialSpeed = 70;
                lgp.maxNumberOfSimultaneousObstacles = 4;
                lgp.obstacleExplosiveCreationBoost = 1.5f;
                lgp.obstacleExplosiveCreationChance = 0.000000001;
                lgp.obstacleLinearCreationPerSecond = 0.8f;
                lgp.coinLinearCreationPerSecond = 1.0f;
                lgp.augmentSpeedEachXPixels = 100000;
                lgp.augmentSpeedByYEachXPixels = 10;
                lgp.maxNumberOfSimultaneousCoins = 20;
                MainGameScene mgs = new MainGameScene(eng);
                ProceduralLevelGenerator plg = new ProceduralLevelGenerator(mgs,lgp, false);
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
        createButton(
            this,
            ButtonCentralPos[2],
            DEFAULT_BUTTON_SIZE,
            "GENERATE LEVELS",
            () ->{
                eng.setScene(new LevelGeneratorToJSON(eng));
            }
        );
    }
}
