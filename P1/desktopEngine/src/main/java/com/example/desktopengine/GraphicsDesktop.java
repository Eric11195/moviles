package com.example.desktopengine;

import static com.example.utils.Utils.getNonDeformingAspectData;

import com.example.engine.ColorEng;
import com.example.engine.FontEng;
import com.example.engine.ImageEng;
import com.example.engine.Engine;
import com.example.engine.GraphicsInterface;
import com.example.utils.Utils;
import com.example.utils.Vec2;

import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
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
                    transformation.setToIdentity();

                    clear();

                    transformation.translate(offsetX, offsetY);
                    transformation.scale(scaleMult, scaleMult);

                    this.graphics.setTransform(transformation);

                    eng.getCurrentScene().render(this, dt);

                    calculateInverseMatrix();
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
    public void drawImage(ImageEng img, Vec2 src_pos, Vec2 src_size, Vec2 dst_pos, Vec2 dst_size) {
        ImageDesktop img_dsk = (ImageDesktop) img;
        Image _img = img_dsk.getImage();
        graphics.drawImage(_img,(int)dst_pos.x,(int)dst_pos.y,(int) (dst_pos.x+dst_size.x),(int)(dst_pos.y+dst_size.y), (int)src_pos.x,(int)src_pos.y,(int)(src_pos.x+src_size.x), (int)(src_pos.y+src_size.y),null);
    }
    @Override
    public void drawImage(ImageEng img, Vec2 pos, Vec2 size){
        drawImage(img, new Vec2(0,0),new Vec2(img.getWidth(), img.getHeight()),pos,size);
    }
    @Override
    public void drawImage(ImageEng img, Vec2 pos){
        drawImage(img, pos, new Vec2(img.getWidth(), img.getHeight()));
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
    public void drawRectangle(Vec2 pos, Vec2 size, boolean fill){
        if (fill) {
            graphics.fillRect((int)pos.x,(int)pos.y,(int)size.x,(int)size.y);
        } else {
            graphics.drawRect((int)pos.x,(int)pos.y,(int)size.x,(int)size.y);
        }
    }
    @Override
    public void drawRoundRectangle(Vec2 pos, Vec2 size, float arc, boolean fill){
        if(fill) {
            graphics.fillRoundRect((int)pos.x,(int)pos.y,(int)size.x,(int)size.y, (int)arc,(int) arc);
        }else{
            graphics.drawRoundRect((int)pos.x,(int)pos.y,(int)size.x,(int)size.y, (int)arc, (int)arc);
        }
    }
    @Override
    public void drawCircle(Vec2 center_pos, float rad, boolean fill) {
        if (!fill) {
            graphics.drawOval((int)Math.round(center_pos.x-rad), (int)Math.round(center_pos.y-rad), (int)Math.round(rad*2), (int)Math.round(rad*2));
        } else {
            graphics.fillOval((int)Math.round(center_pos.x- rad), (int)Math.round(center_pos.y- rad), (int)Math.round(rad*2), (int)Math.round(rad*2));
        }
    }
    @Override
    public void drawLine(Vec2 start_point, Vec2 end_point){
        graphics.drawLine((int)start_point.x,(int)start_point.y,(int)end_point.x,(int)end_point.y);
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
    public void drawNSidePolygon(int n, Vec2 center_pos, float rad, boolean fill){
        if(n<=2) throw new RuntimeException("n must be > 2 in call to drawNSidePolygon");
        if (fill) {
            graphics.fillPolygon(generateNSidePolygon(n,(int)center_pos.x,(int)center_pos.y,rad));
        } else {
            graphics.drawPolygon(generateNSidePolygon(n,(int)center_pos.x,(int)center_pos.y,rad));
        }
    }
    @Override
    public void setFont(FontEng f){
        current_font = (FontDesktop) f;
    }
    @Override
    public void drawText(String text, Vec2 pos){
        graphics.setFont(current_font.getFont());

        FontMetrics metrics = graphics.getFontMetrics();

        float x = pos.x - metrics.stringWidth(text) / 2f;
        float y = pos.y - (metrics.getAscent() - metrics.getDescent()) / 2f;

        graphics.drawString(text, Math.round(x), Math.round(y));
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
        transformation.translate(x, y);
        graphics.setTransform(transformation);
    }

    @Override
    public void scale(float x, float y) {
        transformation.scale(x, y);
        graphics.setTransform(transformation);
    }

    @Override
    public void rotate(float x, float y, float rotationDegrees) {
        transformation.rotate(
                Math.PI * rotationDegrees / 180.0,
                x,
                y
        );

        graphics.setTransform(transformation);
    }

    @Override
    public void save() {
        storedTransform = new AffineTransform(transformation);
    }

    @Override
    public void restore() {
        if (storedTransform == null) {
            throw new RuntimeException(
                    "Ensure to call save before this call at least once"
            );
        }

        transformation = new AffineTransform(storedTransform);
        graphics.setTransform(transformation);
    }

    public JFrame getFrame(){
        return this.mainJFrame;
    }
    public Canvas getCanvas(){
        return this.canvas;
    }

    AffineTransform transformation = new AffineTransform();
    AffineTransform inverse = new AffineTransform();
    private void calculateInverseMatrix(){
        try{
            inverse =
                    transformation.createInverse();
        }catch(NoninvertibleTransformException error){
            inverse = new AffineTransform();
        }
    }
    @Override
    public Vec2 getPointInWindowPos(Vec2 pos){
        Point2D.Float screen = new Point2D.Float(pos.x,pos.y);
        Point2D.Float game = new Point2D.Float();
        inverse.transform(screen,game);

        System.out.println(
                "mouse screen: " + pos.x + ", " + pos.y +
                        " -> game: " + game.x + ", " + game.y
        );
        System.out.println(
                "viewport: scale=" + scaleMult +
                        " offset=" + offsetX + ", " + offsetY
        );
        return new Vec2(game.x,game.y);
    }
}
