package com.example.engine;

import com.example.utils.Vec2;

public interface GraphicsInterface {
    public void render(Engine eng, double dt);
    public void drawImage(ImageEng img, Vec2 src_pos, Vec2 src_size, Vec2 dst_pos, Vec2 dst_size);
    public void drawImage(ImageEng img, Vec2 pos, Vec2 size);
    public void drawImage(ImageEng img, Vec2 pos);
    public void clear(ColorEng color);
    public void setColor(ColorEng color);
    public void setStrokeWidth(int pxWidth);
    public void drawRectangle(Vec2 pos, Vec2 size, boolean fill);
    public void drawRoundRectangle(Vec2 pos, Vec2 size, float arc, boolean fill);
    public void drawCircle(Vec2 centerPos, float rad, boolean fill);
    public void drawLine(Vec2 startPoint, Vec2 enPoint);
    public void drawNSidePolygon(int n, Vec2 centerPos, float rad, boolean fill);
    public void setFont(FontEng f);
    public void drawText(String text, Vec2 center_pos);
    public ImageEng createImage(String path);
    public FontEng createFont(String path, float size);
    public FontEng createFont(float size);
    public void setFontSize(float size);
    public void translate(float x, float y);
    public void scale(float x, float y);
    public void rotate(float x, float y, float rotationDegrees);
    public void save();
    public void restore();

    int getWidth();
    int getHeight();

    void setViewport(float scale_mult, float offset_x, float offset_y);

    Vec2 getPointInWindowPos(Vec2 pos);
}
