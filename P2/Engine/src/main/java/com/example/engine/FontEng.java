package com.example.engine;

public abstract class FontEng {
    public final String fileName;
    public int fontSize;
    public FontEng(String fileName, int fontSize){
        this.fileName = fileName;
        this.fontSize = fontSize;
    }
    protected void setFontSize(int new_size){
        this.fontSize = new_size;
    }
}
