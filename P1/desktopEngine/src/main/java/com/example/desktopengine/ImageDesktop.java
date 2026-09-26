package com.example.desktopengine;

import com.example.engine.ImageEng;
import com.example.utils.Utils;

import java.awt.Frame;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

public class ImageDesktop implements ImageEng {
    Image img;

    public ImageDesktop(String image_path){
        String fullPath = Path.of(
                Utils.getCurrentPath(),
                image_path
        ).toAbsolutePath().toString();

        System.out.println("Searching for image in: " + fullPath);

        try {
            img = ImageIO.read(new File(fullPath));
        } catch (IOException e) {
            throw new RuntimeException(
                    "Could not load image: " + fullPath,
                    e
            );
        }

        if (img == null) {
            throw new RuntimeException(
                    "File is not a supported image: " + fullPath
            );
        }
    }
    @Override
    public int getWidth() {
        return img.getWidth(null);
    }

    @Override
    public int getHeight() {
        return img.getHeight(null);
    }
    Image getImage(){
        return img;
    }
}
