package com.example.game.testing.Systems;

import com.example.utils.Vec2;

import java.util.BitSet;

public class BoolMatrix {
    BitSet bitSet;
    int width,height;
    public BoolMatrix(int width, int height){
        this.width = width;
        this.height = height;
        this.bitSet = new BitSet(width*height);
        clear();
    }
    public void clear(){
        this.bitSet.clear();
    }
    private int getBitsetPos(int x, int y){
        return y*width+x;
    }
    private int getBitsetPos(Vec2 pt){
        return getBitsetPos((int)Math.round(pt.x),(int)Math.round(pt.y));
    }
    public boolean get(int x, int y){
        return this.bitSet.get(getBitsetPos(x,y));
    }
    public boolean get(Vec2 pt){
        return this.bitSet.get(getBitsetPos(pt));
    }
    public void set(int x, int y, boolean value){
        this.bitSet.set(getBitsetPos(x,y), value);
    }
    public void set(Vec2 pt, boolean value){
        this.bitSet.set(getBitsetPos(pt), value);
    }
    public void set(int x1, int y1, int x2, int y2, boolean value){
        for(int x = x1; x<=x2; ++x){
            for(int y = y1; y<=y2; ++y){
                this.bitSet.set(getBitsetPos(x1,y1), getBitsetPos(x2,y2), value);
            }
        }
    }
    public void set(Vec2 startPt,Vec2 endPt, boolean value){
        set(
            (int)Math.round(startPt.x),
            (int)Math.round(startPt.y),
            (int)Math.round(endPt.x),
            (int)Math.round(endPt.y),
            value
        );
    }
    public int getWidth(){
        return width;
    }
    public int getHeight(){
        return height;
    }
}
