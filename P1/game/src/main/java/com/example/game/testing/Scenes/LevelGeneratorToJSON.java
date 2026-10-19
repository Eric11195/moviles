package com.example.game.testing.Scenes;

import static com.example.game.testing.utils.JSON_HELPER.toJSON;

import com.example.engine.Engine;
import com.example.engine.IFileStorage;
import com.example.game.testing.Systems.levelGenerator.LevelFileGenerator;
import com.example.game.testing.Systems.levelGenerator.LevelGeneratorParams;

import org.json.JSONObject;

public class LevelGeneratorToJSON extends mcd_Scene {
    public LevelGeneratorToJSON(Engine eng){
        super(eng);
    }
    @Override
    public void start(Engine eng) {
        JSONObject joLow = LevelFileGenerator.generateNewLevel(
                this,
                eng,
                getEasyLevelParams()
        );
        JSONObject joMid = LevelFileGenerator.generateNewLevel(
                this,
                eng,
                getMediumLevelParams()
        );
        JSONObject joHard = LevelFileGenerator.generateNewLevel(
                this,
                eng,
                getHardLevelParams()
        );
        IFileStorage fileStorage = eng.getFileStorage();
        fileStorage.setRelativePath("Assets/Levels");
        String file_name_easy = "Level1";
        toJSON(fileStorage, file_name_easy, joLow);
        String file_name_mid = "Level2";
        toJSON(fileStorage, file_name_mid, joMid);
        String file_name_hard = "Level3";
        toJSON(fileStorage, file_name_hard, joHard);
        eng.setScene(new MainMenuScene(eng));
    }
    private static LevelGeneratorParams getEasyLevelParams(){
        LevelGeneratorParams lg = new LevelGeneratorParams();
        lg.levelDurationInSeconds = 20;
        lg.levelInitialSpeed = 100;
        lg.maxNumberOfSimultaneousObstacles = 4;
        lg.obstacleExplosiveCreationBoost = 1;
        lg.obstacleLinearCreationPerSecond = 0.8f;
        lg.obstacleExplosiveCreationChance = 0.00000001;
        lg.augmentSpeedEachXPixels = 300;
        lg.augmentSpeedByYEachXPixels = 30;
        return lg;
    }
    public static LevelGeneratorParams getMediumLevelParams(){
        LevelGeneratorParams lg = new LevelGeneratorParams();
        lg.levelDurationInSeconds = 30;
        lg.levelInitialSpeed = 150;
        lg.maxNumberOfSimultaneousObstacles = 8;
        lg.obstacleExplosiveCreationBoost = 1.6f;
        lg.obstacleLinearCreationPerSecond = 0.6f;
        lg.obstacleExplosiveCreationChance = 0.00000001;
        lg.augmentSpeedEachXPixels = 200;
        lg.augmentSpeedByYEachXPixels = 30;
        return lg;
    }
    public static LevelGeneratorParams getHardLevelParams(){
        LevelGeneratorParams lg = new LevelGeneratorParams();
        lg.levelDurationInSeconds = 40;
        lg.levelInitialSpeed = 200;
        lg.maxNumberOfSimultaneousObstacles = 12;
        lg.obstacleExplosiveCreationBoost = 0.7f;
        lg.obstacleLinearCreationPerSecond = 1.2f;
        lg.obstacleExplosiveCreationChance = 0.000001;
        lg.augmentSpeedEachXPixels = 100;
        lg.augmentSpeedByYEachXPixels = 30;
        return lg;
    }

}
