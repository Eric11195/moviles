package com.example.game.testing.utils;
import com.example.engine.ColorEng;
import com.example.engine.FontEng;
import com.example.engine.GraphicsInterface;
import com.example.engine.IFileStorage;
import com.example.utils.Vec2;

import org.json.JSONArray;
import org.json.JSONObject;

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
    public static JSONObject toJSON(ColorEng clr, JSONObject jo){
        jo.put("color", clr.getColorAsInt());
        return jo;
    }
    public static JSONObject toJSON(FontEng f, JSONObject jo){
        jo.put("fontName", f.fileName);
        jo.put("size", f.fontSize);
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

    public static JSONObject fromJSON(IFileStorage fileStorage, String file_name) {
        System.out.println("Begin Reading "+file_name+" in "+fileStorage.getCurrentPath());
        try {
            if (!file_name.endsWith(".json")) {
                file_name += ".json";
            }

            String content = fileStorage.read(file_name);
            JSONObject jo = new JSONObject(content);
            System.out.println(file_name+" Read succesfully");
            return jo;
        } catch (java.io.IOException e) {
            System.out.println("Error while reading file "+file_name+" in "+fileStorage.getCurrentPath());
            e.printStackTrace();
        }

        return null;
    }
    public static Vec2 vec2FromJSON(JSONObject v){
        return new Vec2(
                (float)v.getDouble("x"),
                (float)v.getDouble("y")
        );
    }
    public static ColorEng colorFromJSON(JSONObject clr){
        return new ColorEng(
          clr.getInt("color")
        );
    }
    public static FontEng fontFromJSON(GraphicsInterface g, JSONObject f){
        if(f.isNull("fontName")){
            return g.createFont(null, f.getInt("fontSize"));
        }else{
            return g.createFont(f.getString("fontName"), f.getInt("fontSize"));
        }
    }
}
