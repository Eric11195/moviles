package com.example.game.testing.Systems.Shop;

import com.example.engine.IFileStorage;
import com.example.game.testing.utils.JSON_HELPER;
import com.example.game.testing.utils.mcd_Serializable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LevelKeyManager implements mcd_Serializable {
    /**
     * Keys 0101-3112 are reserved for day of the year keys
     * Keys 1-7 are reserved for day of the week keys
     * Key 0 is always unlocked
     * Key 3112-INT_MAX are reserved for specific levels
     */
    private final Set<Integer> keysUnlocked = new HashSet<>();
    private static LevelKeyManager _instance;
    //from 1 to 366 the current day of the year
    int currentDayMonth_key;
    int currentDayOfWeek_key;
    IFileStorage fs;
    public LevelKeyManager(IFileStorage fs){
        this.fs = fs;
        Calendar calendar = Calendar.getInstance();
        currentDayMonth_key = 100*calendar.get(Calendar.DAY_OF_MONTH)+calendar.get(Calendar.MONTH);
        currentDayOfWeek_key = calendar.get(Calendar.DAY_OF_WEEK)+calendar.get(Calendar.MONTH);
    }
    public static LevelKeyManager getInstance(){
        if(_instance==null)
            throw new RuntimeException("You must set instance before the call to this function");
        return _instance;
    }
    public static void setInstance(LevelKeyManager _instance) {
        LevelKeyManager._instance = _instance;
    }

    public void addKey(int newKey){
        keysUnlocked.add(newKey);
        JSON_HELPER.toJSONModifiable(fs,"levelKeys",this.serialize());
    }

    @Override
    public JSONObject serialize() {
        JSONObject jo = new JSONObject();
        JSONArray ja = new JSONArray();
        keysUnlocked.forEach(ja::put);
        jo.put("keysUnlocked",ja);
        return jo;
    }
    public static LevelKeyManager deserialize(IFileStorage fs){
        fs.setRelativePath("saves");
        JSONObject jo = JSON_HELPER.fromJSONModifiable(fs,"levelKeys");
        if(jo.length()<1){
            jo.put("keysUnlocked",new JSONArray());
        }
        JSONArray ja = jo.getJSONArray("keysUnlocked");
        LevelKeyManager pum = new LevelKeyManager(fs);
        for(int i = 0; i < ja.length(); ++i){
            pum.addKey(ja.getInt(i));
        }

        return pum;
    }
    //Returns true if this has the given key
    public boolean checkKey(int key){
        return key == 0 ||
            key == currentDayMonth_key ||
            key==currentDayOfWeek_key ||
            keysUnlocked.contains(key);
    }
}
