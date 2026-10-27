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

        // 20 coins minimum required
        lg.levelInitialSpeed = 70f;
        // Needs enough time to collect 20 coins. With ~1.5 coins per second, ~40-50 seconds is plenty. Set to 90s to give a good margin while keeping the level active.
        lg.levelDurationInSeconds = 90f;
        lg.maxNumberOfSimultaneousObstacles = 8;
        lg.maxNumberOfSimultaneousCoins = 15;

        // Moderate obstacles
        lg.obstacleLinearCreationPerSecond = 1.5f;
        lg.obstacleExplosiveCreationChance = 0.0001;
        lg.obstacleExplosiveCreationBoost = 2.0f;

        // Frequent coins to ensure they can hit 20
        lg.coinLinearCreationPerSecond = 1.5f;
        lg.augmentSpeedEachXPixels = 15000;
        lg.augmentSpeedByYEachXPixels = 10f;

        return lg;
    }

    public static LevelGeneratorParams getCoin2Params() {
        LevelGeneratorParams lg = new LevelGeneratorParams();

        // 40 coins minimum required
        lg.levelInitialSpeed = 80f;
        // Needs enough time to collect 40 coins. 120s is reasonable.
        lg.levelDurationInSeconds = 120f;
        lg.maxNumberOfSimultaneousObstacles = 12;
        lg.maxNumberOfSimultaneousCoins = 15;

        // More obstacles
        lg.obstacleLinearCreationPerSecond = 2.0f;
        lg.obstacleExplosiveCreationChance = 0.0005;
        lg.obstacleExplosiveCreationBoost = 3.0f;

        // Frequent coins to ensure they can hit 40
        lg.coinLinearCreationPerSecond = 1.8f;
        lg.augmentSpeedEachXPixels = 12000;
        lg.augmentSpeedByYEachXPixels = 12f;

        return lg;
    }

    public static LevelGeneratorParams getCoin3Params() {
        LevelGeneratorParams lg = new LevelGeneratorParams();

        // 60 coins minimum required
        lg.levelInitialSpeed = 95f;
        // Needs enough time to collect 60 coins. 180s gives a good window.
        lg.levelDurationInSeconds = 180f;
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

        return lg;
    }

    private static LevelGeneratorParams getDist1Params() {
        LevelGeneratorParams lg = new LevelGeneratorParams();

        // Target: 10000 pixels. Speed starts at 70. 10000 / 70 = ~142 seconds minimum.
        // We set duration slightly higher to allow for acceleration.
        lg.levelInitialSpeed = 70f;
        lg.levelDurationInSeconds = 160f;

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

        return lg;
    }

    private static LevelGeneratorParams getDist2Params() {
        LevelGeneratorParams lg = new LevelGeneratorParams();

        // Target: 20000 pixels. Initial speed 85. 20000 / 85 = ~235 seconds minimum.
        lg.levelInitialSpeed = 85f;
        lg.levelDurationInSeconds = 250f;

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

        return lg;
    }

    private static LevelGeneratorParams getDist3Params() {
        LevelGeneratorParams lg = new LevelGeneratorParams();

        // Target: 30000 pixels. Initial speed 100. 30000 / 100 = ~300 seconds minimum.
        lg.levelInitialSpeed = 100f;
        lg.levelDurationInSeconds = 320f;

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

        return lg;
    }


    }
