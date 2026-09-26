package com.example.engine;

public interface GraphicsInterface {
    public void render(Engine eng, double dt);
    public void drawImage(ImageEng img, int src_x, int src_y, int src_w, int src_h, int dst_x, int dst_y, int dst_w, int dst_h);
    public void drawImage(ImageEng img, int x, int y, int width, int height);
    public void drawImage(ImageEng img, int x, int y);
    public void clear(ColorEng color);
    public void setColor(ColorEng color);
    public void setStrokeWidth(int pxWidth);
    public void drawRectangle(int x, int y, int width, int height, boolean fill);
    public void drawRoundRectangle(int x, int y, int width, int height, int arc, boolean fill);
    public void drawCircle(int center_x, int center_y, float rad, boolean fill);
    public void drawLine(int x1, int y1, int x2, int y2);
    public void drawNSidePolygon(int n, int center_x, int center_y, float rad, boolean fill);
    public void setFont(FontEng f);
    public void drawText(String text, int x, int y);
    public ImageEng createImage(String path);
    public FontEng createFont(String path, float size);
    public void setFontSize(float size);
}
