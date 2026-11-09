package com.example.game.testing.EC.Component.shop;

import com.example.engine.FontEng;
import com.example.engine.GraphicsInterface;
import com.example.engine.ImageEng;
import com.example.game.testing.Scenes.ShopScene;
import com.example.game.testing.Systems.Shop.ColorManager;
import com.example.game.testing.Systems.Shop.FontManager;
import com.example.game.testing.Systems.Shop.SkinManager;
import com.example.game.testing.utils.JSON_HELPER;
import com.example.utils.Vec2;

import org.json.JSONObject;

import java.awt.Font;
import java.util.Objects;

public class FontBuyable extends Buyable {
    FontEng font;
    FontManager.Fonts fontFor;
    public FontBuyable(String fileName, String tabName, int price, GraphicsInterface g, FontEng f, FontManager.Fonts fontFor){
        super(fileName,tabName,BuyableType.font,price);
        this.font = f;
        this.fontFor = fontFor;
    }

    Vec2 pos=new Vec2();
    Vec2 size=new Vec2();

    public static Buyable deserializeSkin(String fileName, String tabName, int price, GraphicsInterface g, JSONObject obj) {
        FontEng f = JSON_HELPER.fontFromJSON(g,obj);
        return new FontBuyable(
                fileName,
                tabName,
                price,
                g,
                f,
                FontManager.Fonts.valueOf(obj.getString("fontFor"))
        );
    }

    @Override
    public JSONObject serialize(){
        JSONObject jo = super.serialize();
        JSON_HELPER.toJSON(this.font, jo);
        jo.put("fontFor", fontFor.toString());
        return jo;
    }
    @Override
    public void render(GraphicsInterface g, float x, float y, float sizeX, float sizeY) {
        pos.x = x; pos.y = y;
        size.x = sizeX; size.y = sizeY;
        g.drawText("Example", pos,size);
    }

    @Override
    public void equip() {
        FontManager.getInstance().setFont(fontFor,font);
    }
}
