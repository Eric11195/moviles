package com.example.game.testing.Scenes;

import static com.example.game.testing.utils.JSON_HELPER.toJSON;

import com.example.engine.Engine;
import com.example.game.testing.Systems.levelGenerator.LevelGenerator;
import com.example.game.testing.Systems.levelGenerator.LevelGeneratorParams;

import org.json.JSONObject;

public class LevelGeneratorToJSON extends mcd_Scene {
    @Override
    public void start(Engine eng) {
        JSONObject jo = LevelGenerator.generateNewLevel(
                this,
                eng,
                getShortLevelParams()
        );
        String file_name = "EasyLevel01";
        toJSON(file_name, jo);
        eng.setScene(new MainMenuScene());
    }
    private static LevelGeneratorParams getShortLevelParams(){
        LevelGeneratorParams lg = new LevelGeneratorParams();
        lg.levelDurationInSeconds = 15;
        lg.levelInitialSpeed = 50;
        lg.maxNumberOfSimultaneousObstacles = 4;
        lg.obstacleExplosiveCreationBoost = 1;
        lg.obstacleLinearCreationPerSecond = 0.8f;
        lg.obstacleExplosiveCreationChance = 0.00000001f;
        return lg;
    }

}
