package com.example.engine;

public interface GraphicsInterface {
    public void init(int id);
    public boolean startRender();
    public void endRender();
    public void drawImage(EngImage img, int src_x, int src_y, int src_w, int src_h, int dst_x, int dst_y, int dst_w, int dst_h) throws Exception;
    public void drawImage(EngImage img, int x, int y, int width, int height);
    public void drawImage(EngImage img, int x, int y);
    public void clear(EngColor color);
    public void setColor(EngColor color);
    public void setStrokeWidth(int pxWidth);
    public void drawRectangle(int x, int y, int width, int height, boolean fill);
    public void drawRoundRectangle(int x, int y, int width, int height, int arc, boolean fill);
    public void drawCircle(int center_x, int center_y, float rad, boolean fill);
    public void drawLine(int x1, int y1, int x2, int y2);
    public void drawNSidePolygon(int n, int center_x, int center_y, float rad, boolean fill);
    public void setFont(EngFont f);
    public void drawText(String text, int x, int y);
}
