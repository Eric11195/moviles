package com.example.desktopengine;

import com.example.engine.FontEng;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class FontDesktop extends FontEng {
    Font java_font;

    public FontDesktop(){
        super(null, 16);
        java_font = new Font("Arial", Font.PLAIN, this.fontSize);
    }
    public FontDesktop(int size){
        super(null,size);
        java_font = new Font("Arial", Font.PLAIN, this.fontSize);
    }

    public FontDesktop(String file_name, int size) {
        super(file_name,size);
        try {
            // Read directly from the file system path
            InputStream is = new FileInputStream(new File("Assets/fonts/" + file_name));
            java_font = Font.createFont(Font.TRUETYPE_FONT, is);
            setFontSize(size);
        } catch (FontFormatException | IOException exception) {
            exception.printStackTrace();
            throw new RuntimeException("Font could not be loaded");
        }
    }
    public Font getFont(){
        return java_font;
    }
    @Override
    public void setFontSize(int new_size){
        super.setFontSize(new_size);
        java_font =
                java_font.deriveFont((float)this.fontSize);
    }
}
