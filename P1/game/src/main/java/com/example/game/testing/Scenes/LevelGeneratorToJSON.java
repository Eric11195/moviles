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
        //Coin Levels
        JSONObject j1 = LevelFileGenerator.generateNewLevel(
                this,
                eng,
                getCoin1Params()
        );
        JSONObject j2 = LevelFileGenerator.generateNewLevel(
                this,
                eng,
                getCoin2Params()
        );
        JSONObject j3 = LevelFileGenerator.generateNewLevel(
                this,
                eng,
                getCoin3Params()
        );
        //Distance Levels
        JSONObject j4 = LevelFileGenerator.generateNewLevel(
                this,
                eng,
                getDist1Params()
        );
        JSONObject j5 = LevelFileGenerator.generateNewLevel(
                this,
                eng,
                getDist2Params()
        );
        JSONObject j6 = LevelFileGenerator.generateNewLevel(
                this,
                eng,
                getDist3Params()
        );
        IFileStorage fileStorage = eng.getFileStorage();
        fileStorage.setRelativePath("Assets/Levels");
        toJSON(fileStorage, "Level1", j1);
        toJSON(fileStorage, "Level2", j2);
        toJSON(fileStorage, "Level3", j3);
        toJSON(fileStorage, "Level4", j4);
        toJSON(fileStorage, "Level5", j5);
        toJSON(fileStorage, "Level6", j6);
        eng.setScene(new MainMenuScene(eng));
    }
    private static LevelGeneratorParams getCoin1Params(){
        LevelGeneratorParams lg = new LevelGeneratorParams();
        lg.levelDurationInSeconds = 20;
        lg.levelInitialSpeed = 30;
        lg.maxNumberOfSimultaneousObstacles = 4;
        lg.obstacleExplosiveCreationBoost = 1;
        lg.obstacleLinearCreationPerSecond = 0.8f;
        lg.coinLinearCreationPerSecond = 3.0f;
        lg.obstacleExplosiveCreationChance = 0.00000001;
        lg.augmentSpeedEachXPixels = 5000;
        lg.augmentSpeedByYEachXPixels = 15;
        lg.coinLinearCreationPerSecond = 1;
        lg.maxNumberOfSimultaneousCoins = 10;
        return lg;
    }
    public static LevelGeneratorParams getCoin2Params(){
        LevelGeneratorParams lg = new LevelGeneratorParams();
        lg.levelDurationInSeconds = 30;
        lg.levelInitialSpeed = 30;
        lg.maxNumberOfSimultaneousObstacles = 8;
        lg.obstacleExplosiveCreationBoost = 1.6f;
        lg.obstacleLinearCreationPerSecond = 0.6f;
        lg.coinLinearCreationPerSecond = 4.0f;
        lg.obstacleExplosiveCreationChance = 0.00000001;
        lg.augmentSpeedEachXPixels = 5000;
        lg.augmentSpeedByYEachXPixels = 25;
        lg.coinLinearCreationPerSecond = 1;
        lg.maxNumberOfSimultaneousCoins = 10;
        return lg;
    }
    public static LevelGeneratorParams getCoin3Params(){
        LevelGeneratorParams lg = new LevelGeneratorParams();
        lg.levelDurationInSeconds = 40;
        lg.levelInitialSpeed = 50;
        lg.maxNumberOfSimultaneousObstacles = 12;
        lg.obstacleExplosiveCreationBoost = 0.7f;
        lg.obstacleLinearCreationPerSecond = 1.2f;
        lg.coinLinearCreationPerSecond = 5.0f;
        lg.obstacleExplosiveCreationChance = 0.000001;
        lg.augmentSpeedEachXPixels = 4000;
        lg.augmentSpeedByYEachXPixels = 15;
        lg.coinLinearCreationPerSecond = 1;
        lg.maxNumberOfSimultaneousCoins = 10;
        return lg;
    }
    private static LevelGeneratorParams getDist1Params(){
        LevelGeneratorParams lg = new LevelGeneratorParams();
        lg.levelDurationInSeconds = 20;
        lg.levelInitialSpeed = 50;
        lg.maxNumberOfSimultaneousObstacles = 4;
        lg.obstacleExplosiveCreationBoost = 1;
        lg.obstacleLinearCreationPerSecond = 0.8f;
        lg.coinLinearCreationPerSecond = 3.0f;
        lg.obstacleExplosiveCreationChance = 0.00000001;
        lg.augmentSpeedEachXPixels = 7000;
        lg.augmentSpeedByYEachXPixels = 30;
        lg.coinLinearCreationPerSecond = 1;
        lg.maxNumberOfSimultaneousCoins = 10;
        return lg;
    }
    private static LevelGeneratorParams getDist2Params(){
        LevelGeneratorParams lg = new LevelGeneratorParams();
        lg.levelDurationInSeconds = 20;
        lg.levelInitialSpeed = 50;
        lg.maxNumberOfSimultaneousObstacles = 4;
        lg.obstacleExplosiveCreationBoost = 1;
        lg.obstacleLinearCreationPerSecond = 0.8f;
        lg.coinLinearCreationPerSecond = 3.0f;
        lg.obstacleExplosiveCreationChance = 0.00000001;
        lg.augmentSpeedEachXPixels = 6000;
        lg.augmentSpeedByYEachXPixels = 30;
        lg.coinLinearCreationPerSecond = 1;
        lg.maxNumberOfSimultaneousCoins = 10;
        return lg;
    }
    private static LevelGeneratorParams getDist3Params(){
        LevelGeneratorParams lg = new LevelGeneratorParams();
        lg.levelDurationInSeconds = 20;
        lg.levelInitialSpeed = 60;
        lg.maxNumberOfSimultaneousObstacles = 4;
        lg.obstacleExplosiveCreationBoost = 1;
        lg.obstacleLinearCreationPerSecond = 0.8f;
        lg.coinLinearCreationPerSecond = 3.0f;
        lg.obstacleExplosiveCreationChance = 0.00000001;
        lg.augmentSpeedEachXPixels = 5000;
        lg.augmentSpeedByYEachXPixels = 30;
        lg.coinLinearCreationPerSecond = 2;
        lg.maxNumberOfSimultaneousCoins = 10;
        return lg;
    }
}
