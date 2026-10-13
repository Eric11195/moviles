package com.example.game.testing.utils;
import com.example.utils.Vec2;

import org.json.JSONArray;
import org.json.JSONObject;

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
        jo.put("x",v.x);
        jo.put("y",v.y);
        return jo;
    }

    public static void toJSON(String file_name, JSONObject jo){
        try {
            String json = jo.toString();
            FileWriter fw = new FileWriter(file_name+".json");
            fw.write(json);
            fw.close();
            System.out.println("JSON written to file "+file_name+".json succesfully");
        }catch(java.io.IOException e){
            e.printStackTrace();
        }
    }

    public static JSONObject fromJSON(String file_name){
        try {
            FileReader fr = new FileReader(file_name + ".json");
            JSONObject jo = new JSONObject(fr.toString());
            return jo;
        }catch(java.io.IOException e){
            e.printStackTrace();
        }
        return null;
    }
    public static Vec2 fromJSON(JSONObject v){
        return new Vec2(
            v.getFloat("x"),
            v.getFloat("y")
        );
    }
}
