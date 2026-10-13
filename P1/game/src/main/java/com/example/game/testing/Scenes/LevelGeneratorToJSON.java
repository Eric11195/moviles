package com.example.game.testing.Scenes;

import com.example.engine.Engine;
import com.example.game.testing.Systems.LevelGenerator;

import org.json.JSONObject;

import java.io.FileWriter;

public class LevelGeneratorToJSON extends mcd_Scene {
    @Override
    public void start(Engine eng) {
        JSONObject jo = LevelGenerator.generateNewLevel(
                this,
                eng,
                getShortLevelParams()
        );
        String file_name = "EasyLevel01";

        try {
            String json = jo.toString();
            FileWriter fw = new FileWriter(file_name+".json");
            fw.write(json);
            fw.close();
            System.out.println("JSON written to file "+file_name+".json succesfully");
        }catch(java.io.IOException e){
            e.printStackTrace();
        }
        eng.setScene(new MainMenuScene());
    }
    private static LevelGenerator.LevelGeneratorParams getShortLevelParams(){
        LevelGenerator.LevelGeneratorParams lg = new LevelGenerator.LevelGeneratorParams();
        lg.levelDurationInSeconds = 15;
        lg.levelInitialSpeed = 50;
        lg.maxNumberOfSimultaneousObstacles = 4;
        lg.obstacleExplosiveCreationBoost = 1;
        lg.obstacleLinearCreationPerSecond = 0.8f;
        lg.obstacleExplosiveCreationChance = 0.00000001f;
        return lg;
    }

}
