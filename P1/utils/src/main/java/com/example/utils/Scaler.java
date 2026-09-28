package com.example.utils;

public interface Scaler {
    public void translate(int x, int y);
    public void setOffset(int x, int y);
    public void scale(double x_mult, double y_mult);
    public void set_virtual_resolution(int width, int height);
    public void rotate(float additive_rotation_in_degrees);
    public void setRotation(float new_rotation_in_degrees);
    public void cleanupAllTransformations();
    //Rotation, offset, scale
    public void cleanupRegularTransformations();
    public CoordRect transformRect(CoordRect cr);
}
