package com.example.game.testing.EC.Component;

import com.example.engine.ColorEng;
import com.example.engine.GraphicsInterface;
import com.example.game.testing.EC.ComponentId;
import com.example.game.testing.EC.mcd_Component;
import com.example.game.testing.EC.mcd_Entity;

public class DrawSingleRectComponent implements mcd_Component {
    public ColorEng color;
    public DrawSingleRectComponent(ColorEng c){
        this.color = c;
    }

    @Override
    public void update(mcd_Entity ent, float t, float dt) {}

    @Override
    public void render(mcd_Entity ent, GraphicsInterface g) {
        TransformComponent tr = (TransformComponent) ent.getComponent(ComponentId.TRANSFORM_COMPONENT);
        g.setColor(color);
        g.drawRectangle(tr.pos,tr.size,true);
    }
}
