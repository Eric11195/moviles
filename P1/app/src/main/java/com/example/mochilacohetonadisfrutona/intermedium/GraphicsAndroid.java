package com.example.mochilacohetonadisfrutona.intermedium;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.util.Log;

public class GraphicsAndroid {
    private SurfaceHolder surface = null;
    private Canvas can = null;
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    //This must be call before any other of the following calls. And only once
    public void init(SurfaceView surf){
        assert(surface == null);
        assert(can == null);
        surf.setZOrderOnTop(true);
        surface = surf.getHolder();
    }
    //src: https://codingtechroom.com/question/-draw-hexagons-android
    private Path getHexagonPath(float x, float y, float radius){
        Path hexagonPath = new Path();
        for (int i = 0; i < 6; i++) {
            float angle = (float) (i * Math.PI / 3);
            float xPoint = (float) (x + radius * Math.cos(angle));
            float yPoint = (float) (y + radius * Math.sin(angle));
            if (i == 0) {
                hexagonPath.moveTo(xPoint, yPoint);
            } else {
                hexagonPath.lineTo(xPoint, yPoint);
            }
        }
        hexagonPath.close();
        return hexagonPath;
    }
    //This must be call before any other of the following calls.
    //And must be followed (after all the other calls by a endRender()
    public boolean startRender(){
        assert(can == null);
        //can = surface.lockHardwareCanvas();
        can = surface.lockHardwareCanvas();
        if (can == null) {
            Log.d("DRAW", "Canvas is NULL!");
            return false;
        }
        return true;
    }
    //render what has been rendered on screen in between startRender() and this call
    public void endRender(){
        assert(can!=null);
        surface.unlockCanvasAndPost(can);
    }

    //src refers to the part of the src image that will be rendered, dst refers to the destination pos and size in the viewport
    public void drawImage(Image img, int src_x, int src_y, int src_w, int src_h, int dst_x, int dst_y, int dst_w, int dst_h) throws Exception{
        assert(can!=null);
        assert(img!=null);
        set_style(true);
        can.drawBitmap(
                img.getBitmap(),
                new Rect(src_x,src_y,src_w,src_h),
                new Rect(dst_x, dst_y, dst_w, dst_h),
                paint
        );
    }
    //draws the complete image in the indicated position and size
    public void drawImage(Image img, int x, int y, int width, int height) throws Exception {
        drawImage(
                img,
                0,0,img.getWidth(),img.getHeight(),
                x,y,width,height
        );
    }
    //draws image with its original size in screen
    public void drawImage(Image img, int x, int y) throws Exception{
        drawImage(img, x,y,img.getWidth(), img.getHeight());
    }

    // fills the entire display with the given color
    public void clear(int color){
        assert(can!=null);
        can.drawColor(color);
    }
    //sets the color for all the following simple shapes renders
    public void setColor(int color){
        assert(can!=null);
        paint.setColor(color);
    }
    public void set_style(boolean fill){
        paint.setStyle(fill ? Paint.Style.FILL : Paint.Style.STROKE);
    }
    public void drawRectangle(float x, float y, float width, float height, boolean fill){
        assert(can!=null);
        set_style(fill);
        can.drawRect(x,y,width,height,paint);
    }
    public void drawRoundRectangle(float x, float y, float width, float height, float arc, boolean fill){
        assert(can!=null);
        set_style(fill);
        can.drawRoundRect(x,y,width,height,arc,arc,paint);
    }
    public void drawCircle(float center_x, float center_y, float rad, boolean fill){
        assert(can!=null);
        set_style(fill);
        can.drawCircle(center_x,center_y,rad,paint);
    }
    public void drawLine(float x1, float y1, float x2, float y2){
        assert(can!= null);
        set_style(false);
        can.drawLine(x1,y1,x2,y2,paint);
    }
    public void drawHexagon(float x, float y, float rad, boolean fill){
        assert(can!=null);
        set_style(fill);
        can.drawPath(getHexagonPath(x,y,rad), paint);
    }
    //Sets the fonts to be used in the following drawText calls
    public void setFont(Font f){

    }
    public void drawText(String text, float x, float y){
        assert(can!=null);
        set_style(false);
        can.drawText(text, x, y, paint);
    }
}
