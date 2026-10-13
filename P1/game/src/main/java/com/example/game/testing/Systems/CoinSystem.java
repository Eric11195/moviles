package com.example.game.testing.Systems;

import com.example.engine.GraphicsInterface;
import com.example.game.testing.Scenes.mcd_Scene;
import com.example.game.testing.utils.ColorLibrary;
import com.example.game.testing.utils.FontLibrary;
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

    @Override
    public void update(mcd_Scene scene, float t, float dt) {

    }

    @Override
    public void render(GraphicsInterface g) {
        String text = String.valueOf(totalValue);
        g.setColor(ColorLibrary.Gold);
        g.setFont(FontLibrary.getFont(FontLibrary.FontName.HEADER));
        g.drawText(text, new Vec2((float) g.getWidth() / 2 , 100));
    }
}
