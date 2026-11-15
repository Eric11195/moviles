package com.example.game.testing.Scenes;

import static com.example.game.testing.utils.JSON_HELPER.toJSONModifiable;

import com.example.engine.Engine;
import com.example.engine.IFileStorage;
import com.example.game.testing.Systems.LevelGenerator.LevelFileGenerator;
import com.example.game.testing.Systems.LevelGenerator.LevelGeneratorParams;
import com.example.game.testing.utils.JSON_HELPER;

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
        fileStorage.setRelativePath("levels");
        JSON_HELPER.toJSONModifiable(fileStorage, "Level1", j1);
        JSON_HELPER.toJSONModifiable(fileStorage, "Level2", j2);
        JSON_HELPER.toJSONModifiable(fileStorage, "Level3", j3);
        JSON_HELPER.toJSONModifiable(fileStorage, "Level4", j4);
        JSON_HELPER.toJSONModifiable(fileStorage, "Level5", j5);
        JSON_HELPER.toJSONModifiable(fileStorage, "Level6", j6);
        eng.setScene(new MainMenuScene(eng));
    }
    private static LevelGeneratorParams getCoin1Params() {
        LevelGeneratorParams lg = new LevelGeneratorParams();

        lg.levelInitialSpeed = 70f;
        lg.levelCoinsToWin = 20;

        lg.levelTotalMeters = 50;
        lg.maxNumberOfSimultaneousObstacles = 8;
        lg.maxNumberOfSimultaneousCoins = 15;

        lg.obstacleLinearCreationPerSecond = 1.5f;
        lg.obstacleExplosiveCreationChance = 0.0001;
        lg.obstacleExplosiveCreationBoost = 2.0f;

        lg.coinLinearCreationPerSecond = 1.5f;
        lg.augmentSpeedEachXPixels = 15000;
        lg.augmentSpeedByYEachXPixels = 10f;
        lg.reward = 10;
        return lg;
    }

    public static LevelGeneratorParams getCoin2Params() {
        LevelGeneratorParams lg = new LevelGeneratorParams();

        lg.levelInitialSpeed = 80f;
        lg.levelCoinsToWin = 40;
        lg.levelTotalMeters = 100;

        lg.maxNumberOfSimultaneousObstacles = 12;
        lg.maxNumberOfSimultaneousCoins = 15;

        lg.obstacleLinearCreationPerSecond = 2.0f;
        lg.obstacleExplosiveCreationChance = 0.0005;
        lg.obstacleExplosiveCreationBoost = 3.0f;

        // Frequent coins to ensure they can hit 40
        lg.coinLinearCreationPerSecond = 1.8f;
        lg.augmentSpeedEachXPixels = 12000;
        lg.augmentSpeedByYEachXPixels = 12f;
        lg.reward = 20;
        return lg;
    }

    public static LevelGeneratorParams getCoin3Params() {
        LevelGeneratorParams lg = new LevelGeneratorParams();

        lg.levelInitialSpeed = 95f;
        lg.levelCoinsToWin = 60;
        lg.levelTotalMeters = 150;
        lg.maxNumberOfSimultaneousObstacles = 18;
        lg.maxNumberOfSimultaneousCoins = 15;

        // Heavy obstacles
        lg.obstacleLinearCreationPerSecond = 2.5f;
        lg.obstacleExplosiveCreationChance = 0.001;
        lg.obstacleExplosiveCreationBoost = 4.0f;

        // Frequent coins to ensure they can hit 60
        lg.coinLinearCreationPerSecond = 2.0f;
        lg.augmentSpeedEachXPixels = 10000;
        lg.augmentSpeedByYEachXPixels = 15f;
        lg.reward = 30;
        return lg;
    }

    private static LevelGeneratorParams getDist1Params() {
        LevelGeneratorParams lg = new LevelGeneratorParams();
        lg.levelInitialSpeed = 70f;
        lg.levelTotalMeters = 50;
        lg.levelCoinsToWin = -1;

        lg.maxNumberOfSimultaneousObstacles = 12;
        lg.maxNumberOfSimultaneousCoins = 5;

        // Obstacle heavy
        lg.obstacleLinearCreationPerSecond = 2.0f;
        lg.obstacleExplosiveCreationChance = 0.0005;
        lg.obstacleExplosiveCreationBoost = 2.5f;

        // Sparse coins
        lg.coinLinearCreationPerSecond = 0.3f;

        lg.augmentSpeedEachXPixels = 3000; // Accelerates frequently to help reach the goal faster
        lg.augmentSpeedByYEachXPixels = 5f;
        lg.reward = 10;
        return lg;
    }

    private static LevelGeneratorParams getDist2Params() {
        LevelGeneratorParams lg = new LevelGeneratorParams();

        // Target: 20000 pixels. Initial speed 85. 20000 / 85 = ~235 seconds minimum.
        lg.levelInitialSpeed = 85f;
        lg.levelTotalMeters = 100;
        lg.levelCoinsToWin = -1;

        lg.maxNumberOfSimultaneousObstacles = 16;
        lg.maxNumberOfSimultaneousCoins = 5;

        // Very obstacle heavy
        lg.obstacleLinearCreationPerSecond = 2.8f;
        lg.obstacleExplosiveCreationChance = 0.001;
        lg.obstacleExplosiveCreationBoost = 3.5f;

        // Sparse coins
        lg.coinLinearCreationPerSecond = 0.3f;

        lg.augmentSpeedEachXPixels = 4000;
        lg.augmentSpeedByYEachXPixels = 8f;
        lg.reward = 20;
        return lg;
    }

    private static LevelGeneratorParams getDist3Params() {
        LevelGeneratorParams lg = new LevelGeneratorParams();

        lg.levelInitialSpeed = 100f;
        lg.levelTotalMeters = 150;
        lg.levelCoinsToWin = -1;

        lg.maxNumberOfSimultaneousObstacles = 22;
        lg.maxNumberOfSimultaneousCoins = 5;

        // Extremely obstacle heavy
        lg.obstacleLinearCreationPerSecond = 3.5f;
        lg.obstacleExplosiveCreationChance = 0.005; // Noticeable spikes in difficulty
        lg.obstacleExplosiveCreationBoost = 5.0f;

        // Sparse coins
        lg.coinLinearCreationPerSecond = 0.3f;

        lg.augmentSpeedEachXPixels = 5000;
        lg.augmentSpeedByYEachXPixels = 10f;
        lg.reward = 30;
        return lg;
    }


    }
