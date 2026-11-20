package com.example.androidengine;

import static com.example.utils.Utils.getNonDeformingAspectData;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.engine.ColorEng;
import com.example.engine.FontEng;
import com.example.engine.ImageEng;
import com.example.engine.GraphicsInterface;
import com.example.utils.Vec2;
import com.google.android.libraries.ads.mobile.sdk.banner.AdView;

/**
 * Android implementation of {@link GraphicsInterface} rendering onto an Android SurfaceView Canvas.
 */
public class GraphicsAndroid implements GraphicsInterface {

    /** SurfaceView view instance. */
    private final SurfaceView surf;

    /** SurfaceHolder managing surface canvas lock. */
    private SurfaceHolder surface = null;

    /** Active Android Canvas during rendering. */
    private Canvas can = null;

    /** Android Paint instance for styling shapes and text. */
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    /** Active font. */
    private FontAndroid font = new FontAndroid();

    /** Hosting AppCompatActivity instance. */
    private final AppCompatActivity activity;

    /** AdView banner instance. */
    AdView v;

    /**
     * Constructs GraphicsAndroid initializing SurfaceView callback and logic dimensions.
     *
     * @param activity hosting AppCompatActivity
     * @param screen_id SurfaceView view ID
     * @param add_id AdView view ID
     * @param width logical width
     * @param height logical height
     */
    public GraphicsAndroid(AppCompatActivity activity, int screen_id, int add_id, int width, int height) {
        this.activity = activity;
        assert(surface == null);
        assert(can == null);
        this._logicWindowHeight = height;
        this._logicWindowWidth = width;
        v = activity.findViewById(add_id);
        surf = activity.findViewById(screen_id);
        surface = surf.getHolder();
        surface.addCallback(
                new SurfaceHolder.Callback() {

                    @Override
                    public void surfaceCreated(SurfaceHolder holder) {
                    }

                    @Override
                    public void surfaceChanged(
                            SurfaceHolder holder,
                            int format,
                            int width,
                            int height) {
                        com.example.utils.Utils.NonDeformingAspectData data = getNonDeformingAspectData(width, height, _logicWindowWidth, _logicWindowHeight);
                        setViewport(data.scale_mult, data.offset_x, data.offset_y);
                    }

                    @Override
                    public void surfaceDestroyed(SurfaceHolder holder) {
                    }
                }
        );
    }

    /**
     * Retrieves actual width of the SurfaceView in pixels.
     *
     * @return surface width
     */
    @Override
    public int getWidth() {
        return surf.getWidth();
    }

    /**
     * Retrieves actual height of the SurfaceView in pixels.
     *
     * @return surface height
     */
    @Override
    public int getHeight() {
        return surf.getHeight();
    }

    /** Scale multiplier for viewport letterboxing. */
    float scaleMult;

    /** Horizontal offset for viewport letterboxing. */
    float offsetX;

    /** Vertical offset for viewport letterboxing. */
    float offsetY;

    /**
     * Configures viewport matrix scaling and letterbox offsets.
     *
     * @param scale_mult uniform scale factor
     * @param offset_x x letterbox offset
     * @param offset_y y letterbox offset
     */
    @Override
    public void setViewport(float scale_mult, float offset_x, float offset_y) {
        scaleMult = scale_mult;
        offsetX = offset_x;
        offsetY = offset_y;

        viewportMatrix.reset();

        viewportMatrix.setScale(scaleMult, scaleMult);
        viewportMatrix.postTranslate(offsetX, offsetY);

        calculateInverseMatrix();
    }

    /**
     * Generates a Path for an N-sided regular polygon.
     *
     * @param n number of sides
     * @param center_pos center coordinate
     * @param radius polygon radius
     * @return Path object representing polygon
     */
    private Path getNSidePolygonPath(int n, Vec2 center_pos, float radius) {
        Path nSidePolygonPath = new Path();
        for (int i = 0; i < n; i++) {
            float angle = (float) (2 * i * Math.PI / n);
            float xPoint = (float) (center_pos.x + radius * Math.cos(angle));
            float yPoint = (float) (center_pos.y + radius * Math.sin(angle));
            if (i == 0) {
                nSidePolygonPath.moveTo(xPoint, yPoint);
            } else {
                nSidePolygonPath.lineTo(xPoint, yPoint);
            }
        }
        nSidePolygonPath.close();
        return nSidePolygonPath;
    }

    /** Viewport transformation matrix. */
    private final Matrix viewportMatrix = new Matrix();

    /** Inverse viewport transformation matrix. */
    private final Matrix inverseViewportMatrix = new Matrix();

    /**
     * Locks canvas and initializes viewport rendering.
     *
     * @return true if canvas locked successfully, false otherwise
     */
    public boolean startRender() {
        assert(can == null);

        can = surface.lockCanvas();

        if (can == null) {
            Log.d("DRAW", "Canvas is NULL!");
            return false;
        }

        can.drawColor(clearColor.getColorAsInt());
        can.setMatrix(viewportMatrix);

        return true;
    }

    /**
     * Creates an ImageAndroid instance from an asset path.
     *
     * @param path relative path to image asset
     * @return created ImageEng instance
     */
    @Override
    public ImageEng createImage(String path) {
        return new ImageAndroid(activity, path);
    }

    /**
     * Creates a FontAndroid instance from path and size.
     *
     * @param path relative path to font asset
     * @param size font point size
     * @return created FontEng instance
     */
    @Override
    public FontEng createFont(String path, int size) {
        return new FontAndroid(activity, path, size, false);
    }

    /**
     * Creates a default FontAndroid instance of specified size.
     *
     * @param size font point size
     * @return created FontEng instance
     */
    @Override
    public FontEng createFont(int size) {
        return new FontAndroid(size, false);
    }

    /**
     * Sets active font size.
     *
     * @param size font size in points
     */
    @Override
    public void setFontSize(int size) {
        font.setFontSize(size);
    }

    /**
     * Translates current canvas transform matrix.
     *
     * @param x horizontal translation
     * @param y vertical translation
     */
    @Override
    public void translate(float x, float y) {
        can.translate(x, y);
    }

    /**
     * Scales current canvas transform matrix.
     *
     * @param x horizontal scale factor
     * @param y vertical scale factor
     */
    @Override
    public void scale(float x, float y) {
        can.scale(x, y);
    }

    /**
     * Rotates current canvas transform matrix around a pivot point.
     *
     * @param x pivot point x
     * @param y pivot point y
     * @param rotationDegrees rotation angle in degrees
     */
    @Override
    public void rotate(float x, float y, float rotationDegrees) {
        can.rotate(rotationDegrees, x, y);
    }

    /**
     * Saves canvas state onto matrix stack.
     */
    @Override
    public void save() {
        can.save();
    }

    /**
     * Restores previous canvas state from matrix stack.
     */
    @Override
    public void restore() {
        can.restore();
    }

    /**
     * Prepares frame canvas and clears screen with clear color.
     *
     * @return true if canvas locked, false otherwise
     */
    @Override
    public boolean prepareFrame() {
        assert(can == null);

        can = surface.lockCanvas();

        if (can == null) {
            Log.d("DRAW", "Canvas is NULL!");
            return false;
        }

        can.drawColor(clearColor.getColorAsInt());
        can.setMatrix(viewportMatrix);
        resetClipping();

        return true;
    }

    /**
     * Unlocks canvas and posts rendered frame to surface.
     */
    @Override
    public void endFrame() {
        assert(can != null);
        surface.unlockCanvasAndPost(can);
        can = null;
    }

    /**
     * Draws a sub-region of an image to a destination rectangle.
     *
     * @param img image asset
     * @param src_pos source top-left coordinate
     * @param src_size source width and height
     * @param dst_pos destination top-left coordinate
     * @param dst_size destination width and height
     */
    @Override
    public void drawImage(ImageEng img, Vec2 src_pos, Vec2 src_size, Vec2 dst_pos, Vec2 dst_size) {
        assert(can != null);
        assert(img != null);
        setStyle(true);
        ImageAndroid _img = (ImageAndroid) img;
        can.drawBitmap(_img.getBitmap(), new Rect((int) src_pos.x, (int) src_pos.y, (int) (src_pos.x + src_size.x), (int) (src_pos.y + src_size.y)), new Rect((int) dst_pos.x, (int) dst_pos.y, (int) (dst_pos.x + dst_size.x), (int) (dst_pos.y + dst_size.y)), paint);
    }

    /**
     * Draws entire image scaled to specified destination rectangle.
     *
     * @param img image asset
     * @param pos destination top-left coordinate
     * @param size destination width and height
     */
    @Override
    public void drawImage(ImageEng img, Vec2 pos, Vec2 size) {
        drawImage(
                img,
                new Vec2(0, 0), new Vec2(img.getWidth(), img.getHeight()),
                pos, size
        );
    }

    /**
     * Draws image at original unscaled size at destination position.
     *
     * @param img image asset
     * @param pos destination top-left coordinate
     */
    @Override
    public void drawImage(ImageEng img, Vec2 pos) {
        drawImage(img, pos, new Vec2(img.getWidth(), img.getHeight()));
    }

    /** Logical window width. */
    private int _logicWindowWidth;

    /** Logical window height. */
    private int _logicWindowHeight;

    /**
     * Configures logical window size for scaling.
     *
     * @param width logical width
     * @param height logical height
     */
    @Override
    public void setWindowLogicSize(int width, int height) {
        _logicWindowHeight = height;
        _logicWindowWidth = width;
    }

    /** Canvas clear color. */
    ColorEng clearColor = new ColorEng(255, 255, 255, 255);

    /**
     * Clears canvas with background color.
     *
     * @param color clear color
     */
    @Override
    public void clear(ColorEng color) {
        assert(can != null);
        clearColor = color;
        can.drawColor(color.getColorAsInt());
    }

    /**
     * Sets current draw color.
     *
     * @param color draw color
     */
    @Override
    public void setColor(ColorEng color) {
        assert(can != null);
        if (color == null) {
            color = new ColorEng(255, 255, 255, 255);
        }
        paint.setColor(color.getColorAsInt());
    }

    /**
     * Sets stroke line width.
     *
     * @param pxWidth width in pixels
     */
    @Override
    public void setStrokeWidth(int pxWidth) {
        assert(can != null);
        paint.setStrokeWidth(pxWidth);
    }

    /**
     * Sets Paint style (FILL vs STROKE).
     *
     * @param fill true for FILL, false for STROKE
     */
    private void setStyle(boolean fill) {
        paint.setStyle(fill ? Paint.Style.FILL : Paint.Style.STROKE);
    }

    /**
     * Draws a rectangle.
     *
     * @param pos top-left position
     * @param size width and height
     * @param fill true to fill, false to stroke
     */
    @Override
    public void drawRectangle(Vec2 pos, Vec2 size, boolean fill) {
        assert(can != null);
        setStyle(fill);
        can.drawRect(pos.x, pos.y, pos.x + size.x, pos.y + size.y, paint);
    }

    /**
     * Draws a rounded rectangle.
     *
     * @param pos top-left position
     * @param size width and height
     * @param arc corner arc radius
     * @param fill true to fill, false to stroke
     */
    @Override
    public void drawRoundRectangle(Vec2 pos, Vec2 size, float arc, boolean fill) {
        assert(can != null);
        setStyle(fill);
        can.drawRoundRect(pos.x, pos.y, pos.x + size.x, pos.y + size.y, arc, arc, paint);
    }

    /**
     * Draws a circle.
     *
     * @param center_pos center position
     * @param rad radius
     * @param fill true to fill, false to stroke
     */
    @Override
    public void drawCircle(Vec2 center_pos, float rad, boolean fill) {
        assert(can != null);
        setStyle(fill);
        can.drawCircle(center_pos.x, center_pos.y, rad, paint);
    }

    /**
     * Draws a line segment between two points.
     *
     * @param start_pos start coordinate
     * @param end_pos end coordinate
     */
    @Override
    public void drawLine(Vec2 start_pos, Vec2 end_pos) {
        assert(can != null);
        setStyle(false);
        can.drawLine(start_pos.x, start_pos.y, end_pos.x, end_pos.y, paint);
    }

    /**
     * Draws an N-sided regular polygon.
     *
     * @param n number of sides
     * @param center_pos center coordinate
     * @param rad radius
     * @param fill true to fill, false to stroke
     */
    @Override
    public void drawNSidePolygon(int n, Vec2 center_pos, float rad, boolean fill) {
        assert(can != null);
        if (n <= 2) throw new RuntimeException("n must be > 2 in call to drawNSidePolygon");
        setStyle(fill);
        can.drawPath(getNSidePolygonPath(n, center_pos, rad), paint);
    }

    /**
     * Sets active font.
     *
     * @param f FontEng instance
     */
    @Override
    public void setFont(FontEng f) {
        this.font = (FontAndroid) f;
    }

    /**
     * Sets paint font size and typeface.
     *
     * @param f FontAndroid instance
     */
    private void setFontStyle(FontAndroid f) {
        paint.setTextSize(f.getFontSize());
        paint.setTypeface(f.getTypeface());
    }

    /**
     * Draws text centered at position.
     *
     * @param text string to render
     * @param pos center coordinate
     */
    @Override
    public void drawText(String text, Vec2 pos) {
        assert(can != null);
        setStyle(true);
        setFontStyle(font);

        Paint.FontMetrics metrics = paint.getFontMetrics();

        float x = pos.x - paint.measureText(text) / 2f;
        float y = pos.y - (metrics.ascent + metrics.descent) / 2f;

        can.drawText(text, x, y, paint);
    }

    /**
     * Draws text fitted inside a bounding rectangle.
     *
     * @param text string to render
     * @param x0y0Pos top-left position
     * @param size bounding dimensions
     */
    @Override
    public void drawText(String text, Vec2 x0y0Pos, Vec2 size) {
        assert (can != null);

        setStyle(true);
        setFontStyle(font);

        can.save();

        can.clipRect(
                x0y0Pos.x,
                x0y0Pos.y,
                x0y0Pos.x + size.x,
                x0y0Pos.y + size.y
        );

        float originalSize = paint.getTextSize();
        Paint.FontMetrics metrics = paint.getFontMetrics();

        float textWidth = paint.measureText(text);
        float textHeight = metrics.descent - metrics.ascent;

        if (textWidth > 0 && textHeight > 0) {
            float scale = Math.min(
                    1f,
                    Math.min(size.x / textWidth, size.y / textHeight)
            );

            paint.setTextSize(originalSize * scale);
        }

        metrics = paint.getFontMetrics();

        float x = x0y0Pos.x + size.x / 2f;
        float y = x0y0Pos.y + (size.y - (metrics.descent - metrics.ascent)) / 2f
                - metrics.ascent;

        paint.setTextAlign(Paint.Align.CENTER);
        can.drawText(text, x, y, paint);

        paint.setTextSize(originalSize);
        can.restore();
    }

    /**
     * Computes inverse of viewport matrix for coordinate mapping.
     */
    public void calculateInverseMatrix() {
        if (!viewportMatrix.invert(inverseViewportMatrix)) {
            inverseViewportMatrix.reset();
        }
    }

    /**
     * Translates window pixel point into logical game coordinate space.
     *
     * @param pos point in screen pixel coordinates
     * @return translated point in logical game coordinates
     */
    @Override
    public Vec2 getPointInWindowPos(Vec2 pos) {
        float[] point = {
                pos.x,
                pos.y
        };

        inverseViewportMatrix.mapPoints(point);

        return new Vec2(point[0], point[1]);
    }

    /**
     * Resets clipping area to logical window size.
     */
    @Override
    public void resetClipping() {
        clipRect(0, 0, _logicWindowWidth, _logicWindowHeight);
    }

    /**
     * Sets rectangular clipping boundary.
     *
     * @param x top-left x
     * @param y top-left y
     * @param width clipping width
     * @param height clipping height
     */
    @Override
    public void clipRect(int x, int y, int width, int height) {
        can.clipRect(
            x,
            y,
            x + width,
            y + height
        );
    }
}
