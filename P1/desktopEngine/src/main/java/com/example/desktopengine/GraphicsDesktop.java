package com.example.desktopengine;

import com.example.engine.EngColor;
import com.example.engine.EngFont;
import com.example.engine.ImageEng;
import com.example.engine.Engine;
import com.example.engine.GraphicsInterface;

import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class GraphicsDesktop implements GraphicsInterface {
    private JFrame mainJFrame;
    Graphics2D graphics;
    BufferStrategy buf;
    Canvas canvas;
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
    public void clear(EngColor color){
        if (graphics == null) return;
        EngColor previous = new EngColor(color.r,color.g,color.b,color.a);
        setColor(color);
        // Fill canvas area with clear color
        graphics.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        setColor(previous);
    }
    @Override
    public void setColor(EngColor color){
        graphics.setColor(new Color(color.r,color.g,color.b,color.a));
    }
    @Override
    public void setStrokeWidth(int pxWidth){
        graphics.setStroke(new BasicStroke(pxWidth));
    }
    @Override
    public void drawRectangle(int x, int y, int width, int height, boolean fill){
        if (fill) {
            graphics.fillRect(x, y, width, height);
        } else {
            graphics.drawRect(x, y, width, height);
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
    public void setFont(EngFont f){

    }
    @Override
    public void drawText(String text, int x, int y){

    }

    public ImageEng createImage(String path){
        return new ImageDesktop(path);
    }
}
