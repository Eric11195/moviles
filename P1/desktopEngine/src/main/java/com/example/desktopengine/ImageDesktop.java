package com.example.desktopengine;

import com.example.engine.ImageEng;
import com.example.utils.Utils;

import java.awt.Frame;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.util.logging.Logger;

public class ImageDesktop implements ImageEng {
    Image img;

    public ImageDesktop(String image_path){
        img = Toolkit.getDefaultToolkit().getImage(image_path);
        String output ="Searching for image in: "
                + Utils.getCurrentPath()
                + image_path;
        System.out.println(output);
        MediaTracker tracker = new MediaTracker(new Frame());
        tracker.addImage(img, 0);
        try {
            tracker.waitForID(0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
