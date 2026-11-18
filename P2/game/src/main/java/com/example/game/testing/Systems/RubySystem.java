package com.example.game.testing.Systems;

import com.example.engine.IFileStorage;
import com.example.game.testing.utils.JSON_HELPER;
import com.example.game.testing.utils.mcd_Serializable;

import org.json.JSONObject;

import java.nio.file.FileSystem;

public class RubySystem{
    private static int rubyCount;
    public static int getRubies(){
        return rubyCount;
    }
    public static boolean canPay(int amount){
        return rubyCount >= amount;
    }
    public static void pay(IFileStorage fs, int amount){
        if(!canPay(amount))
            throw new RuntimeException("You need to ensure that it can be paid before calling this function");
        rubyCount -= amount;
        serialize(fs);
    }
    public static void gainRubies(IFileStorage fs, int amount){
        rubyCount += amount;
        serialize(fs);
    }

    public static void deserialize(IFileStorage fs){
        fs.setRelativePath("saves");
        JSONObject jo = JSON_HELPER.fromJSONModifiable(fs,"rubies");
        if(jo.length() < 1){
            rubyCount = 0;
        }else {
            rubyCount = jo.getInt("count");
        }
    }
    public static void serialize(IFileStorage fs) {
        fs.setRelativePath("saves");
        JSONObject jo = new JSONObject();
        jo.put("count", rubyCount);
        JSON_HELPER.toJSONModifiable(fs, "rubies", jo);
    }
}
