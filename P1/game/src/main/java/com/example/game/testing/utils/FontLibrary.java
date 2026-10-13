package com.example.game.testing.utils;

import com.example.engine.FontEng;
import com.example.engine.GraphicsInterface;

import java.util.ArrayList;

public class FontLibrary {
    public enum FontName{
        STANDARD(0),
        HEADER(1),
        SUBHEADER(2);


        public final int value;

        FontName(int value) {
            this.value = value;
        }
    }
    public static ArrayList<FontEng> fonts = new ArrayList<>();
    public static void createFont(GraphicsInterface g, FontName name, float size){
        fonts.add(name.value,g.createFont(size));
    }
    public static void createFont(GraphicsInterface g, FontName name, float size, String path){
        fonts.add(name.value,g.createFont(path, size));
    }

    public static FontEng getFont(FontName name){
        return fonts.get(name.value);
    }
}
