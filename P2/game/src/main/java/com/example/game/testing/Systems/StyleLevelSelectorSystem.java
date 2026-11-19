package com.example.game.testing.Systems;

import com.example.engine.ColorEng;

import org.json.JSONObject;

public class StyleLevelSelectorSystem {
    private static ColorEng backgroundColor1;
    private static ColorEng backgroundColor2;
    private static ColorEng buttonLockedColor;
    private static ColorEng buttonUnLockedColor;
    private static ColorEng buttonCompleteColor;


    public static ColorEng fromJsonToColor(JSONObject object)
    {
        return new ColorEng(object.getInt("r"), object.getInt("g"), object.getInt("b"), object.getInt("a"));
    }
    public static void setBackgroundColor(JSONObject object)
    {
         JSONObject background1 = object.getJSONObject("background1");
         JSONObject background2 = object.getJSONObject("background2");

         backgroundColor1 = fromJsonToColor(background1);
         backgroundColor2 = fromJsonToColor(background2);
    }
    public static  void setColors(JSONObject object)
    {
        JSONObject locked = object.getJSONObject("locked");
        JSONObject unlocked = object.getJSONObject("unlocked");
        JSONObject complete = object.getJSONObject("complete");

        buttonLockedColor = fromJsonToColor(locked);
        buttonUnLockedColor = fromJsonToColor(unlocked);
        buttonCompleteColor = fromJsonToColor(complete);
    }
    public static ColorEng getBackgroundColor1()
    {
        return backgroundColor1;
    }
    public static ColorEng getBackgroundColor2()
    {
        return backgroundColor2;
    }
    public static ColorEng getButtonLockedColor()
    {
        return  buttonLockedColor;
    }
    public static ColorEng getButtonUnLockedColor()
    {
        return  buttonUnLockedColor;
    }
    public static ColorEng getButtonCompleteColor()
    {
        return  buttonCompleteColor;
    }

}
