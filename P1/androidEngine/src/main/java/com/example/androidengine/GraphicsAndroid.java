package com.example.androidengine;

import static com.example.utils.Utils.getNonDeformingAspectData;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.engine.ColorEng;
import com.example.engine.FontEng;
import com.example.engine.ImageEng;
import com.example.engine.Engine;
import com.example.engine.GraphicsInterface;
import com.example.utils.Utils;
import com.example.utils.Vec2;

public class GraphicsAndroid implements GraphicsInterface {
    private final SurfaceView surf;
    private SurfaceHolder surface = null;
    private Canvas can = null;
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private FontAndroid font = new FontAndroid();

    private final AppCompatActivity activity;
    public GraphicsAndroid(AppCompatActivity activity, int id){
        this.activity = activity;
        assert(surface == null);
        assert(can == null);
        surf = activity.findViewById(id);
        surf.setZOrderOnTop(true);
        surface = surf.getHolder();
        surface.addCallback(
                new SurfaceHolder.Callback() {

                    @Override
                    public void surfaceCreated(SurfaceHolder holder) {
                    }

                    @Override
                    public void surfaceChanged(
                            SurfaceHolder holder,
                            int format,
                            int width,
                            int height) {
                        com.example.utils.Utils.NonDeformingAspectData data = getNonDeformingAspectData(width,height);
                        setViewport(data.scale_mult,data.offset_x,data.offset_y);
                    }

                    @Override
                    public void surfaceDestroyed(SurfaceHolder holder) {
                    }
                }
        );
    }

    @Override
    public int getWidth() {
        return surf.getWidth();
    }

    @Override
    public int getHeight() {
        return surf.getHeight();
    }

    float scaleMult,offsetX,offsetY;
    @Override
    public void setViewport(float scale_mult, float offset_x, float offset_y) {
        scaleMult = scale_mult;
        offsetX = offset_x;
        offsetY = offset_y;

        viewportMatrix.reset();

        // Must match the transformation used in startRender()
        viewportMatrix.setScale(scaleMult, scaleMult);
        viewportMatrix.postTranslate(offsetX, offsetY);

        calculateInverseMatrix();
    }

    //src: https://codingtechroom.com/question/-draw-hexagons-android
    private Path getNSidePolygonPath(int n, Vec2 center_pos, float radius){
        Path nSidePolygonPath = new Path();
        for (int i = 0; i < n; i++) {
            float angle = (float) (2*i * Math.PI / n);
            float xPoint = (float) (center_pos.x + radius * Math.cos(angle));
            float yPoint = (float) (center_pos.y + radius * Math.sin(angle));
            if (i == 0) {
                nSidePolygonPath.moveTo(xPoint, yPoint);
            } else {
                nSidePolygonPath.lineTo(xPoint, yPoint);
            }
        }
        nSidePolygonPath.close();
        return nSidePolygonPath;
    }
    private final Matrix viewportMatrix = new Matrix();
    private final Matrix inverseViewportMatrix = new Matrix();
    //This must be call before any other of the following calls.
    //And must be followed (after all the other calls by a endRender()
    public boolean startRender() {
        assert(can == null);

        can = surface.lockCanvas();

        if (can == null) {
            Log.d("DRAW", "Canvas is NULL!");
            return false;
        }

        can.drawColor(clearColor.getColorAsInt());
        can.setMatrix(viewportMatrix);

        return true;
    }
    public ImageEng createImage(String path){
        return new ImageAndroid(activity,path);
    }

    @Override
    public FontEng createFont(String path, float size) {
        return new FontAndroid(activity,path,size, false);
    }

    @Override
    public FontEng createFont(float size) {
        return new FontAndroid(size, false);
    }

    @Override
    public void setFontSize(float size) {
        font.setFontSize(size);
    }

    @Override
    public void translate(float x, float y) {
        can.translate(x,y);
    }

    @Override
    public void scale(float x, float y) {
        can.scale(x,y);
    }

    @Override
    public void rotate(float x, float y, float rotationDegrees) {
        can.rotate(rotationDegrees,x,y);
    }
    @Override
    public void save() {
        can.save();
    }

    @Override
    public void restore() {
        can.restore();
    }

    //render what has been rendered on screen in between startRender() and this call
    public void endRender(){
        assert(can!=null);
        surface.unlockCanvasAndPost(can);
        can = null;
    }
    @Override
    public void render(Engine eng){
        if(startRender()) {
            eng.getCurrentScene().render(this);
            endRender();
        }
    }

    //src refers to the part of the src image that will be rendered, dst refers to the destination pos and size in the viewport
    @Override
    public void drawImage(ImageEng img, Vec2 src_pos, Vec2 src_size, Vec2 dst_pos, Vec2 dst_size) {
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
        ImageAndroid _img = (ImageAndroid)img;
        can.drawBitmap(_img.getBitmap(), new Rect((int)src_pos.x,(int)src_pos.y,(int)(src_pos.x+src_size.x),(int)(src_pos.y+src_size.y)), new Rect((int)dst_pos.x,(int)dst_pos.y,(int)(dst_pos.x+dst_size.x),(int)(dst_pos.y+dst_size.y)), paint);
    }
    //draws the complete image in the indicated position and size
    @Override
    public void drawImage(ImageEng img, Vec2 pos, Vec2 size) {
        drawImage(
                img,
                new Vec2(0,0),new Vec2(img.getWidth(),img.getHeight()),
                pos,size
        );
    }
    //draws image with its original size in screen
    @Override
    public void drawImage(ImageEng img, Vec2 pos) {
        drawImage(img, pos,new Vec2(img.getWidth(), img.getHeight()));
    }

    ColorEng clearColor = new ColorEng(255,255,255,255);
    // fills the entire display with the given color
    @Override
    public void clear(ColorEng color){
        assert(can!=null);
        clearColor = color;
        can.drawColor(color.getColorAsInt());
    }
    //sets the color for all the following simple shapes renders
    @Override
    public void setColor(ColorEng color){
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
    public void drawRectangle(Vec2 pos, Vec2 size, boolean fill){
        assert(can!=null);
        setStyle(fill);
        can.drawRect(pos.x,pos.y,pos.x+size.x,pos.y+size.y,paint);
    }
    @Override
    public void drawRoundRectangle(Vec2 pos, Vec2 size, float arc, boolean fill){
        assert(can!=null);
        setStyle(fill);
        can.drawRoundRect(pos.x,pos.y,pos.x+size.x,pos.y+size.y,arc,arc,paint);
    }
    @Override
    public void drawCircle(Vec2 center_pos, float rad, boolean fill){
        assert(can!=null);
        setStyle(fill);
        can.drawCircle(center_pos.x,center_pos.y,rad,paint);
    }
    @Override
    public void drawLine(Vec2 start_pos, Vec2 end_pos){
        assert(can!= null);
        setStyle(false);
        can.drawLine(start_pos.x,start_pos.y,end_pos.x,end_pos.y,paint);
    }
    @Override
    public void drawNSidePolygon(int n, Vec2 center_pos, float rad, boolean fill){
        assert(can!=null);
        if(n<=2) throw new RuntimeException("n must be > 2 in call to drawNSidePolygon");
        setStyle(fill);
        can.drawPath(getNSidePolygonPath(n,center_pos,rad), paint);
    }
    //Sets the fonts to be used in the following drawText calls
    @Override
    public void setFont(FontEng f){
        this.font = (FontAndroid) f;
    }
    private void setFontStyle(FontAndroid f){
        paint.setTextSize(f.getFontSize());
        paint.setTypeface(f.getTypeface());
    }
    @Override
    public void drawText(String text, Vec2 pos){
        assert(can!=null);
        setStyle(true);
        setFontStyle(font);

        Paint.FontMetrics metrics = paint.getFontMetrics();

        float x = pos.x - paint.measureText(text) / 2f;
        float y = pos.y - (metrics.ascent + metrics.descent) / 2f;

        can.drawText(text, x, y, paint);
    }
    public void calculateInverseMatrix(){
        if (!viewportMatrix.invert(inverseViewportMatrix)) {
            inverseViewportMatrix.reset();
        }
    }
    @Override
    public Vec2 getPointInWindowPos(Vec2 pos) {
        float[] point = {
                pos.x,
                pos.y
        };

        inverseViewportMatrix.mapPoints(point);

        return new Vec2(point[0], point[1]);
    }
}
