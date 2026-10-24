package com.example.game.testing.Systems;

import com.example.engine.GraphicsInterface;
import com.example.game.testing.Scenes.mcd_Scene;
import com.example.game.testing.utils.ColorLibrary;
import com.example.game.testing.utils.FontLibrary;
import com.example.utils.Vec2;

public class DistanceSystem implements mcd_System{
    static DistanceSystem instance = null;

    public static DistanceSystem getInstance(){
        if (instance == null) instance = new DistanceSystem();
        return instance;
    }

    public static void release(){
        instance = null;
    }

    float totalValue = 0;

    public void addValue(float _val){
        totalValue += _val;
    }
    public void reset(){
        totalValue = 0;
    }

    @Override
    public void update(mcd_Scene scene, float t, float dt) {

    }
    public float getDistance(){
        return totalValue;
    }

    @Override
    public void render(GraphicsInterface g) {
        //render bar to goal
        String text = String.valueOf((int)totalValue/100);
        g.setColor(ColorLibrary.Red);
        g.setFont(FontLibrary.getFont(FontLibrary.FontName.HEADER));
        g.drawText(text, new Vec2((float) 0 , 100));
    }
}
