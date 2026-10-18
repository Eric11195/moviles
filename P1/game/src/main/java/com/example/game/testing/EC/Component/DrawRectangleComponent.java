package com.example.game.testing.EC.Component;


import com.example.engine.ColorEng;
import com.example.game.testing.EC.ComponentId;
import com.example.game.testing.EC.mcd_Component;
import com.example.engine.GraphicsInterface;
import com.example.game.testing.EC.mcd_Entity;

public class DrawRectangleComponent implements mcd_Component {
    public ColorEng color;
    DrawRectangleComponent()
    {

        color = ColorEng.RED;

    }

    @Override
    public void render(mcd_Entity ent, GraphicsInterface g) {
        assert(ent.hasComponent(ComponentId.TRANSFORM_COMPONENT));
        TransformComponent tr = (TransformComponent) ent.getComponent(ComponentId.TRANSFORM_COMPONENT);
        g.setColor(color);
        g.drawRectangle(tr.pos,tr.size,true);
    }

    @Override
    public void update(mcd_Entity ent, float t, float dt) {

    }
}
