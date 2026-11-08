package com.example.game.testing.EC.Component.shop;

import com.example.engine.GraphicsInterface;
import com.example.engine.ImageEng;
import com.example.game.testing.Systems.Shop.ColorManager;
import com.example.game.testing.Systems.Shop.SkinManager;
import com.example.utils.Vec2;

import org.json.JSONObject;

import java.util.Objects;

public class SpriteBuyable extends Buyable {
    String spritePath;
    ImageEng sprite;
    SkinManager.Images imageFor;
    public SpriteBuyable(String fileName, int price, GraphicsInterface g, String imagePath, SkinManager.Images imageFor){
        super(fileName,BuyableType.skin,price);
        this.spritePath = imagePath;
        if(this.spritePath!=null)
            sprite = g.createImage(spritePath);
        else sprite = null;
        this.imageFor = imageFor;
    }

    Vec2 pos=new Vec2();
    Vec2 size=new Vec2();

    public static Buyable deserializeSkin(String fileName,int price, GraphicsInterface g, JSONObject obj) {
        return new SpriteBuyable(
            fileName,
            price,
            g,
            obj.isNull("path")?null: obj.getString("path"),
            SkinManager.Images.valueOf(obj.getString("imageFor"))
        );
    }

    @Override
    public JSONObject serialize(){
        JSONObject jo = super.serialize();
        jo.put("path",spritePath);
        jo.put("imageFor", imageFor.toString());
        return jo;
    }
    @Override
    public void render(GraphicsInterface g, float x, float y, float sizeX, float sizeY) {
        pos.x = x; pos.y = y;
        size.x = sizeX; size.y = sizeY;
        if(sprite == null){
            g.drawText("COLOR",pos,size);
            return;
        }
        g.drawImage(sprite, pos, size);
    }

    @Override
    public void equip() {
        SkinManager.getInstance().setImage(imageFor,sprite);
    }
}
