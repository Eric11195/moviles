package com.example.game.testing.Systems;

import com.example.engine.Engine;
import com.example.game.testing.utils.JSON_HELPER;

import org.json.JSONException;
import org.json.JSONObject;

public class LevelsSystem{

    static Engine eng;
    static String currentLevel;
    public static String getCurrentLevelName()
    {
        return currentLevel;
    }
    public static void init(Engine engine)
    {
        eng =engine;
    }
    public static void completedLevel()
    {

        JSONObject info = JSON_HELPER.fromJSONModifiable(eng.getFileStorage(), "infoLevels");
        if(!info.has(currentLevel)) {

            info.put(currentLevel, new JSONObject());
        }
        JSONObject infoLevel= info.getJSONObject(currentLevel);
        try {
        infoLevel.put("LevelCompleted",true);
        JSON_HELPER.toJSONModifiable(eng.getFileStorage(),"infoLevels",info);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
    public static boolean checkRecord(int score)
    {
        JSONObject info = JSON_HELPER.fromJSONModifiable(eng.getFileStorage(), "infoLevels");
        if(!info.has(currentLevel)) {

            info.put(currentLevel, new JSONObject());
        }
        JSONObject infoLevel= info.getJSONObject(currentLevel);
        if(!infoLevel.has("Record"))
        {
            infoLevel.put("Record",score);
            return true;
        }
        boolean sol =infoLevel.getInt("Record") < score;
        infoLevel.put("Record", Math.max( infoLevel.getInt("Record"),score));

        return sol;
    }

    public static boolean checkCompleted()
    {
        JSONObject info = JSON_HELPER.fromJSONModifiable(eng.getFileStorage(), "infoLevels");

        if(!info.has(currentLevel)) {

            info.put(currentLevel, new JSONObject());
        }
        JSONObject infoLevel= info.getJSONObject(currentLevel);

        if(!infoLevel.has("LevelCompleted")){
            infoLevel.put("LevelCompleted", false);
        }

        return infoLevel.getBoolean("LevelCompleted");
    }

    public static void setCurrentLevel(String name)
    {
        currentLevel = name;
    }

}
