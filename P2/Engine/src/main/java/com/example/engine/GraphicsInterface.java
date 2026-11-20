package com.example.engine;

import com.example.utils.Vec2;

/**
 * Platform-agnostic 2D graphics rendering interface.
 */
public interface GraphicsInterface {

    /**
     * Draws a sub-region of an image to a destination rectangle.
     *
     * @param img image asset
     * @param src_pos top-left source position
     * @param src_size source sub-region size
     * @param dst_pos top-left destination position
     * @param dst_size destination size
     */
    public void drawImage(ImageEng img, Vec2 src_pos, Vec2 src_size, Vec2 dst_pos, Vec2 dst_size);

    /**
     * Draws an image scaled to a destination rectangle.
     *
     * @param img image asset
     * @param pos top-left destination position
     * @param size destination size
     */
    public void drawImage(ImageEng img, Vec2 pos, Vec2 size);

    /**
     * Draws an image at its unscaled size at a destination position.
     *
     * @param img image asset
     * @param pos top-left destination position
     */
    public void drawImage(ImageEng img, Vec2 pos);

    /**
     * Sets the logical window size for scaling coordinate space.
     *
     * @param width logical width
     * @param height logical height
     */
    public void setWindowLogicSize(int width, int height);

    /**
     * Clears the screen canvas with a background color.
     *
     * @param color clear color
     */
    public void clear(ColorEng color);

    /**
     * Sets the current active draw color.
     *
     * @param color draw color
     */
    public void setColor(ColorEng color);

    /**
     * Sets stroke line width in pixels.
     *
     * @param pxWidth line width in pixels
     */
    public void setStrokeWidth(int pxWidth);

    /**
     * Draws a rectangle.
     *
     * @param pos top-left position
     * @param size width and height
     * @param fill true to fill, false to stroke outline
     */
    public void drawRectangle(Vec2 pos, Vec2 size, boolean fill);

    /**
     * Draws a rounded rectangle.
     *
     * @param pos top-left position
     * @param size width and height
     * @param arc corner arc radius
     * @param fill true to fill, false to stroke outline
     */
    public void drawRoundRectangle(Vec2 pos, Vec2 size, float arc, boolean fill);

    /**
     * Draws a circle.
     *
     * @param centerPos center position
     * @param rad radius
     * @param fill true to fill, false to stroke outline
     */
    public void drawCircle(Vec2 centerPos, float rad, boolean fill);

    /**
     * Draws a line segment between two points.
     *
     * @param startPoint starting coordinate
     * @param enPoint ending coordinate
     */
    public void drawLine(Vec2 startPoint, Vec2 enPoint);

    /**
     * Draws an N-sided regular polygon.
     *
     * @param n number of sides
     * @param centerPos center coordinate
     * @param rad radius
     * @param fill true to fill, false to stroke outline
     */
    public void drawNSidePolygon(int n, Vec2 centerPos, float rad, boolean fill);

    /**
     * Sets the active font.
     *
     * @param f FontEng instance
     */
    public void setFont(FontEng f);

    /**
     * Draws text centered at center_pos.
     *
     * @param text string to render
     * @param center_pos center coordinate
     */
    public void drawText(String text, Vec2 center_pos);

    /**
     * Renders text fitted inside a bounding rectangle (x0y0Pos, size).
     *
     * @param text string to render
     * @param x0y0Pos top-left position
     * @param size bounding box dimensions
     */
    public void drawText(String text, Vec2 x0y0Pos, Vec2 size);

    /**
     * Creates an image from an asset path.
     *
     * @param path relative path to image file
     * @return created ImageEng instance
     */
    public ImageEng createImage(String path);

    /**
     * Creates a font from an asset path and point size.
     *
     * @param path relative path to font file
     * @param size point size
     * @return created FontEng instance
     */
    public FontEng createFont(String path, int size);

    /**
     * Creates a default font with specified point size.
     *
     * @param size point size
     * @return created FontEng instance
     */
    public FontEng createFont(int size);

    /**
     * Sets active font size.
     *
     * @param size point size
     */
    public void setFontSize(int size);

    /**
     * Translates the canvas origin by (x, y).
     *
     * @param x horizontal translation offset
     * @param y vertical translation offset
     */
    public void translate(float x, float y);

    /**
     * Scales the canvas coordinate space by (x, y).
     *
     * @param x horizontal scale factor
     * @param y vertical scale factor
     */
    public void scale(float x, float y);

    /**
     * Rotates the canvas around a pivot point (x, y) by rotationDegrees.
     *
     * @param x pivot point x
     * @param y pivot point y
     * @param rotationDegrees rotation angle in degrees
     */
    public void rotate(float x, float y, float rotationDegrees);

    /**
     * Saves current graphics transform state.
     */
    public void save();

    /**
     * Restores previous graphics transform state.
     */
    public void restore();

    /**
     * Prepares the graphics context for rendering a frame.
     *
     * @return true if context is ready, false otherwise
     */
    public boolean prepareFrame();

    /**
     * Ends rendering for current frame and presents to screen.
     */
    public void endFrame();

    /**
     * Returns actual canvas width in pixels.
     *
     * @return width in pixels
     */
    int getWidth();

    /**
     * Returns actual canvas height in pixels.
     *
     * @return height in pixels
     */
    int getHeight();

    /**
     * Configures viewport scaling multiplier and offsets.
     *
     * @param scale_mult uniform scale factor
     * @param offset_x x offset
     * @param offset_y y offset
     */
    void setViewport(float scale_mult, float offset_x, float offset_y);

    /**
     * Converts a window pixel coordinate to logical game coordinate space.
     *
     * @param pos point in screen pixel space
     * @return converted point in logical coordinate space
     */
    Vec2 getPointInWindowPos(Vec2 pos);

    /**
     * Resets clipping area to full canvas.
     */
    void resetClipping();

    /**
     * Sets rectangular clipping region.
     *
     * @param x top-left x
     * @param y top-left y
     * @param width clipping width
     * @param height clipping height
     */
    void clipRect(int x, int y, int width, int height);
}
