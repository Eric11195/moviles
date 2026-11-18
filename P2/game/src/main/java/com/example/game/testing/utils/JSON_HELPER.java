package com.example.game.testing.utils;

import com.example.engine.ColorEng;
import com.example.engine.Engine;
import com.example.engine.FontEng;
import com.example.engine.GraphicsInterface;
import com.example.engine.IFileStorage;
import com.example.game.testing.Scenes.CorruptedSaveFileScene;
import com.example.utils.Vec2;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

public class JSON_HELPER {
    static boolean checkValidityOfJSONs = true;
    public static Dictionary<String, Long> hashPerFile;

    private final static String[] saveFiles = {
            "colors",
            "fonts",
            "powerUps",
            "levels",
            "skins",
            "bought",
            "rubies"
    };

    public static void initializeHasherDefault(JSONObject jo) {
        long defaultFileHash = HashUtils.getHashForJSONObject("{}");
        for (String file : saveFiles) {
            jo.put(file, defaultFileHash);
        }
    }

    static Engine engine;
    static IFileStorage fs;
    public static void deserialize(Engine eng, IFileStorage fileStorage) {
        hashPerFile = new Hashtable<>();

        engine = eng;
        fs = fileStorage;

        String file_name = "hashPerFile.json";
        fileStorage.setRelativePath("security");
        System.out.println("Begin Reading " + file_name + " in " + fileStorage.getCurrentPath());

        JSONObject jo = new JSONObject();
        try {
            String content = fileStorage.readModifiable(file_name);
            if (content != null && !content.isEmpty()) {
                jo = new JSONObject(content);
                System.out.println(file_name + " Read succesfully");
            } else {
                System.out.println(file_name + " not found, initializing empty {}");
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }

        if (jo.length() == 0) {
            initializeHasherDefault(jo);
        }

        for (String key : jo.keySet()) {
            hashPerFile.put(key, jo.getLong(key));
        }

        saveCurrentHasher(fileStorage);
    }

    public static void storeValuesFromJSON(JSONObject jo) {
        for (String key : jo.keySet()) {
            hashPerFile.put(key, jo.getLong(key));
        }
        saveCurrentHasher(fs);
    }

    private static void saveCurrentHasher(IFileStorage fs) {
        JSONObject hasherJson = new JSONObject();
        Enumeration<String> keys = hashPerFile.keys();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            hasherJson.put(key, hashPerFile.get(key));
        }
        String file_name = "hashPerFile.json";
        String path = fs.getCurrentPath();
        fs.setRelativePath("security");
        try {
            fs.writeModifiable(file_name, hasherJson.toString());
            System.out.println("In path: " + fs.getCurrentPath() + "\nJSON written to file " + file_name + " succesfully");
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        fs.setRelativePath(path);
    }

    public static JSONArray toJSONModifiable(List<?> l) {
        JSONArray ja = new JSONArray();

        l.forEach(elem -> {
            if (elem instanceof mcd_Serializable) {
                ja.put(toJSONModifiable((mcd_Serializable) elem));
            } else if (elem instanceof Vec2) {
                ja.put(toJSONModifiable((Vec2) elem));
            } else {
                throw new IllegalArgumentException(
                        "Unsupported type: " + elem.getClass().getName()
                );
            }
        });

        return ja;
    }

    public static <T extends mcd_Serializable> JSONObject toJSONModifiable(T obj) {
        return obj.serialize();
    }

    public static JSONObject toJSONModifiable(Vec2 v) {
        JSONObject jo = new JSONObject();
        jo.put("x", (double) v.x);
        jo.put("y", (double) v.y);
        return jo;
    }

    public static JSONObject toJSONModifiable(ColorEng clr, JSONObject jo) {
        jo.put("color", clr.getColorAsInt());
        return jo;
    }

    public static JSONObject toJSONModifiable(FontEng f, JSONObject jo) {
        jo.put("fontName", f.fileName);
        jo.put("fontSize", f.fontSize);
        return jo;
    }

    public static JSONObject pathToJSON(String path, String tag, JSONObject jo) {
        String p = path;
        if (path == null) {
            jo.put(tag, JSONObject.NULL);
            return jo;
        }
        int index = p.toLowerCase().indexOf("assets\\");

        if (index != -1) {
            p = p.substring(index + "assets\\".length());
        }
        jo.put(tag, p);
        return jo;
    }

    public static void toJSONModifiable(IFileStorage fileStorage, String file_name, JSONObject jo) {
        if (hashPerFile == null) {
            throw new RuntimeException("HashPerFile not initialized, deserialize must be called");
        }
        try {
            String key = file_name;
            if (key.endsWith(".json")) {
                key = key.substring(0, key.length() - 5);
            }

            String json = jo.toString();
            long hash = HashUtils.getHashForJSONObject(json);
            hashPerFile.put(key, hash);
            System.out.println("Written file with hash:" + hash);
            fileStorage.writeModifiable(key + ".json", json);
            System.out.println("In path: " + fileStorage.getCurrentPath() + "\nJSON written to file " + key + ".json succesfully");
            saveCurrentHasher(fileStorage);
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    public static JSONObject fromJSON(IFileStorage fileStorage, String file_name) {
        System.out.println("Begin Reading " + file_name + " in " + fileStorage.getCurrentPath());
        try {
            if (!file_name.endsWith(".json")) {
                file_name += ".json";
            }

            String content = fileStorage.read(file_name);
            if (content == null || content.isEmpty()) {
                return new JSONObject();
            }

            JSONObject jo = new JSONObject(content);
            System.out.println(file_name + " Read succesfully");
            return jo;
        } catch (java.io.IOException e) {
            System.out.println("Error while reading file " + file_name + " in " + fileStorage.getCurrentPath());
            e.printStackTrace();
        }

        return new JSONObject();
    }

    public static JSONObject fromJSONModifiable(IFileStorage fileStorage, String file_name) {
        if (hashPerFile == null) {
            throw new RuntimeException("HashPerFile not initialized, deserialize must be called");
        }
        System.out.println("Begin Reading " + file_name + " in " + fileStorage.getCurrentPath());
        try {
            String key = file_name;
            if (key.endsWith(".json")) {
                key = key.substring(0, key.length() - 5);
            }
            String actualFileName = key + ".json";

            String content = fileStorage.readModifiable(actualFileName);
            if (content == null || content.isEmpty()) {
                System.out.println("File " + actualFileName + " could not be opened, returning {}");
                return new JSONObject();
            }

            Long l = hashPerFile.get(key);
            long hash = HashUtils.getHashForJSONObject(content);
            System.out.println("Read file with expected hash: " + l + " but calculated: " + hash);

            if (checkValidityOfJSONs && (l != null && l != hash)) {
                if (engine != null) {
                    engine.pushScene(new CorruptedSaveFileScene(engine));
                }
                throw new RuntimeException("Invalid hash for file " + actualFileName + " should be " + hash + " but is " + l);
            }

            JSONObject jo = new JSONObject(content);
            System.out.println(actualFileName + " Read succesfully");
            return jo;
        } catch (java.io.IOException e) {
            System.out.println("Error while reading file " + file_name + " in " + fileStorage.getCurrentPath());
            e.printStackTrace();
        }

        return new JSONObject();
    }

    public static Vec2 vec2FromJSON(JSONObject v) {
        return new Vec2(
                (float) v.getDouble("x"),
                (float) v.getDouble("y")
        );
    }

    public static ColorEng colorFromJSON(JSONObject clr) {
        return new ColorEng(
                clr.getInt("color")
        );
    }

    public static FontEng fontFromJSON(GraphicsInterface g, JSONObject f) {
        if (f.isNull("fontName")) {
            return g.createFont(f.getInt("fontSize"));
        } else {
            return g.createFont(f.getString("fontName"), f.getInt("fontSize"));
        }
    }
}
