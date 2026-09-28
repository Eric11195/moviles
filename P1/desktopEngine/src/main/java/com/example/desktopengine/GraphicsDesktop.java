package com.example.desktopengine;

import com.example.engine.ColorEng;
import com.example.engine.FontEng;
import com.example.engine.ImageEng;
import com.example.engine.Engine;
import com.example.engine.GraphicsInterface;
import com.example.utils.CoordRect;
import com.example.utils.Scale;
import com.example.utils.Vec2;

import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferStrategy;
import java.nio.file.Path;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class GraphicsDesktop extends Scale implements GraphicsInterface {
    private JFrame mainJFrame;
    Graphics2D graphics;
    BufferStrategy buf;
    Canvas canvas;
    FontDesktop current_font = new FontDesktop();
    GraphicsDesktop(){
        mainJFrame = new JFrame("Desktop Engine");
        mainJFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        canvas = new Canvas();
        // Give the canvas an explicit size
        canvas.setPreferredSize(new Dimension(800, 600));
        mainJFrame.add(canvas);
        mainJFrame.pack();
        mainJFrame.setVisible(true);
        // must be called after setVisible(true) and pack()
        canvas.createBufferStrategy(2);

        buf = canvas.getBufferStrategy();
        graphics = (Graphics2D) buf.getDrawGraphics();
    }

    boolean correcltyInitialized(){
        return mainJFrame.getWidth()!=0;
    }
    @Override
    public void render(Engine eng, double dt){
        do {
            do {
                this.graphics = (Graphics2D)this.buf.getDrawGraphics();
                try {
                    eng.getCurrentScene().render(this,dt);
                }
                finally {
                    this.graphics.dispose();
                    //Elimina el contexto gráfico y libera recursos del sistema realacionado
                }
            } while(this.buf.contentsRestored());
            this.buf.show();
        } while(this.buf.contentsLost());
    }
    @Override
    public void drawImage(ImageEng img, int src_x, int src_y, int src_w, int src_h, int dst_x, int dst_y, int dst_w, int dst_h) {
        ImageDesktop img_dsk = (ImageDesktop) img;
        Image _img = img_dsk.getImage();
        graphics.drawImage(_img,dst_x,dst_y,dst_x+dst_w,dst_y+dst_h, src_x,src_y,src_x+src_w, src_y+src_h,null);
    }
    @Override
    public void drawImage(ImageEng img, int x, int y, int width, int height){
        drawImage(img, 0,0,img.getWidth(), img.getHeight(),x,y,width,height);
    }
    @Override
    public void drawImage(ImageEng img, int x, int y){
        drawImage(img,x,y, img.getWidth(), img.getHeight());
    }
    @Override
    public void clear(ColorEng color){
        if (graphics == null) return;
        ColorEng previous = new ColorEng(color.r,color.g,color.b,color.a);
        setColor(color);
        // Fill canvas area with clear color
        graphics.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        setColor(previous);
    }
    @Override
    public void setColor(ColorEng color){
        graphics.setColor(new Color(color.r,color.g,color.b,color.a));
    }
    @Override
    public void setStrokeWidth(int pxWidth){
        graphics.setStroke(new BasicStroke(pxWidth));
    }
    public Polygon getPolygon(Vec2... points) {
        int[] xpoints = new int[points.length];
        int[] ypoints = new int[points.length];

        for (int i = 0; i < points.length; i++) {
            xpoints[i] = (int) points[i].x;
            ypoints[i] = (int) points[i].y;
        }
        return new Polygon(xpoints, ypoints, points.length);
    }
    @Override
    public void drawRectangle(int x, int y, int width, int height, boolean fill){
        CoordRect cr = new CoordRect(new Vec2(x,y), new Vec2(x+width,y+height));
        transformRect(cr);
        Polygon p = getPolygon(
                cr.ul,
                cr.ur,
                cr.dr,
                cr.dl);
        if (fill) {
            graphics.fillPolygon(p);
        } else {
            graphics.drawPolygon(p);
        }
    }
    @Override
    public void drawRoundRectangle(int x, int y, int width, int height, int arc, boolean fill){
        if(fill) {
            graphics.fillRoundRect(x, y, width, height, arc, arc);
        }else{
            graphics.drawRoundRect(x, y, width, height, arc, arc);
        }
    }
    @Override
    public void drawCircle(int center_x, int center_y, float rad, boolean fill) {
        if (!fill) {
            graphics.drawOval((int)Math.round(center_x-rad), (int)Math.round(center_y-rad), (int)Math.round(rad*2), (int)Math.round(rad*2));
        } else {
            graphics.fillOval((int)Math.round(center_x - rad), (int)Math.round(center_y - rad), (int)Math.round(rad*2), (int)Math.round(rad*2));
        }
    }
    @Override
    public void drawLine(int x1, int y1, int x2, int y2){
        graphics.drawLine(x1,y1,x2,y2);
    }
    //src: https://codingtechroom.com/question/-draw-hexagons-android
    private Polygon generateNSidePolygon(int n, int x, int y, float radius){
        if(n<=2) throw new RuntimeException("n must be > 2 in call to drawNSidePolygon");
        int[] xVec = new int[n];
        int[] yVec = new int[n];
        for (int i = 0; i < n; i++) {
            float angle = (float) (2*i * Math.PI / n);
            float xPoint = (float) (x + radius * Math.cos(angle));
            float yPoint = (float) (y + radius * Math.sin(angle));
            xVec[i]=Math.round(xPoint);
            yVec[i]=Math.round(yPoint);
        }
        return new Polygon(xVec,yVec,n);
    }
    @Override
    public void drawNSidePolygon(int n, int center_x, int center_y, float rad, boolean fill){
        if(n<=2) throw new RuntimeException("n must be > 2 in call to drawNSidePolygon");
        if (fill) {
            graphics.fillPolygon(generateNSidePolygon(n,center_x,center_y,rad));
        } else {
            graphics.drawPolygon(generateNSidePolygon(n,center_x,center_y,rad));
        }
    }
    @Override
    public void setFont(FontEng f){
        current_font = (FontDesktop) f;
    }
    @Override
    public void drawText(String text, int x, int y){
        graphics.setFont(current_font.getFont());
        graphics.drawString(text, x, y);
    }

    public ImageEng createImage(String path){
        return new ImageDesktop(Path.of(
                "assets",
                path
        ).toString());
    }

    @Override
    public FontEng createFont(String path, float size) {
        return (FontEng) new FontDesktop(path,size);
    }
    @Override
    public FontEng createFont(float size) {
        return (FontEng) new FontDesktop(size);
    }

    @Override
    public void setFontSize(float size) {
        current_font.setFontSize(size);
    }

    public JFrame getFrame(){
        return this.mainJFrame;
    }
    public Canvas getCanvas(){
        return this.canvas;
    }
}
