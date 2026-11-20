package com.example.engine;

/**
 * Abstract platform-independent representation of an image asset.
 */
public abstract class ImageEng {

    /** Relative path to the image asset file. */
    public final String path;

    /**
     * Constructs an ImageEng instance with specified asset path.
     *
     * @param path relative path to image asset file
     */
    protected ImageEng(String path) {
        this.path = path;
    }

    /**
     * Retrieves the image width in pixels.
     *
     * @return image width in pixels
     */
    public abstract int getWidth();

    /**
     * Retrieves the image height in pixels.
     *
     * @return image height in pixels
     */
    public abstract int getHeight();
}
