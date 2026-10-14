package com.example.game.testing.Systems.levelGenerator;


import com.example.game.testing.utils.mcd_Serializable;

import org.json.JSONObject;

public class LevelGeneratorParams implements mcd_Serializable {
    //Px speed per second. The obstacles move that much towards the left of the screen
    public float levelInitialSpeed;
    public float levelDurationInSeconds;
    public int maxNumberOfSimultaneousObstacles;
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
     * each second this amount of obstacle are created by default
     */
    public float obstacleLinearCreationPerSecond;
    public JSONObject serialize(){
        JSONObject jo = new JSONObject();
        jo.put("levelInitialSpeed", Float.valueOf(levelInitialSpeed));
        jo.put("levelDurationInSeconds", Float.valueOf(levelDurationInSeconds));
        jo.put("maxNumberOfSimultaneousObstacles", Float.valueOf(maxNumberOfSimultaneousObstacles));
        jo.put("obstacleExplosiveCreationChance", Double.valueOf(obstacleExplosiveCreationChance));
        jo.put("obstacleExplosiveCreationBoost", Float.valueOf(obstacleExplosiveCreationBoost));
        jo.put("obstacleLinearCreationPerSecond", Float.valueOf(obstacleLinearCreationPerSecond));
        return jo;
    }
    public static LevelGeneratorParams deserialize(JSONObject jo){
        LevelGeneratorParams lgp = new LevelGeneratorParams();
        lgp.levelDurationInSeconds = jo.getFloat("levelDurationInSeconds");
        lgp.levelInitialSpeed = jo.getFloat("levelInitialSpeed");
        lgp.maxNumberOfSimultaneousObstacles = jo.getInt("maxNumberOfSimultaneousObstacles");
        lgp.obstacleExplosiveCreationBoost = jo.getFloat("obstacleExplosiveCreationBoost");
        lgp.obstacleExplosiveCreationChance = jo.getDouble("obstacleExplosiveCreationChance");
        lgp.obstacleLinearCreationPerSecond = jo.getFloat("obstacleLinearCreationPerSecond");
        return lgp;
    }
}