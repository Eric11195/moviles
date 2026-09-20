package com.example.desktop;

public class GraphicsDesktop {
    @Override
    public void init(int id);
    @Override
    public boolean startRender();
    @Override
    public void endRender();
    @Override
    public void drawImage(ImageAndroid img, int src_x, int src_y, int src_w, int src_h, int dst_x, int dst_y, int dst_w, int dst_h) throws Exception;
    @Override
    public void drawImage(ImageAndroid img, int x, int y, int width, int height);
    @Override
    public void drawImage(ImageAndroid img, int x, int y);
    @Override
    public void clear(EngColor color);
    @Override
    public void setColor(EngColor color);
    @Override
    public void setStrokeWidth(float pxWidth);
    @Override
    public void drawRectangle(float x, float y, float width, float height, boolean fill);
    @Override
    public void drawRoundRectangle(float x, float y, float width, float height, float arc, boolean fill);
    @Override
    public void drawCircle(float center_x, float center_y, float rad, boolean fill);
    @Override
    public void drawLine(float x1, float y1, float x2, float y2);
    @Override
    public void drawHexagon(float center_x, float center_y, float rad, boolean fill);
    @Override
    public void setFont(FontAndroid f);
    @Override
    public void drawText(String text, float x, float y);
}
