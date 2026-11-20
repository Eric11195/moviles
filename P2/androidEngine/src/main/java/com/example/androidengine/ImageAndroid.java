package com.example.androidengine;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.engine.ImageEng;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Android implementation of {@link ImageEng} encapsulating Android {@link Bitmap}.
 */
public class ImageAndroid extends ImageEng {

    /** Android Bitmap asset instance. */
    private Bitmap bm;

    /**
     * Constructs ImageAndroid loading Bitmap asset from assets directory.
     *
     * @param context Android Context
     * @param path relative path to image asset file
     */
    public ImageAndroid(Context context, String path) {
        super(path);
        try (InputStream raw = context.getAssets().open(path);
             BufferedInputStream input = new BufferedInputStream(raw)) {

            bm = BitmapFactory.decodeStream(input);

            if (bm == null) {
                throw new RuntimeException("Failed to decode image: " + path);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load asset: " + path, e);
        }
    }

    /**
     * Returns the underlying Android Bitmap.
     *
     * @return Bitmap instance
     */
    public final Bitmap getBitmap() {
        return bm;
    }

    /**
     * Retrieves the image width in pixels.
     *
     * @return width in pixels
     */
    @Override
    public final int getWidth() {
        return bm.getWidth();
    }

    /**
     * Retrieves the image height in pixels.
     *
     * @return height in pixels
     */
    @Override
    public final int getHeight() {
        return bm.getHeight();
    }
}
