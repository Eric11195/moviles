package com.example.engine;

public abstract class ImageEng{
    public final String path;
    protected ImageEng(String path){
        this.path = path;
    }
    public abstract int getWidth();
    public abstract int getHeight();
}
