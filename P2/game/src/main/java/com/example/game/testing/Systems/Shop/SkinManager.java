package com.example.game.testing.Systems.Shop;

import static com.example.game.testing.utils.JSON_HELPER.colorFromJSON;
import static com.example.game.testing.utils.JSON_HELPER.toJSON;

import com.example.engine.ColorEng;
import com.example.engine.GraphicsInterface;
import com.example.engine.ImageEng;
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
        final int[] i = {0};
        imageList.forEach((img)->{
            jo.put(
                enum_image[0].toString(),
                img.path
            );
            ++i[0];
        });
        return jo;
    }
    public static SkinManager deserialize(GraphicsInterface g, JSONObject jo){
        ArrayList<ImageEng> imgs = new ArrayList<>();
        Images[] enum_img = Images.values();
        for(int i = 0; i < Images.COUNT.ordinal(); ++i){
            if(!jo.has(enum_img[i].toString()) || jo.isNull(enum_img[i].toString())){
                imgs.add(null);
                continue;
            }else {
                imgs.add(
                    g.createImage(
                        jo.getString(
                            jo.getString(enum_img[i].toString())
                        )
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
        LASER_WARNINGS,
        WARNINGS,
        ZAPPER_HEAD,
        ZAPPER_POINT,
        COINS,
        COUNT
    }
    ArrayList<ImageEng> imageList;
    public ImageEng getImage(Images clr){
        return imageList.get(clr.ordinal());
    }
    public void setImage(Images type, ImageEng img){
        ImageEng imgEng = imageList.get(type.ordinal());
        imgEng = img;
    }
}
