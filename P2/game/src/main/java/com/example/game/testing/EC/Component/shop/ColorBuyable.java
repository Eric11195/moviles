package com.example.game.testing.EC.Component.shop;

import com.example.engine.ColorEng;
import com.example.engine.GraphicsInterface;
import com.example.engine.ImageEng;
import com.example.utils.Vec2;

public class ColorBuyable extends Buyable {
ColorEng color;
    ColorBuyable(ColorEng color){
        this.color = color;
    }
    Vec2 pos;
    Vec2 size;
    @Override
    public void render(GraphicsInterface g, float x, float y, float sizeX, float sizeY) {
        pos.x = x; pos.y = y;
        size.x = sizeX; size.y = sizeY;
        g.drawRectangle(pos,size,true);
    }
}
