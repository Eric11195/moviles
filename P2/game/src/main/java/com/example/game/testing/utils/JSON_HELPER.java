package com.example.game.testing.utils;
import com.example.engine.ColorEng;
import com.example.engine.FontEng;
import com.example.engine.GraphicsInterface;
import com.example.engine.IFileStorage;
import com.example.utils.Vec2;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

public class JSON_HELPER {
    static boolean checkValidityOfJSONs = false;
    public static Dictionary<String, Long> hashPerFile;

    private final static String[] saveFiles ={
        "colors",
        "fonts",
        "powerUps",
        "levels"
    };
    private static void initializeHasherFile(JSONObject jo){
        long defaultFileHash = HashUtils.getHashForJSONObject("{}");
        for(String file : saveFiles){
            jo.put(file, defaultFileHash);
        }
    }
    public static void deserialize(IFileStorage fileStorage){
        hashPerFile = new Hashtable<>();

        String file_name = "hashPerFile.json";
        fileStorage.setRelativePath("security");
        System.out.println("Begin Reading "+file_name+" in "+fileStorage.getCurrentPath());

        JSONObject jo;
        try {
            String content = fileStorage.readModifiable(file_name);
            jo = new JSONObject(content);
            System.out.println(file_name+" Read succesfully");
        } catch (java.io.IOException e) {
            //File does not exist
            jo = new JSONObject();
            initializeHasherFile(jo);
        }
        for(String key : jo.keySet()){
            hashPerFile.put(key,jo.getLong(key));
        }
        saveCurrentHasher(fileStorage);
    }
    private static void saveCurrentHasher(IFileStorage fs){
        JSONObject jo = new JSONObject();
        Enumeration<String> keys = hashPerFile.keys();
        while(keys.hasMoreElements()) {
            String key = keys.nextElement();
            jo.put(key, hashPerFile.get(key));
        }
        String file_name = "hashPerFile.json";
        String path = fs.getCurrentPath();
        fs.setRelativePath("security");
        try {
            fs.writeModifiable(file_name, jo.toString());
            System.out.println("In path: "+ fs.getCurrentPath()+"\nJSON written to file "+file_name+".json succesfully");
        }catch(java.io.IOException e){
            e.printStackTrace();
        }
        fs.setRelativePath(path);
    }
    public static JSONArray toJSONModifiable(List<?> l) {
        JSONArray ja = new JSONArray();

        l.forEach(elem -> {
            if (elem instanceof mcd_Serializable) {
                ja.put(toJSONModifiable((mcd_Serializable) elem));
            }else if (elem instanceof Vec2) {
                ja.put(toJSONModifiable((Vec2) elem));
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
    public static <T extends mcd_Serializable> JSONObject toJSONModifiable(T obj){
        return obj.serialize();
    }
    public static JSONObject toJSONModifiable(Vec2 v){
        JSONObject jo = new JSONObject();
        jo.put("x",(double)v.x);
        jo.put("y",(double)v.y);
        return jo;
    }
    public static JSONObject toJSONModifiable(ColorEng clr, JSONObject jo){
        jo.put("color", clr.getColorAsInt());
        return jo;
    }
    public static JSONObject toJSONModifiable(FontEng f, JSONObject jo){
        jo.put("fontName", f.fileName);
        jo.put("fontSize", f.fontSize);
        return jo;
    }
    public static JSONObject pathToJSON(String path, String tag, JSONObject jo){
        String p = path;
        if(path == null){
            jo.put(tag,JSONObject.NULL);
            return jo;
        }
        int index = p.toLowerCase().indexOf("assets\\");

        if (index != -1) {
            // Keep everything after "assets/"
            p = p.substring(index + "assets\\".length());
        }
        jo.put(tag,p);
        return jo;
    }

    public static void toJSONModifiable(IFileStorage fileStorage, String file_name, JSONObject jo){
        if(hashPerFile==null){
            throw new RuntimeException("HashPerFile not initialized, deserialize must be called");
        }
        try {
            String json = jo.toString();
            long hash = HashUtils.getHashForJSONObject(json);
            hashPerFile.put(file_name, hash);
            System.out.println("Written file with hash:"+hash);
            fileStorage.writeModifiable(file_name+".json", json);
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
    public static JSONObject fromJSONModifiable(IFileStorage fileStorage, String file_name) {
        if(hashPerFile==null){
            throw new RuntimeException("HashPerFile not initialized, deserialize must be called");
        }
        System.out.println("Begin Reading "+file_name+" in "+fileStorage.getCurrentPath());
        try {
            if (!file_name.endsWith(".json")) {
                file_name += ".json";
            }

            String content = fileStorage.readModifiable(file_name);
            long hash = HashUtils.getHashForJSONObject(content);
            System.out.println("Read file with expected hash: "+hash);

            Long l = hashPerFile.get(file_name);
            if(checkValidityOfJSONs && (l==null || l != hash)){
                throw new RuntimeException("Incorrect Hash for file "+file_name+".json");
            }

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
            return g.createFont(f.getInt("fontSize"));
        }else{
            return g.createFont(f.getString("fontName"), f.getInt("fontSize"));
        }
    }
}
