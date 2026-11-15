package com.example.game.testing.Systems.Shop;

import com.example.game.testing.EC.Component.shop.Buyable;
import com.example.game.testing.EC.Component.shop.ShopObject;
import com.example.game.testing.utils.mcd_Serializable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ShopManager implements mcd_Serializable {
    Set<String> boughtObjects;
    private ShopManager(List<String> b){
    }
    private static ShopManager _instance;
    public static void setInstance(ShopManager sm){
        _instance = sm;
    }
    public static ShopManager deserialize(JSONObject jo){
        List<String> ls = new ArrayList<>();
        if(jo==null){
            return new ShopManager(ls);
        }
        JSONArray ja = jo.getJSONArray("boughObjects");
        ja.toList().forEach((str)->{
            ls.add((String)str);
        });
        return new ShopManager(ls);
    }
    public void buyObject(Buyable object) {
        boughtObjects.add(object.getId());
    }
    public boolean isBought(Buyable object){
        return boughtObjects.contains(object);
    }

    @Override
    public JSONObject serialize() {
        JSONObject jo = new JSONObject();
        JSONArray ja = new JSONArray();
        boughtObjects.forEach(ja::put);
        jo.put("boughtObjects", ja);
        return jo;
    }
}
