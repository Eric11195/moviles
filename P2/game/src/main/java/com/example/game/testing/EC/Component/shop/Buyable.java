package com.example.game.testing.EC.Component.shop;

import com.example.engine.GraphicsInterface;
import com.example.engine.IFileStorage;
import com.example.game.testing.Scenes.ShopScene;
import com.example.game.testing.Systems.Shop.ColorManager;
import com.example.game.testing.utils.mcd_Serializable;

import org.json.JSONObject;

public abstract class Buyable implements mcd_Serializable {
    public final String fileName;
    public String tabName;
    public final BuyableType type;
    /**
     * If price == -1. Then this item has already been bought
     */
    public int price;
    public Buyable(String fileName, String tabName, BuyableType type, int price){
        this.fileName = fileName;
        this.tabName = tabName;
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
    public abstract void equip(IFileStorage fs);
    @Override
    public JSONObject serialize(){
        JSONObject jo = new JSONObject();
        jo.put("price", this.price);
        jo.put("type", this.type.toString());
        return jo;
    }
    public static Buyable deserialize(String fileName, String tabName, GraphicsInterface g, JSONObject obj){
        int price = obj.getInt("price");
        switch(BuyableType.valueOf(obj.getString("type"))){
            case skin:
                return SpriteBuyable.deserializeSkin(fileName,tabName,price,g,obj);
            case color:
                return ColorBuyable.deserializeColor(fileName,tabName,price,obj);
            case font:
                throw new RuntimeException("Unimplemented yet");
            default:
                throw new RuntimeException("Invalid type");
        }
    }
    public abstract boolean selected();

}
