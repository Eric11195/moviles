package com.example.game.testing.EC.Component.shop;

import com.example.engine.GraphicsInterface;
import com.example.engine.ImageEng;
import com.example.utils.Vec2;

public class SpriteBuyable extends Buyable {
    String spritePath;
    ImageEng sprite;
    public SpriteBuyable(int price, GraphicsInterface g, String imagePath){
        super(price);
        this.spritePath = imagePath;
        sprite = g.createImage(spritePath);
    }

    Vec2 pos;
    Vec2 size;
    @Override
    public void render(GraphicsInterface g, float x, float y, float sizeX, float sizeY) {
        pos.x = x; pos.y = y;
        size.x = sizeX; size.y = sizeY;
        g.drawImage(sprite, pos, size);
    }
}
