package com.example.desktopengine;

import static com.example.utils.Utils.getNonDeformingAspectData;

import com.example.engine.ColorEng;
import com.example.engine.FontEng;
import com.example.engine.ImageEng;
import com.example.engine.Engine;
import com.example.engine.GraphicsInterface;
import com.example.utils.Utils;

import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferStrategy;
import java.nio.file.Path;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class GraphicsDesktop implements GraphicsInterface {
    private JFrame mainJFrame;
    Graphics2D graphics;
    BufferStrategy buf;
    Canvas canvas;
    FontDesktop current_font = new FontDesktop();
    GraphicsDesktop(){
        mainJFrame = new JFrame("Desktop Engine");
        mainJFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        canvas = new Canvas();

        canvas.addComponentListener(new ComponentAdapter() {

            @Override
            public void componentResized(ComponentEvent e) {
            Utils.NonDeformingAspectData data = getNonDeformingAspectData(getWidth(),getHeight());
            setViewport(data.scale_mult,data.offset_x,data.offset_y);
            }
        });

        // Give the canvas an explicit size
        canvas.setPreferredSize(new Dimension(600, 400));
        mainJFrame.add(canvas);
        mainJFrame.pack();
        mainJFrame.setVisible(true);
        // must be called after setVisible(true) and pack()
        canvas.createBufferStrategy(2);

        buf = canvas.getBufferStrategy();
        graphics = (Graphics2D) buf.getDrawGraphics();
    }
    @Override
    public int getWidth() {
        return canvas.getWidth();
    }

    @Override
    public int getHeight() {
        return canvas.getHeight();
    }

    float scaleMult,offsetX,offsetY;
    private final AffineTransform identityTransform = new AffineTransform();
    @Override
    public void setViewport(float scale_mult, float offset_x, float offset_y) {
        scaleMult = scale_mult;
        offsetX = offset_x;
        offsetY = offset_y;
    }
    boolean correcltyInitialized(){
        return mainJFrame.getWidth()!=0;
    }
    public void applyViewport() {
        graphics.translate(offsetX, offsetY);
        graphics.scale(scaleMult, scaleMult);
    }
    @Override
    public void render(Engine eng, double dt){
        do {
            do {
                this.graphics = (Graphics2D)this.buf.getDrawGraphics();
                try {
                    // Reset transform for THIS frame
                    graphics.setTransform(identityTransform);
                    // Clear screen
                    clear();

                    // Apply viewport
                    graphics.translate(offsetX, offsetY);
                    graphics.scale(scaleMult, scaleMult);
                    // Render
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
    ColorEng clearColor = new ColorEng(255,255,255,255);
    @Override
    public void clear(ColorEng color){
        if (graphics == null) return;
        clearColor = color;
        setColor(color);
        // Fill canvas area with clear color
        graphics.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }
    public void clear(){
        if (graphics == null) return;
        setColor(clearColor);
        // Fill canvas area with clear color
        graphics.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }
    @Override
    public void setColor(ColorEng color){
        graphics.setColor(new Color(color.r,color.g,color.b,color.a));
    }
    @Override
    public void setStrokeWidth(int pxWidth){
        graphics.setStroke(new BasicStroke(pxWidth));
    }

    private AffineTransform storedTransform = null;
    @Override
    public void drawRectangle(int x, int y, int width, int height, boolean fill){
        if (fill) {
            graphics.fillRect(x,y,width,height);
        } else {
            graphics.drawRect(x,y,width,height);
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
    private Polygon generateNSidePolygon(int n, int centerX, int centerY, float radius){
        if(n<=2) throw new RuntimeException("n must be > 2 in call to drawNSidePolygon");
        int[] xVec = new int[n];
        int[] yVec = new int[n];
        for (int i = 0; i < n; i++) {
            float angle = (float) (2*i * Math.PI / n);
            float xPoint = (float) (centerX + radius * Math.cos(angle));
            float yPoint = (float) (centerY + radius * Math.sin(angle));
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

    @Override
    public void translate(float x, float y) {
        graphics.translate(x,y);
    }

    @Override
    public void scale(float x, float y) {
        graphics.scale(x,y);
    }

    @Override
    public void rotate(float x, float y, float rotationDegrees) {
        graphics.rotate(Math.PI*rotationDegrees/180, x, y);
    }

    @Override
    public void save() {
        storedTransform = graphics.getTransform();
    }

    @Override
    public void restore() {
        if(storedTransform==null) throw new RuntimeException("Ensure to call save before this call at least once");
        graphics.setTransform(storedTransform);
    }

    public JFrame getFrame(){
        return this.mainJFrame;
    }
    public Canvas getCanvas(){
        return this.canvas;
    }
}
