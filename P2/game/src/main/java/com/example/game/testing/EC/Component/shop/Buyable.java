package com.example.game.testing.EC.Component.shop;

import com.example.engine.GraphicsInterface;

public abstract class Buyable {
    public int price;
    public abstract void render(
        GraphicsInterface g,
        float x,
        float y,
        float sizeX,
        float sizeY
    );
}
