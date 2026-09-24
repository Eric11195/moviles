package com.example.desktopengine;

import com.example.engine.EngImage;

import java.awt.Image;
import java.awt.Toolkit;

public class ImageDesktop implements EngImage {
    Image img;

    public ImageDesktop(String image_path){
        img = Toolkit.getDefaultToolkit().getImage(image_path);
    }
    @Override
    public int getWidth() {
        return 0;
    }

    @Override
    public int getHeight() {
        return 0;
    }
    Image getImage(){
        return img;
    }
}
