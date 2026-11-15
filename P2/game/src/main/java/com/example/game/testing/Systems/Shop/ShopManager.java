package com.example.game.testing.Systems.Shop;

import com.example.engine.IFileStorage;
import com.example.game.testing.EC.Component.shop.Buyable;
import com.example.game.testing.EC.Component.shop.ShopObject;
import com.example.game.testing.utils.JSON_HELPER;
import com.example.game.testing.utils.mcd_Serializable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ShopManager implements mcd_Serializable {
    Set<String> boughtObjects;
    IFileStorage fs;
    private ShopManager(List<String> b, IFileStorage fs){
        this.boughtObjects = new HashSet<String>();
        this.boughtObjects.addAll(b);
        this.fs = fs;
    }
    private static ShopManager _instance;
    public static ShopManager getInstance(){
        return _instance;
    }
    public static void setInstance(ShopManager sm){
        _instance = sm;
    }
    public static ShopManager deserialize(JSONObject jo, IFileStorage fs){
        List<String> ls = new ArrayList<>();

        if(jo==null || jo.length()==0){
            return new ShopManager(ls,fs);
        }
        JSONArray ja = jo.getJSONArray("boughtObjects");
        for(int i = 0; i < ja.length(); ++i){
            ls.add(ja.getString(i));
        }
        return new ShopManager(ls,fs);
    }
    public void buyObject(Buyable object) {
        boughtObjects.add(object.getId());
        fs.setRelativePath("saves");
        JSON_HELPER.toJSONModifiable(fs, "bought", serialize());
    }
    public boolean isBought(Buyable object){
        if(object==null) return false;
        return boughtObjects.contains(object.getId());
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
