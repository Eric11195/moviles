package com.example.game.testing.EC.Component.shop;

import com.example.engine.FontEng;
import com.example.engine.GraphicsInterface;
import com.example.engine.IFileStorage;
import com.example.engine.ImageEng;
import com.example.game.testing.Scenes.ShopScene;
import com.example.game.testing.Systems.Shop.ColorManager;
import com.example.game.testing.Systems.Shop.FontManager;
import com.example.game.testing.Systems.Shop.SkinManager;
import com.example.game.testing.utils.ColorLibrary;
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

    public static Buyable deserializeFont(String fileName, String tabName, int price, GraphicsInterface g, JSONObject obj) {
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
    public boolean selected() {
        FontEng f = FontManager.getInstance().getFont(fontFor);
        return Objects.equals(f.fileName, font.fileName) &&
                f.fontSize == font.fontSize;
    }

    @Override
    public void render(GraphicsInterface g, float x, float y, float sizeX, float sizeY) {
        pos.x = x; pos.y = y;
        size.x = sizeX; size.y = sizeY;

        g.setColor(ColorLibrary.Black);
        g.setFont(font);
        g.drawText(this.fontFor.toString() ,pos.add(new Vec2(0,0.2f*size.y)), size.add(new Vec2(0,-size.y*0.2f)));
    }

    @Override
    public void equip(IFileStorage fs) {
        if(!this.selected())
            FontManager.getInstance().setFont(fs,fontFor,font);
    }
}
