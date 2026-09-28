package com.example.utils;

public class CoordRect {
    public Vec2 ul; // up left corner pos
    public Vec2 ur; // up right corner pos
    public Vec2 dl; // down left corner pos
    public Vec2 dr; // down right corner pos
    public CoordRect(Vec2 top_left, Vec2 down_right){
        this.ul = new Vec2(top_left.x, top_left.y);
        this.ur = new Vec2(down_right.x, top_left.y);
        this.dl = new Vec2(top_left.x, down_right.y);
        this.dr = new Vec2(down_right.x, down_right.y);
    }
    public CoordRect add(Vec2 toAdd){
        ul.add(toAdd);
        ur.add(toAdd);
        dl.add(toAdd);
        dr.add(toAdd);
        return this;
    }
    public CoordRect mult(Vec2 toMult){
        ul.mult(toMult);
        ur.mult(toMult);
        dl.mult(toMult);
        dr.mult(toMult);
        return this;
    }
    public CoordRect rotateFromCenter(double rotation){
        Vec2 center = new Vec2(
                (ul.x + dr.x) / 2.0,
                (ul.y + dr.y) / 2.0
        );
        double cos = Math.cos(rotation);
        double sin = Math.sin(rotation);
        rotatePoint(this.ul, center, cos,sin);
        rotatePoint(this.ur, center, cos,sin);
        rotatePoint(this.dl, center, cos,sin);
        rotatePoint(this.dr, center, cos,sin);
        return this;
    }
    private static void rotatePoint(Vec2 point, Vec2 center, double cos, double sin){
        // Position relative to center
        double x = point.x - center.x;
        double y = point.y - center.y;

        // Rotate
        double rotatedX = x * cos - y * sin;
        double rotatedY = x * sin + y * cos;

        // Move back to world coordinates
        point.x = rotatedX + center.x;
        point.y = rotatedY + center.y;
    }
}