package com.example.game.testing.Systems;

import com.example.engine.Engine;
import com.example.engine.IFileStorage;
import com.example.game.testing.Scenes.mcd_Scene;
import com.example.game.testing.utils.JSON_HELPER;

import org.json.JSONObject;

import javax.naming.Context;

public class LevelsSystem{
    static JSONObject [] levels= new JSONObject[7];

    static Engine eng;
    static int currentLevel;
    public static void init(Engine engine){eng =engine; }
    public static void setLevel(JSONObject level, int id)
    {
        levels[id+1] = level;
    }

    public static void completedLevel()
    {
        levels[currentLevel].put("LevelCompleted",true);
        JSON_HELPER.toJSON(eng.getFileStorage(),"Level"+Integer.toString(currentLevel),levels[currentLevel]);
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
