package com.example.game.testing.Systems;

import com.example.game.testing.Scenes.mcd_Scene;
import com.example.game.testing.utils.JSON_HELPER;

import org.json.JSONObject;

import javax.naming.Context;

public class LevelsSystem{
    static JSONObject [] levels= new JSONObject[7];

    static int currentLevel;
    public static void setLevel(JSONObject level, int id)
    {
        levels[id] = level;
    }

    public static void completedLevel()
    {
        levels[currentLevel].put("LevelCompleted",true);
    }
    public static boolean checkRecord(int score)
    {
        if(!levels[currentLevel].has("Record"))
        {
            levels[currentLevel].put("Record",score);
            return true;
        }
        boolean sol =levels[currentLevel].getInt("Record") < score;
        levels[currentLevel].put("Record", Math.max(levels[currentLevel].getInt("Record"),score));
        return sol;
    }

    public static boolean checkCompleted()
    {

        if(!levels[currentLevel].has("LevelCompleted"))
        {
            levels[currentLevel].put("LevelCompleted",false);

        }
        return levels[currentLevel].getBoolean("LevelCompleted");
    }

    public static void setCurrentLevel(int id)
    {
        currentLevel = id;
    }

    public static JSONObject getLevel(int id)
    {
        return levels[id];
    }

}
