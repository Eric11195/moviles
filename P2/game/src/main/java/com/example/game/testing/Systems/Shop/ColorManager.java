package com.example.game.testing.Systems.Shop;

import static com.example.game.testing.utils.JSON_HELPER.colorFromJSON;
import static com.example.game.testing.utils.JSON_HELPER.toJSON;

import com.example.engine.ColorEng;
import com.example.game.testing.utils.mcd_Serializable;

import org.json.JSONObject;

import java.awt.Color;
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

    @Override
    public JSONObject serialize() {
        if(colorList.size() != Colors.COUNT.ordinal())
            throw new RuntimeException("Color list size must be Colors.COUNT value");

        JSONObject jo = new JSONObject();
        Colors[] enum_clr = Colors.values();
        final int[] i = {0};
        colorList.forEach((clr)->{
            JSONObject joColor = new JSONObject();
            toJSON(clr, joColor);
            jo.put(enum_clr[i[0]].toString(), joColor);
            ++i[0];
        });
        return jo;
    }
    public static ColorManager deserialize(JSONObject jo){
        ArrayList<ColorEng> clrs = new ArrayList<>();
        Colors[] enum_clr = Colors.values();
        for(int i = 0; i < Colors.COUNT.ordinal(); ++i){
            clrs.add(colorFromJSON((JSONObject) jo.get(enum_clr[i].toString())));
        }
        return new ColorManager(clrs);
    }

    public static enum Colors{
        TEXT_COLOR,
        BACKGROUND_COLOR,
        BUTTONS_COLOR,
        COUNT
    }
    ArrayList<ColorEng> colorList;
    public ColorEng getColor(ColorManager.Colors clr){
        return colorList.get(clr.ordinal());
    }
}
