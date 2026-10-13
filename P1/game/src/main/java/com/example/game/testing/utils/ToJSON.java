package com.example.game.testing.utils;
import com.example.utils.Vec2;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

public class ToJSON {
    public static JSONArray toJSON(List<?> l) {
        JSONArray ja = new JSONArray();

        l.forEach(elem -> {
            if (elem instanceof mcd_Serializable) {
                ja.put(toJSON((mcd_Serializable) elem));
            }else if (elem instanceof Vec2) {
                ja.put(toJSON((Vec2) elem));
            } else {
                throw new IllegalArgumentException(
                        "Unsupported type: " + elem.getClass().getName()
                );
            }
        });

        return ja;
    }
//    public static <T extends mcd_Serializable> JSONArray toJSON(List<T> l){
//        JSONArray ja = new JSONArray();
//        l.forEach((elem)->{
//            ja.put(toJSON(elem));
//        });
//        return ja;
//    }
    public static <T extends mcd_Serializable> JSONObject toJSON(T obj){
        return obj.serialize();
    }
    public static JSONObject toJSON(Vec2 v){
        JSONObject jo = new JSONObject();
        jo.put("x",v.x);
        jo.put("y",v.y);
        return jo;
    }
}
