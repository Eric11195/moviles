package com.example.game.testing.utils;
import com.example.engine.IFileStorage;
import com.example.utils.Vec2;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.List;

public class JSON_HELPER {
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
        jo.put("x",(double)v.x);
        jo.put("y",(double)v.y);
        return jo;
    }

    public static void toJSON(IFileStorage fileStorage, String file_name, JSONObject jo){
        try {
            String json = jo.toString();
            fileStorage.write(file_name+".json", json);
            System.out.println("In path: "+ fileStorage.getCurrentPath()+"\nJSON written to file "+file_name+".json succesfully");
        }catch(java.io.IOException e){
            e.printStackTrace();
        }
    }

    public static JSONObject fromJSON(IFileStorage fileStorage, String file_name){
        try {
            String content = fileStorage.read(file_name+".json");
            return new JSONObject(content);
        }catch(java.io.IOException e){
            e.printStackTrace();
        }
        return null;
    }
    public static Vec2 fromJSON(JSONObject v){
        return new Vec2(
                (float)v.getDouble("x"),
                (float)v.getDouble("y")
        );
    }
}
