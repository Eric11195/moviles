package com.example.utils;

import java.nio.file.FileSystems;

/**
 * Utility helper class for system paths and aspect ratio scaling calculations.
 */
public class Utils {

    /**
     * Retrieves the current working directory absolute path.
     *
     * @return current working directory path as a String
     */
    public static String getCurrentPath() {
        return FileSystems.getDefault().getPath("").toAbsolutePath().toString();
    }

    /**
     * Container class holding non-deforming aspect ratio scale and letterboxing offsets.
     */
    public static class NonDeformingAspectData {
        /** Uniform scale multiplier. */
        public float scale_mult;

        /** Horizontal letterbox offset. */
        public float offset_x;

        /** Vertical letterbox offset. */
        public float offset_y;
    }

    /**
     * Calculates uniform scaling factor and centering offsets to preserve aspect ratio when fitting game dimensions into window dimensions.
     *
     * @param width actual display window width
     * @param height actual display window height
     * @param gameWidth logical game width
     * @param gameHeight logical game height
     * @return scaling and offset data
     */
    public static NonDeformingAspectData getNonDeformingAspectData(
            float width,
            float height,
            float gameWidth,
            float gameHeight
    ) {

        NonDeformingAspectData data =
                new NonDeformingAspectData();

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
