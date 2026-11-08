package com.example.game.testing.EC.Component.shop;

import static com.example.game.testing.utils.JSON_HELPER.toJSON;

import com.example.engine.ColorEng;
import com.example.engine.GraphicsInterface;
import com.example.engine.ImageEng;
import com.example.game.testing.Systems.Shop.ColorManager;
import com.example.game.testing.utils.JSON_HELPER;
import com.example.utils.Vec2;

import org.json.JSONObject;

public class ColorBuyable extends Buyable {
    ColorEng color;
    ColorManager.Colors colorFor;
    public ColorBuyable(String fileName,int price, ColorManager.Colors colorFor, ColorEng color){
        super(fileName,BuyableType.color, price);
        this.color = color;
        this.colorFor = colorFor;
    }
    Vec2 pos = new Vec2();
    Vec2 size = new Vec2();
    @Override
    public void render(GraphicsInterface g, float x, float y, float sizeX, float sizeY) {
        pos.x = x; pos.y = y;
        size.x = sizeX; size.y = sizeY;
        g.setColor(this.color);
        g.drawRectangle(pos,size,true);
    }

    @Override
    public void equip() {
        ColorManager.getInstance().setColor(colorFor, color);
    }

    public static ColorBuyable deserializeColor(String fileName, int price, JSONObject obj){
        return new ColorBuyable(
            fileName,
            price,
            ColorManager.Colors.valueOf(obj.getString("colorFor")),
            JSON_HELPER.colorFromJSON(obj)
        );
    }

    @Override
    public JSONObject serialize() {
        JSONObject jo = super.serialize();
        toJSON(color, jo);
        jo.put("colorFor",colorFor.toString());
        return jo;
    }
}
