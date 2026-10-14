package com.example.game.testing.Scenes;

import static com.example.game.testing.utils.JSON_HELPER.toJSON;

import com.example.engine.Engine;
import com.example.game.testing.Systems.levelGenerator.LevelGenerator;
import com.example.game.testing.Systems.levelGenerator.LevelGeneratorParams;

import org.json.JSONObject;

public class LevelGeneratorToJSON extends mcd_Scene {
    @Override
    public void start(Engine eng) {
        JSONObject joLow = LevelGenerator.generateNewLevel(
                this,
                eng,
                getEasyLevelParams()
        );
        JSONObject joMid = LevelGenerator.generateNewLevel(
                this,
                eng,
                getMediumLevelParams()
        );
        JSONObject joHard = LevelGenerator.generateNewLevel(
                this,
                eng,
                getHardLevelParams()
        );
        String file_name_easy = "EasyLevel01";
        toJSON(file_name_easy, joLow);
        String file_name_mid = "MidLevel02";
        toJSON(file_name_mid, joMid);
        String file_name_hard = "HardLevel01";
        toJSON(file_name_hard, joHard);
        eng.setScene(new MainMenuScene());
    }
    private static LevelGeneratorParams getEasyLevelParams(){
        LevelGeneratorParams lg = new LevelGeneratorParams();
        lg.levelDurationInSeconds = 20;
        lg.levelInitialSpeed = 100;
        lg.maxNumberOfSimultaneousObstacles = 4;
        lg.obstacleExplosiveCreationBoost = 1;
        lg.obstacleLinearCreationPerSecond = 0.8f;
        lg.obstacleExplosiveCreationChance = 0.00000001;
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
        return lg;
    }

}
