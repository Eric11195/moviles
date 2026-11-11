package com.example.game.testing.Systems.Shop;

import com.example.game.testing.utils.mcd_Serializable;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PowerUpManager implements mcd_Serializable {
    public static enum extraVars{
        shieldCount,
        extraStartMeters,
        extraStartSpeed,
        COUNT
    }
    private final List<Integer> powerUpVars;
    public PowerUpManager(){
        this.powerUpVars = new ArrayList<Integer>();
    }
    private static PowerUpManager _instance;
    public static PowerUpManager getInstance(){
        if(_instance==null)
            throw new RuntimeException("You must set instance before the call to this function");
        return _instance;
    }
    public static void setInstance(PowerUpManager _instance) {
        PowerUpManager._instance = _instance;
    }

    @Override
    public JSONObject serialize() {
        JSONObject jo = new JSONObject();
        var values = extraVars.values();
        for(int i = 0; i < extraVars.COUNT.ordinal(); ++i){
            jo.put(values[i].toString(), this.powerUpVars.get(i));
        }
        return jo;
    }
    static PowerUpManager deserialize(JSONObject jo){
        PowerUpManager pum = new PowerUpManager();
        var values = extraVars.values();
        for(int i = 0; i < extraVars.COUNT.ordinal(); ++i){
            String key = values[i].toString();
            pum.powerUpVars.add(jo.getInt(key));
        }
        return pum;
    }
    public void setVar(extraVars var, int newValue){
        this.powerUpVars.set(var.ordinal(), newValue);
    }
    public void addToVar(extraVars var, int valueToAdd){
        this.powerUpVars.set(var.ordinal(), valueToAdd+this.powerUpVars.get(var.ordinal()));
    }
    public int getVar(extraVars var){
        return this.powerUpVars.get(var.ordinal());
    }
}
