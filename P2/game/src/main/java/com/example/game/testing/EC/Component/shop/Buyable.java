package com.example.game.testing.EC.Component.shop;

import com.example.engine.GraphicsInterface;
import com.example.game.testing.Systems.Shop.ColorManager;
import com.example.game.testing.utils.mcd_Serializable;

import org.json.JSONObject;

public abstract class Buyable implements mcd_Serializable {
    BuyableType type;
    /**
     * If price == -1. Then this item has already been bought
     */
    public int price;
    public Buyable(BuyableType type, int price){
        this.type = type;
        this.price = price;
    }
    public abstract void render(
        GraphicsInterface g,
        float x,
        float y,
        float sizeX,
        float sizeY
    );
    protected static enum BuyableType{
        skin,
        color,
        font
    }
    public abstract void equip();
    @Override
    public JSONObject serialize(){
        JSONObject jo = new JSONObject();
        jo.put("price", this.price);
        jo.put("type", this.type.toString());
        return jo;
    }
    public static Buyable deserialize(GraphicsInterface g, JSONObject obj){
        int price = obj.getInt("price");
        switch(BuyableType.valueOf(obj.getString("type"))){
            case skin:
                return SpriteBuyable.deserializeSkin(price,g,obj);
            case color:
                return ColorBuyable.deserializeColor(price,obj);
            case font:
                throw new RuntimeException("Unimplemented yet");
            default:
                throw new RuntimeException("Invalid type");
        }
    }

}
