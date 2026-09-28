package com.example.utils;

public class Vec2 {
    public double x, y;
    public Vec2(double x, double y){
        this.x=x;
        this.y=y;
    }
    public Vec2 add(Vec2 toAdd){
        this.x += toAdd.x;
        this.y += toAdd.y;
        return this;
    }
    public Vec2 mult(Vec2 toMult){
        this.x *= toMult.x;
        this.y *= toMult.y;
        return this;
    }
}
