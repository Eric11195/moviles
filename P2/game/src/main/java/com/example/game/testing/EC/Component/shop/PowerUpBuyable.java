package com.example.game.testing.EC.Component.shop;

import com.example.engine.GraphicsInterface;
import com.example.engine.IFileStorage;
import com.example.engine.ImageEng;
import com.example.game.testing.Systems.Shop.PowerUpManager;
import com.example.game.testing.Systems.Shop.SkinManager;
import com.example.utils.Vec2;

import org.json.JSONObject;

import java.util.ArrayList;

public class PowerUpBuyable extends Buyable {
    int value;
    PowerUpManager.extraVars var;
    public PowerUpBuyable(String fileName, String tabName, int price, GraphicsInterface g, PowerUpManager.extraVars var, int value){
        super(fileName,tabName,BuyableType.powerUp,price);
        this.value = value;
        this.var = var;
        if(powerUpUpgradesImg == null) {
            powerUpUpgradesImg = new ArrayList<>();
            for(int i = 0; i <  PowerUpManager.extraVars.COUNT.ordinal(); ++i){
                powerUpUpgradesImg.add(g.createImage("img/"+PowerUpManager.extraVars.values()[i].toString()+".png"));
            }
        }
    }

    Vec2 pos=new Vec2();
    Vec2 size=new Vec2();

    public static Buyable deserializePUp(String fileName, String tabName, GraphicsInterface g, int price, JSONObject obj) {
        return new PowerUpBuyable(
            fileName,
            tabName,
            price,
            g,
            PowerUpManager.extraVars.valueOf(obj.getString("var")),
            obj.getInt("value")
        );
    }

    @Override
    public JSONObject serialize(){
        JSONObject jo = super.serialize();
        jo.put("var", var.toString());
        jo.put("value", value);
        return jo;
    }

    @Override
    public boolean selected() {
        return true;
    }
    private static ArrayList<ImageEng> powerUpUpgradesImg;

    @Override
    public void render(GraphicsInterface g, float x, float y, float sizeX, float sizeY) {
        pos.x = x; pos.y = y;
        size.x = sizeX; size.y = sizeY;
        g.drawImage(powerUpUpgradesImg.get(var.ordinal()), pos.add(new Vec2(0,0.2f*size.y)), size.add(new Vec2(0,-size.y*0.2f)));
        g.drawText("+"+Integer.toString(value),pos,size.add(new Vec2(0,-0.8f*size.y)));
    }

    @Override
    public void equip(IFileStorage fs) {}
    @Override
    public void buy(IFileStorage fs){
        PowerUpManager.getInstance().addToVar(fs,var, value);
    }
}
