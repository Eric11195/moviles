package com.example.game.testing.Systems.levelGenerator;


import com.example.game.testing.utils.mcd_Serializable;

import org.json.JSONObject;

public class LevelGeneratorParams implements mcd_Serializable {
    //Px speed per second. The obstacles move that much towards the left of the screen
    public float levelInitialSpeed;
    /**
     * Each augmentSpeedEachXPixels pixels moved to the right the speed is increased by augmentSpeedByYEachXPixels
     */
    public int augmentSpeedEachXPixels;
    public float augmentSpeedByYEachXPixels;
    public int levelTotalMeters;
    public int levelCoinsToWin;
    //at most there will be this many obstacles
    public int maxNumberOfSimultaneousObstacles;
    //at most there will be this many coins on screen at the same time
    public int maxNumberOfSimultaneousCoins;
    /**
     * What are the chances of having an explosive obstacle creation
     * Moment in which the obstacle creator receives a huge boosts on tokens.
     */
    public double obstacleExplosiveCreationChance;
    /**
     * When obstacleExplosiveCreationChance happens.
     * How many token obstacles to add to the obstacle creator in that second. Float number.
     */
    public float obstacleExplosiveCreationBoost;
    /**
     * each second this amount is added to the obstacle acumulator. When it reaches one an obstacle will be spawn
     */
    public float obstacleLinearCreationPerSecond;
    /**
     * value to add to an acumulator. each time the acumulator reaches 1 it will spawn a coin
     */
    public float coinLinearCreationPerSecond;
    public JSONObject serialize(){
        JSONObject jo = new JSONObject();
        jo.put("levelInitialSpeed", Double.valueOf(levelInitialSpeed));
        jo.put("levelTotalMeters", levelTotalMeters);
        jo.put("levelCoinsToWin", levelCoinsToWin);
        jo.put("maxNumberOfSimultaneousObstacles", Double.valueOf(maxNumberOfSimultaneousObstacles));
        jo.put("obstacleExplosiveCreationChance", Double.valueOf(obstacleExplosiveCreationChance));
        jo.put("obstacleExplosiveCreationBoost", Double.valueOf(obstacleExplosiveCreationBoost));
        jo.put("obstacleLinearCreationPerSecond", Double.valueOf(obstacleLinearCreationPerSecond));
        jo.put("coinLinearCreationPerSecond", Double.valueOf(coinLinearCreationPerSecond));
        jo.put("augmentSpeedEachXPixels", augmentSpeedEachXPixels);
        jo.put("augmentSpeedByYEachXPixels", Double.valueOf(augmentSpeedByYEachXPixels));
        jo.put("maxNumberOfSimultaneousCoins",maxNumberOfSimultaneousCoins);
        return jo;
    }
    public static LevelGeneratorParams deserialize(JSONObject jo){
        LevelGeneratorParams lgp = new LevelGeneratorParams();
        lgp.levelTotalMeters = jo.getInt("levelTotalMeters");
        lgp.levelCoinsToWin = jo.getInt("levelCoinsToWin");
        lgp.levelInitialSpeed = (float)jo.getDouble("levelInitialSpeed");
        lgp.maxNumberOfSimultaneousObstacles = jo.getInt("maxNumberOfSimultaneousObstacles");
        lgp.obstacleExplosiveCreationBoost = (float)jo.getDouble("obstacleExplosiveCreationBoost");
        lgp.obstacleExplosiveCreationChance = (float)jo.getDouble("obstacleExplosiveCreationChance");
        lgp.obstacleLinearCreationPerSecond = (float)jo.getDouble("obstacleLinearCreationPerSecond");
        lgp.coinLinearCreationPerSecond = (float)jo.getDouble("coinLinearCreationPerSecond");
        lgp.augmentSpeedByYEachXPixels = (float)jo.getDouble("augmentSpeedByYEachXPixels");
        lgp.augmentSpeedEachXPixels = jo.getInt("augmentSpeedEachXPixels");
        lgp.maxNumberOfSimultaneousCoins = jo.getInt("maxNumberOfSimultaneousCoins");
        return lgp;
    }
}