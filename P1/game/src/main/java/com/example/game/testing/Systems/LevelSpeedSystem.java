package com.example.game.testing.Systems;

import static com.example.game.testing.constants.Constants.LOGIC_WINDOW_SIZE;

import com.example.engine.GraphicsInterface;
import com.example.game.testing.Scenes.mcd_Scene;
import com.example.game.testing.utils.ColorLibrary;
import com.example.game.testing.utils.FontLibrary;
import com.example.utils.Vec2;

public class LevelSpeedSystem implements mcd_System{
    static LevelSpeedSystem instance = null;

    public static LevelSpeedSystem getInstance(){
        if (instance == null) instance = new LevelSpeedSystem();
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
    public float getSpeedMult(){
        return totalValue;
    }
    @Override
    public void render(GraphicsInterface g) {
    }
}
