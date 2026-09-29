package com.example.utils;

import java.nio.file.FileSystems;

public class Utils {
    public static String getCurrentPath(){
        return FileSystems.getDefault().getPath("").toAbsolutePath().toString();
    }
    public static class NonDeformingAspectData {
        public float scale_mult;
        public float offset_x,offset_y;
    }
    public static NonDeformingAspectData getNonDeformingAspectData(
            float width,
            float height) {

        NonDeformingAspectData data =
                new NonDeformingAspectData();

        float gameWidth = 600;
        float gameHeight = 400;

        float scaleX = width / gameWidth;
        float scaleY = height / gameHeight;

        data.scale_mult = Math.min(scaleX, scaleY);

        float scaledWidth = gameWidth * data.scale_mult;
        float scaledHeight = gameHeight * data.scale_mult;

        data.offset_x = (width - scaledWidth) / 2.0f;
        data.offset_y = (height - scaledHeight) / 2.0f;

        return data;
    }
}