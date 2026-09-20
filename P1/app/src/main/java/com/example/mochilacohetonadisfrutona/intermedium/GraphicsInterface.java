package com.example.mochilacohetonadisfrutona.intermedium;

public interface GraphicsInterface {
    public void init(int id);
    public boolean startRender();
    public void endRender();
    public void drawImage(ImageAndroid img, int src_x, int src_y, int src_w, int src_h, int dst_x, int dst_y, int dst_w, int dst_h) throws Exception;
    public void drawImage(ImageAndroid img, int x, int y, int width, int height);
    public void drawImage(ImageAndroid img, int x, int y);
    public void clear(int color);
    public void setColor(int color);
    public void set_style(boolean fill);
    public void drawRectangle(float x, float y, float width, float height, boolean fill);
    public void drawRoundRectangle(float x, float y, float width, float height, float arc, boolean fill);
    public void drawCircle(float center_x, float center_y, float rad, boolean fill);
    public void drawLine(float x1, float y1, float x2, float y2);
    public void drawHexagon(float x, float y, float rad, boolean fill);
    public void setFont(FontAndroid f);
    public void drawText(String text, float x, float y);
}
