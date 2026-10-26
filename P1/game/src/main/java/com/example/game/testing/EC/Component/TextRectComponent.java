package com.example.game.testing.EC.Component;

import com.example.engine.ColorEng;
import com.example.engine.GraphicsInterface;
import com.example.game.testing.EC.ComponentId;
import com.example.game.testing.EC.mcd_Component;
import com.example.game.testing.EC.mcd_Entity;

public class TextRectComponent implements mcd_Component {
    String text;
    ColorEng textColor;
    public TextRectComponent(String text, ColorEng textColor){
        this.text = text;
        this.textColor = textColor;
    }
    @Override
    public void update(mcd_Entity ent, float t, float dt) {}

    @Override
    public void render(mcd_Entity ent, GraphicsInterface g) {
        TransformComponent tr = (TransformComponent)ent.getComponent(ComponentId.TRANSFORM_COMPONENT);
        g.setColor(textColor);
        g.drawText(text,tr.pos,tr.size);
    }
}
