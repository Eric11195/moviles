package com.example.utils;

public class Scale implements Scaler{
    double rotation=0;
    public double getRotation(){return this.rotation;}
    Vec2 offset=new Vec2(0,0);
    Vec2 scale=new Vec2(1,1);

    int expected_resolution_width=1280, expected_resolution_height=720;
    int virtual_resolution_width=1280, virtual_resolution_height=720;

    @Override
    public void translate(int x, int y){
        offset.x += x;
        offset.y += y;
    }
    @Override
    public void setOffset(int x, int y){
        offset.x = x;
        offset.y = y;
    }
    @Override
    public void scale(double x_mult, double y_mult){
        scale.x *= x_mult;
        scale.y *= y_mult;
    }
    @Override
    public void set_virtual_resolution(int width, int height){
        virtual_resolution_height=height;
        virtual_resolution_width=width;
        calculate_scale_with_current_resolutions();
    }
    @Override
    public void rotate(float additive_rotation_in_degrees){
        rotation += Math.PI * additive_rotation_in_degrees/180;
    }
    @Override
    public void setRotation(float new_rotation_in_degrees){
        rotation = Math.PI * new_rotation_in_degrees/180;
    }
    private void calculate_scale_with_current_resolutions(){
        scale.x = (double)virtual_resolution_width / expected_resolution_width;
        scale.y = (double)virtual_resolution_height / expected_resolution_height;
    }
    @Override
    public void cleanupAllTransformations(){
        virtual_resolution_width=1280; virtual_resolution_height=720;
        scale.x=scale.y=1;
        offset.x=offset.y=0;
        rotation = 0;
    }
    @Override
    //Rotation, offset, scale
    public void cleanupRegularTransformations(){
        calculate_scale_with_current_resolutions();
        offset.x=offset.y=0;
        rotation=0;
    }

    public CoordRect transformRect(CoordRect cr) {
        return cr.mult(scale).rotateFromCenter(rotation).add(offset);
    }
}
