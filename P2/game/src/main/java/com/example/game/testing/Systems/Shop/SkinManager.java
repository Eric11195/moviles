package com.example.game.testing.Systems.Shop;

import static com.example.game.testing.utils.JSON_HELPER.colorFromJSON;
import static com.example.game.testing.utils.JSON_HELPER.toJSON;

import com.example.engine.ColorEng;
import com.example.engine.GraphicsInterface;
import com.example.engine.IFileStorage;
import com.example.engine.ImageEng;
import com.example.game.testing.utils.JSON_HELPER;
import com.example.game.testing.utils.mcd_Serializable;

import org.json.JSONObject;

import java.util.ArrayList;

public class SkinManager implements mcd_Serializable {
    public SkinManager(ArrayList<ImageEng> images){
        if(images.size() != Images.COUNT.ordinal())
            throw new RuntimeException("Colors array must be same size as Colors.COUNT value");

        imageList = new ArrayList<>();
        for(int i = 0; i < Images.COUNT.ordinal(); ++i){
            imageList.add(images.get(i));
        }
    }
    public static void setInstance(SkinManager mngr){
        _instance = mngr;
    }
    static SkinManager _instance = null;
    public static boolean hasInstance(){
        return _instance != null;
    }
    public static SkinManager getInstance(){
        if(_instance == null)
            throw new RuntimeException("Instance was null, you must initialize this first");
        return _instance;
    }

    @Override
    public JSONObject serialize() {
        if(imageList.size() != Images.COUNT.ordinal())
            throw new RuntimeException("Color list size must be Colors.COUNT value");

        JSONObject jo = new JSONObject();
        Images[] enum_image = Images.values();
        for(int i = 0; i < Images.COUNT.ordinal(); ++i){
            ImageEng img = imageList.get(i);
            JSON_HELPER.pathToJSON(
                img == null ? null : img.path,
                enum_image[i].toString(),
                jo
            );
        }
        return jo;
    }

    public static SkinManager deserialize(GraphicsInterface g, JSONObject jo){
        ArrayList<ImageEng> imgs = new ArrayList<>();
        Images[] enum_img = Images.values();
        for(int i = 0; i < Images.COUNT.ordinal(); ++i){
            String key = enum_img[i].toString();
            if(!jo.has(key) || jo.isNull(key)){
                imgs.add(null);
            } else {
                imgs.add(
                        g.createImage(
                                jo.getString(key) // Fixed: removed the redundant outer jo.getString() wrapper
                        )
                );
            }
        }
        return new SkinManager(imgs);
    }

    public static enum Images {
        PLAYER,
        MISSILE,
        LASER,
        WARNINGS,
        ZAPPER,
        COINS,
        BUTTON,
        COUNT
    }

    ArrayList<ImageEng> imageList;

    public ImageEng getImage(Images clr){
        return imageList.get(clr.ordinal());
    }

    public void setImage(Images type, ImageEng img, IFileStorage fs){
        imageList.set(type.ordinal(), img); // Fixed: actually update the list instead of a local variable
        fs.setRelativePath("presets");
        JSON_HELPER.toJSON(fs, "skins", this.serialize());
    }
}