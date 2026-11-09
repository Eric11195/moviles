package com.example.game.testing.Systems;

import com.example.engine.GraphicsInterface;
import com.example.game.testing.Scenes.mcd_Scene;
import com.example.game.testing.Systems.Shop.FontManager;
import com.example.game.testing.utils.ColorLibrary;
import com.example.utils.Vec2;

public class CoinSystem implements mcd_System{
    static CoinSystem instance = null;

    public static CoinSystem getInstance(){
        if (instance == null) instance = new CoinSystem();
        return instance;
    }

    public static void release(){
        instance = null;
    }

    int totalValue = 0;

    public void addValue(int _val){
        totalValue += _val;
    }
    public void reset(){
        totalValue = 0;
    }

    @Override
    public void update(mcd_Scene scene, float t, float dt) {

    }
    public int getCoins(){
        return totalValue;
    }
    static public final Vec2 size = new Vec2(100,50);
    static public final Vec2 pos = new Vec2(0,30);
    @Override
    public void render(GraphicsInterface g) {
        String text = String.valueOf(totalValue);
        g.setColor(ColorLibrary.Gold);
        g.setFont(FontManager.getInstance().getFont(FontManager.Fonts.COIN_N_DISTANCE_COUNT));
        g.drawText(text, pos,size);
    }
}
