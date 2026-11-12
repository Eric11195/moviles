package com.example.game.testing.Systems.Shop;

import static com.example.game.testing.utils.JSON_HELPER.toJSONModifiable;

import com.example.engine.FontEng;
import com.example.engine.GraphicsInterface;
import com.example.engine.IFileStorage;
import com.example.game.testing.utils.JSON_HELPER;
import com.example.game.testing.utils.mcd_Serializable;

import org.json.JSONObject;

import java.util.ArrayList;

public class FontManager implements mcd_Serializable {
    public FontManager(ArrayList<FontEng> fonts){
        if(fonts.size() != Fonts.COUNT.ordinal())
            throw new RuntimeException("Fonts array must be same size as Fonts.COUNT value");

        fontList = new ArrayList<>();
        for(int i = 0; i < Fonts.COUNT.ordinal(); ++i){
            fontList.add(fonts.get(i));
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
            throw new RuntimeException("Font list size must be Fonts.COUNT value");

        JSONObject jo = new JSONObject();
        Fonts[] enum_font = Fonts.values();

        // Fixed: Replaced awkward lambda with a clean for-loop
        for(int i = 0; i < Fonts.COUNT.ordinal(); ++i) {
            FontEng font = fontList.get(i);
            if (font == null) {
                jo.put(enum_font[i].toString(), JSONObject.NULL);
            } else {
                JSONObject joFont = new JSONObject();
                toJSONModifiable(font, joFont);
                jo.put(enum_font[i].toString(), joFont);
            }
        }
        return jo;
    }

    public static FontManager deserialize(GraphicsInterface g, JSONObject jo){
        ArrayList<FontEng> fonts = new ArrayList<>();
        Fonts[] enum_font = Fonts.values();

        for(int i = 0; i < Fonts.COUNT.ordinal(); ++i){
            String key = enum_font[i].toString();
            // Fixed: Safely check for missing or null keys before extracting the object
            if (!jo.has(key) || jo.isNull(key)) {
                fonts.add(null);
            } else {
                fonts.add(JSON_HELPER.fontFromJSON(g, jo.getJSONObject(key)));
            }
        }
        return new FontManager(fonts);
    }

    public static enum Fonts{
        BUTTON_FONT,
        COIN_N_DISTANCE_COUNT,
        TITLE_FONT,
        COUNT
    }

    ArrayList<FontEng> fontList;

    public final FontEng getFont(FontManager.Fonts font){
        return fontList.get(font.ordinal());
    }

    public void setFont(IFileStorage fs, FontManager.Fonts type, FontEng font){
        // Fixed: Actually update the item in the list
        fontList.set(type.ordinal(), font);

        fs.setRelativePath("presets");
        JSON_HELPER.toJSONModifiable(fs,"fonts",this.serialize());
    }
}