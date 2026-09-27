package com.example.desktopengine;

import com.example.engine.FontEng;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.io.InputStream;

public class FontDesktop implements FontEng {
    Font java_font;

    public FontDesktop(){
        java_font = new Font("Arial", Font.PLAIN, 16);
    }
    public FontDesktop(float size){
        java_font = new Font("Arial", Font.PLAIN, (int)size);
    }

    public FontDesktop(String file_name, float size) {
        InputStream is = getClass().getResourceAsStream("/fonts/" + file_name);
        try {
            java_font = Font.createFont(Font.TRUETYPE_FONT, is);
            setFontSize(size);
        } catch (FontFormatException | IOException exception) {
            exception.printStackTrace();
        }
    }
    public Font getFont(){
        return java_font;
    }
    @Override
    public void setFontSize(float new_size){
        java_font =
                java_font.deriveFont(new_size);
    }
}
