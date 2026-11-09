package com.example.game.testing.Systems.Shop;

import static com.example.game.testing.utils.JSON_HELPER.colorFromJSON;
import static com.example.game.testing.utils.JSON_HELPER.toJSON;

import com.example.engine.ColorEng;
import com.example.engine.FontEng;
import com.example.engine.GraphicsInterface;
import com.example.engine.IFileStorage;
import com.example.game.testing.utils.JSON_HELPER;
import com.example.game.testing.utils.mcd_Serializable;

import org.json.JSONObject;

import java.util.ArrayList;

public class FontManager implements mcd_Serializable {
    public FontManager(ArrayList<FontEng> colors){
        if(colors.size() != Fonts.COUNT.ordinal())
            throw new RuntimeException("Colors array must be same size as Colors.COUNT value");

        fontList = new ArrayList<>();
        for(int i = 0; i < Fonts.COUNT.ordinal(); ++i){
            fontList.add(colors.get(i));
        }
    }
    public static void setInstance(FontManager mngr){
        _instance = mngr;
    }
    private static FontManager _instance = null;
    public static FontManager getInstance(){
        if(_instance == null)
            throw new RuntimeException("_instance must be initialized before this call");
        return _instance;
    }
    public static boolean hasInstance(){
        return _instance != null;
    }

    @Override
    public JSONObject serialize() {
        if(fontList.size() != Fonts.COUNT.ordinal())
            throw new RuntimeException("Color list size must be Colors.COUNT value");

        JSONObject jo = new JSONObject();
        Fonts[] enum_clr = Fonts.values();
        final int[] i = {0};
        fontList.forEach((clr)->{
            JSONObject joColor = new JSONObject();
            toJSON(clr, joColor);
            jo.put(enum_clr[i[0]].toString(), joColor);
            ++i[0];
        });
        return jo;
    }
    public static FontManager deserialize(GraphicsInterface g, JSONObject jo){
        ArrayList<FontEng> clrs = new ArrayList<>();
        Fonts[] enum_clr = Fonts.values();
        for(int i = 0; i < Fonts.COUNT.ordinal(); ++i){
            clrs.add(JSON_HELPER.fontFromJSON(g,jo.getJSONObject(enum_clr[i].toString())));
        }
        return new FontManager(clrs);
    }

    public static enum Fonts{
        BUTTON_FONT,
        COIN_N_DISTANCE_COUNT,
        TITLE_FONT,
        COUNT
    }
    ArrayList<FontEng> fontList;
    public final FontEng getFont(FontManager.Fonts clr){
        return fontList.get(clr.ordinal());
    }
    public void setFont(IFileStorage fs, FontManager.Fonts type, FontEng clr){
        FontEng c = fontList.get(type.ordinal());
        c = clr;
        fs.setRelativePath("presets");
        JSON_HELPER.toJSON(fs,"fonts",this.serialize());
    }
}
