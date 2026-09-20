package com.example.android;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.engine.EngColor;
import com.example.engine.EngFont;
import com.example.engine.EngImage;
import com.example.engine.GraphicsInterface;

public class GraphicsAndroid implements GraphicsInterface {
    private SurfaceHolder surface = null;
    private Canvas can = null;
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private final FontAndroid font = new FontAndroid();

    private AppCompatActivity activity;
    public GraphicsAndroid(AppCompatActivity activity){
        this.activity = activity;
    }

    //This must be call before any other of the following calls. And only once
    @Override
    public void init(int id){
        assert(surface == null);
        assert(can == null);
        SurfaceView surf = activity.findViewById(id);
        surf.setZOrderOnTop(true);
        surface = surf.getHolder();
    }
    //src: https://codingtechroom.com/question/-draw-hexagons-android
    private Path getHexagonPath(int n, int x, int y, float radius){
        Path nSidePolygonPath = new Path();
        for (int i = 0; i < n; i++) {
            float angle = (float) (2*i * Math.PI / n);
            float xPoint = (float) (x + radius * Math.cos(angle));
            float yPoint = (float) (y + radius * Math.sin(angle));
            if (i == 0) {
                nSidePolygonPath.moveTo(xPoint, yPoint);
            } else {
                nSidePolygonPath.lineTo(xPoint, yPoint);
            }
        }
        nSidePolygonPath.close();
        return nSidePolygonPath;
    }
    //This must be call before any other of the following calls.
    //And must be followed (after all the other calls by a endRender()
    @Override
    public boolean startRender(){
        assert(can == null);
        //can = surface.lockHardwareCanvas();
        can = surface.lockCanvas();
        if (can == null) {
            Log.d("DRAW", "Canvas is NULL!");
            return false;
        }
        return true;
    }
    //render what has been rendered on screen in between startRender() and this call
    @Override
    public void endRender(){
        assert(can!=null);
        surface.unlockCanvasAndPost(can);
    }

    //src refers to the part of the src image that will be rendered, dst refers to the destination pos and size in the viewport
    @Override
    public void drawImage(EngImage img, int src_x, int src_y, int src_w, int src_h, int dst_x, int dst_y, int dst_w, int dst_h) {
        assert(can!=null);
        assert(img!=null);
        setStyle(true);
        /*
        can.drawBitmap(
                img.getBitmap(),
                new Rect(src_x,src_y,src_w,src_h),
                new Rect(dst_x, dst_y, dst_w, dst_h),
                paint
        );
        */
    }
    //draws the complete image in the indicated position and size
    @Override
    public void drawImage(EngImage img, int x, int y, int width, int height) {
        drawImage(
                img,
                0,0,img.getWidth(),img.getHeight(),
                x,y,width,height
        );
    }
    //draws image with its original size in screen
    @Override
    public void drawImage(EngImage img, int x, int y) {
        drawImage(img, x,y,img.getWidth(), img.getHeight());
    }

    // fills the entire display with the given color
    @Override
    public void clear(EngColor color){
        assert(can!=null);
        can.drawColor(color.getColorAsInt());
    }
    //sets the color for all the following simple shapes renders
    @Override
    public void setColor(EngColor color){
        assert(can!=null);
        paint.setColor(color.getColorAsInt());
    }
    @Override
    public void setStrokeWidth(int pxWidth){
        assert(can!=null);
        paint.setStrokeWidth(pxWidth);
    }

    private void setStyle(boolean fill){
        paint.setStyle(fill ? Paint.Style.FILL : Paint.Style.STROKE);
    }
    @Override
    public void drawRectangle(int x, int y, int width, int height, boolean fill){
        assert(can!=null);
        setStyle(fill);
        can.drawRect(x,y,x+width,y+height,paint);
    }
    @Override
    public void drawRoundRectangle(int x, int y, int width, int height, int arc, boolean fill){
        assert(can!=null);
        setStyle(fill);
        can.drawRoundRect(x,y,x+width,y+height,arc,arc,paint);
    }
    @Override
    public void drawCircle(int center_x, int center_y, float rad, boolean fill){
        assert(can!=null);
        setStyle(fill);
        can.drawCircle(center_x,center_y,rad,paint);
    }
    @Override
    public void drawLine(int x1, int y1, int x2, int y2){
        assert(can!= null);
        setStyle(false);
        can.drawLine(x1,y1,x2,y2,paint);
    }
    @Override
    public void drawNSidePolygon(int n, int x, int y, float rad, boolean fill){
        assert(can!=null);
        if(n<=2) throw new RuntimeException("n must be > 2 in call to drawNSidePolygon");
        setStyle(fill);
        can.drawPath(getHexagonPath(n,x,y,rad), paint);
    }
    //Sets the fonts to be used in the following drawText calls
    @Override
    public void setFont(EngFont f){
        //font = f;
    }
    private void setFontStyle(FontAndroid f){
        assert(paint!=null);
        paint.setTextSize(f.getFontSize());
        paint.setTypeface(f.getTypeface());
    }
    @Override
    public void drawText(String text, int x, int y){
        assert(can!=null);
        setStyle(false);
        setFontStyle(font);
        can.drawText(text, x, y, paint);
    }
}
