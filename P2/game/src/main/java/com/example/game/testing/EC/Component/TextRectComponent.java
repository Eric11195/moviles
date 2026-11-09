package com.example.game.testing.EC.Component;

import com.example.engine.ColorEng;
import com.example.engine.GraphicsInterface;
import com.example.game.testing.EC.ComponentId;
import com.example.game.testing.EC.mcd_Component;
import com.example.game.testing.EC.mcd_Entity;
import com.example.game.testing.Systems.Shop.ColorManager;
import com.example.game.testing.Systems.Shop.FontManager;

public class TextRectComponent implements mcd_Component {
    String text;
    ColorEng textColor = ColorManager.getInstance().getColor(ColorManager.Colors.TEXT_COLOR);
    public TextRectComponent(String text, ColorEng textColor){
        this.text = text;
        this.textColor = textColor;
    }
    public TextRectComponent(String text){
        this.text = text;
    }
    @Override
    public void update(mcd_Entity ent, float t, float dt) {}

    @Override
    public void render(mcd_Entity ent, GraphicsInterface g) {
        TransformComponent tr = (TransformComponent)ent.getComponent(ComponentId.TRANSFORM_COMPONENT);
        g.setColor(textColor);
        g.setFont(FontManager.getInstance().getFont(FontManager.Fonts.BUTTON_FONT));
        g.drawText(text,tr.pos,tr.size);
    }
    public void setText(String s){
        text = s;
    }
}
