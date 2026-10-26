package com.example.game.testing.Scenes;

import static com.example.game.testing.utils.JSON_HELPER.toJSON;

import com.example.engine.Engine;
import com.example.engine.IFileStorage;
import com.example.game.testing.Systems.levelGenerator.LevelFileGenerator;
import com.example.game.testing.Systems.levelGenerator.LevelGeneratorParams;

import org.json.JSONObject;


/**
 * This scene will generate level Files for 6 levels. Then return to mainScene.
 */
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
    private static LevelGeneratorParams getCoin1Params() {
        LevelGeneratorParams lg = new LevelGeneratorParams();

        lg.levelInitialSpeed = 30f;
        lg.levelDurationInSeconds = 80f;
        lg.maxNumberOfSimultaneousObstacles = 5;
        lg.maxNumberOfSimultaneousCoins = 10;

        lg.obstacleLinearCreationPerSecond = 1.0f;
        lg.obstacleExplosiveCreationChance = 0.00000001;
        lg.obstacleExplosiveCreationBoost = 1.0f;

        lg.coinLinearCreationPerSecond = 1.0f;
        lg.augmentSpeedEachXPixels = 10000;
        lg.augmentSpeedByYEachXPixels = 5f;

        return lg;
    }

    public static LevelGeneratorParams getCoin2Params() {
        LevelGeneratorParams lg = new LevelGeneratorParams();

        lg.levelInitialSpeed = 35f;
        lg.levelDurationInSeconds = 140f;
        lg.maxNumberOfSimultaneousObstacles = 8;
        lg.maxNumberOfSimultaneousCoins = 10;

        lg.obstacleLinearCreationPerSecond = 1.3f;
        lg.obstacleExplosiveCreationChance = 0.00000005;
        lg.obstacleExplosiveCreationBoost = 1.3f;

        lg.coinLinearCreationPerSecond = 1.2f;
        lg.augmentSpeedEachXPixels = 8000;
        lg.augmentSpeedByYEachXPixels = 7f;

        return lg;
    }

    public static LevelGeneratorParams getCoin3Params() {
        LevelGeneratorParams lg = new LevelGeneratorParams();

        lg.levelInitialSpeed = 45f;
        lg.levelDurationInSeconds = 170f;
        lg.maxNumberOfSimultaneousObstacles = 12;
        lg.maxNumberOfSimultaneousCoins = 10;

        lg.obstacleLinearCreationPerSecond = 1.6f;
        lg.obstacleExplosiveCreationChance = 0.0000001;
        lg.obstacleExplosiveCreationBoost = 1.6f;

        lg.coinLinearCreationPerSecond = 1.5f;
        lg.augmentSpeedEachXPixels = 6000;
        lg.augmentSpeedByYEachXPixels = 10f;

        return lg;
    }

    private static LevelGeneratorParams getDist1Params() {
        LevelGeneratorParams lg = new LevelGeneratorParams();

        lg.levelInitialSpeed = 40f;
        lg.levelDurationInSeconds = 300f;
        lg.maxNumberOfSimultaneousObstacles = 5;
        lg.maxNumberOfSimultaneousCoins = 10;

        lg.obstacleLinearCreationPerSecond = 0.9f;
        lg.obstacleExplosiveCreationChance = 0.00000001;
        lg.obstacleExplosiveCreationBoost = 1.0f;

        lg.coinLinearCreationPerSecond = 0.5f;
        lg.augmentSpeedEachXPixels = 10000;
        lg.augmentSpeedByYEachXPixels = 5f;

        return lg;
    }

    private static LevelGeneratorParams getDist2Params() {
        LevelGeneratorParams lg = new LevelGeneratorParams();

        lg.levelInitialSpeed = 50f;
        lg.levelDurationInSeconds = 450f;
        lg.maxNumberOfSimultaneousObstacles = 8;
        lg.maxNumberOfSimultaneousCoins = 10;

        lg.obstacleLinearCreationPerSecond = 1.2f;
        lg.obstacleExplosiveCreationChance = 0.00000005;
        lg.obstacleExplosiveCreationBoost = 1.3f;

        lg.coinLinearCreationPerSecond = 0.5f;
        lg.augmentSpeedEachXPixels = 8000;
        lg.augmentSpeedByYEachXPixels = 7f;

        return lg;
    }

    private static LevelGeneratorParams getDist3Params() {
        LevelGeneratorParams lg = new LevelGeneratorParams();

        lg.levelInitialSpeed = 60f;
        lg.levelDurationInSeconds = 550f;
        lg.maxNumberOfSimultaneousObstacles = 12;
        lg.maxNumberOfSimultaneousCoins = 10;

        lg.obstacleLinearCreationPerSecond = 1.5f;
        lg.obstacleExplosiveCreationChance = 0.0000001;
        lg.obstacleExplosiveCreationBoost = 1.6f;

        lg.coinLinearCreationPerSecond = 0.5f;
        lg.augmentSpeedEachXPixels = 6000;
        lg.augmentSpeedByYEachXPixels = 10f;

        return lg;
    }


    }
