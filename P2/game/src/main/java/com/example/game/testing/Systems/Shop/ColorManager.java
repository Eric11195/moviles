package com.example.game.testing.Systems.Shop;

import static com.example.game.testing.utils.JSON_HELPER.colorFromJSON;
import static com.example.game.testing.utils.JSON_HELPER.toJSONModifiable;

import com.example.engine.ColorEng;
import com.example.engine.IFileStorage;
import com.example.game.testing.utils.JSON_HELPER;
import com.example.game.testing.utils.mcd_Serializable;

import org.json.JSONObject;

import java.util.ArrayList;

public class ColorManager implements mcd_Serializable {
    public ColorManager(ArrayList<ColorEng> colors){
        if(colors.size() != Colors.COUNT.ordinal())
            throw new RuntimeException("Colors array must be same size as Colors.COUNT value");

        colorList = new ArrayList<>();
        for(int i = 0; i < Colors.COUNT.ordinal(); ++i){
            colorList.add(colors.get(i).clone());
        }
    }
    public static void setInstance(ColorManager mngr){
        _instance = mngr;
    }
    private static ColorManager _instance = null;
    public static ColorManager getInstance(){
        if(_instance == null)
            throw new RuntimeException("_instance must be initialized before this call");
        return _instance;
    }
    public static boolean hasInstance(){
        return _instance != null;
    }

    @Override
    public JSONObject serialize() {
        if(colorList.size() != Colors.COUNT.ordinal())
            throw new RuntimeException("Color list size must be Colors.COUNT value");

        JSONObject jo = new JSONObject();
        Colors[] enum_clr = Colors.values();
        final int[] i = {0};
        colorList.forEach((clr)->{
            JSONObject joColor = new JSONObject();
            JSON_HELPER.toJSONModifiable(clr, joColor);
            jo.put(enum_clr[i[0]].toString(), joColor);
            ++i[0];
        });
        return jo;
    }
    public static ColorManager deserialize(JSONObject jo){
        ArrayList<ColorEng> clrs = new ArrayList<>();
        Colors[] enum_clr = Colors.values();
        for(int i = 0; i < Colors.COUNT.ordinal(); ++i){
            if(!jo.has(enum_clr[i].toString()))
                clrs.add(new ColorEng(0xFFFFFFFF));
            clrs.add(colorFromJSON((JSONObject) jo.get(enum_clr[i].toString())));
        }
        return new ColorManager(clrs);
    }

    public static enum Colors{
        PLAYER_COLOR,
        LASER_COLOR,
        ZAPPER_COLOR,
        MISSILE_COLOR,
        COIN_COLOR,
        TEXT_COLOR,
        BACKGROUND_COLOR,
        BUTTONS_COLOR,
        COUNT
    }
    ArrayList<ColorEng> colorList;
    public final ColorEng getColor(ColorManager.Colors clr){
        return colorList.get(clr.ordinal());
    }
    public void setColor(ColorManager.Colors type, ColorEng clr, IFileStorage fs){
        ColorEng c = colorList.get(type.ordinal());
        c.r = clr.r;
        c.g = clr.g;
        c.b = clr.b;
        c.a = clr.a;
        storeChangesToFile(fs);
    }
    private void storeChangesToFile(IFileStorage fs){
        fs.setRelativePath("presets");
        JSON_HELPER.toJSONModifiable(fs, "colors",this.serialize());
    }
}
