package com.example.game.testing.Scenes;

import static com.example.game.testing.EC.Prefabs.ButtonPrefab.createButton;
import static com.example.game.testing.constants.Constants.DEFAULT_BUTTON_SIZE;
import static com.example.game.testing.constants.Constants.LOGIC_WINDOW_SIZE;

import com.example.engine.Engine;
import com.example.game.testing.Systems.levelGenerator.FileLevelGenerator;
import com.example.game.testing.Systems.levelGenerator.LevelFileGenerator;
import com.example.game.testing.utils.JSON_HELPER;
import com.example.utils.Vec2;

import org.json.JSONObject;

public class LevelSelectorScene extends mcd_Scene{
    LevelSelectorScene(Engine eng){
        super(eng);
    }
    @Override
    public void start(Engine eng) {
        int BUTTON_COUNT = 3;
        eng.getFileStorage().setRelativePath("Assets/Levels");
        for(int i = 1; i <= BUTTON_COUNT; ++i){
            Vec2 pos = new Vec2(
                LOGIC_WINDOW_SIZE.x/2.0f-0.5f*DEFAULT_BUTTON_SIZE.x,
                LOGIC_WINDOW_SIZE.y * ((float)i/(BUTTON_COUNT+1.0f)) - 0.5f*DEFAULT_BUTTON_SIZE.y
            );
            int finalI = i;
            createButton(
                    this,
                    pos,
                    DEFAULT_BUTTON_SIZE,
                    "Level"+Integer.toString(i),
                    ()->{loadLevel(eng,"Level"+Integer.toString(finalI));}
            );
        }
    }

    private void loadLevel(Engine eng, String filename){
        JSONObject obj = JSON_HELPER.fromJSON(eng.getFileStorage(),filename);
        if(obj==null){
            throw new RuntimeException("File level could not be found");
        }
        MainGameScene mainScene = new MainGameScene(eng);
        mainScene.setLevelGen(
            new FileLevelGenerator(
                mainScene,
                LevelFileGenerator.deserialize(obj)
            )
        );
        eng.setScene(mainScene);
    }
}
