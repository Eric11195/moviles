package com.example.game.testing.EC.Component.UI;

import com.example.engine.GraphicsInterface;
import com.example.game.testing.EC.Component.TransformComponent;
import com.example.game.testing.EC.ComponentId;
import com.example.game.testing.EC.mcd_Component;
import com.example.game.testing.EC.mcd_Entity;
import com.example.game.testing.Systems.RubySystem;
import com.example.game.testing.utils.ColorLibrary;

public class RubyDisplayComponent implements mcd_Component {
    @Override
    public void update(mcd_Entity ent, float t, float dt) {
        TransformComponent tr = (TransformComponent) ent.getComponent(ComponentId.TRANSFORM_COMPONENT);
    }

    @Override
    public void render(mcd_Entity ent, GraphicsInterface g) {
        TransformComponent tr = (TransformComponent) ent.getComponent(ComponentId.TRANSFORM_COMPONENT);
        g.setColor(ColorLibrary.HotPink);
        g.setFontSize((int) tr.size.y);
        int rubies = RubySystem.getRubies();
        g.drawText(Integer.toString(rubies),tr.pos,tr.size);
    }
}
