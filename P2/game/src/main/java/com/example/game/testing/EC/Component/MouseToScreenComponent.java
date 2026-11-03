package com.example.game.testing.EC.Component;

import com.example.engine.ColorEng;
import com.example.engine.Engine;
import com.example.engine.GraphicsInterface;
import com.example.engine.TouchEvent;
import com.example.game.testing.EC.mcd_Component;
import com.example.game.testing.EC.mcd_Entity;
import com.example.utils.Vec2;

import java.awt.Color;
import java.util.ArrayList;

public class MouseToScreenComponent implements mcd_Component {
    ArrayList<Vec2> inputPos = new ArrayList<>();
    @Override
    public void update(mcd_Entity ent, float t, float dt) {
        Engine eng = ent.getEngine();
        for(TouchEvent evt :eng.getInput().getTouchEvents()){
            inputPos.add(evt.pos);
        }
    }
    ColorEng dotColor = new ColorEng(0,255,0,255);
    @Override
    public void render(mcd_Entity ent, GraphicsInterface g) {
        g.setColor(dotColor);
        for(Vec2 v : inputPos){
            g.drawCircle(v,5,true);
        }
    }
}
