package com.example.game.testing.EC.Component.shop;

import com.example.engine.GraphicsInterface;
import com.example.engine.IFileStorage;
import com.example.game.testing.Systems.Shop.LevelKeyManager;
import com.example.game.testing.Systems.Shop.ShopManager;
import com.example.game.testing.utils.ColorLibrary;
import com.example.utils.Vec2;

import org.json.JSONObject;

public class LevelKeyBuyable extends Buyable {
    public String description;
    int key;
    LevelKeyBuyable(String fileName, String tabName, int price, String description, int key){
        super(fileName,tabName,BuyableType.levelKey, price);
        this.description = description;
        this.key = key;
    }
    public static LevelKeyBuyable deserializeLevelKey(String fileName, String tabName, int price, JSONObject jo){
        return new LevelKeyBuyable(fileName,tabName,price,
                jo.getString("description"),jo.getInt("key")
        );
    }

    Vec2 pos = new Vec2();
    Vec2 size = new Vec2();
    @Override
    public void render(GraphicsInterface g, float x, float y, float sizeX, float sizeY) {
        pos.x = x;
        pos.y = y;
        size.x = sizeX;
        size.y = sizeY;
        g.setColor(ColorLibrary.Black);
        g.drawText(description,new Vec2(x,y),new Vec2(sizeX,sizeY));
    }

    @Override
    public void equip(IFileStorage fs) {}

    @Override
    public boolean selected() {
        return false;
    }
    public void buy(IFileStorage fs){
        super.buy(fs);
        LevelKeyManager.getInstance().addKey(this.key);
    }
}
