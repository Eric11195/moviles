package com.example.game.testing.EC.Component;

import com.example.engine.ColorEng;
import com.example.engine.GraphicsInterface;
import com.example.game.testing.EC.ComponentId;
import com.example.game.testing.EC.mcd_Component;
import com.example.game.testing.EC.mcd_Entity;
import com.example.utils.Vec2;

import java.awt.Button;

import javax.xml.crypto.dsig.Transform;

// All buttons have same color, but different text
public class ButtonComponent implements mcd_Component {
    public interface ButtonClickFunction{
        void onClick();
    }
    ButtonClickFunction func;
    boolean hovering = false;
    boolean clicked = false;
    public String button_text;
    private final static ColorEng idleColor = new ColorEng(255,0,0, 255);
    private final static ColorEng hoverColor = new ColorEng(0,255,0, 255);
    private final static ColorEng clickedColor = new ColorEng(255,255,255, 255);
    private final static ColorEng textColor = new ColorEng(0,0,0, 255);
    public ButtonComponent(String text, ButtonClickFunction onClickFunc){
        button_text = text;
        func = onClickFunc;
    }

    @Override
    public void update(mcd_Entity ent, double dt) {
        clicked = false;
        hovering = false;
        //Detect clic
        if(clickedDown() && func != null) {
            func.onClick();
            clicked=true;
        }
    }

    @Override
    public void render(mcd_Entity ent,  GraphicsInterface g) {
        if(!ent.hasComponent(ComponentId.TRANSFORM_COMPONENT)) return;
        TransformComponent tr = (TransformComponent) ent.getComponent(ComponentId.TRANSFORM_COMPONENT);
        if(clicked){
            g.setColor(clickedColor);
        }
        else if(hovering){
            g.setColor(hoverColor);
        }else{
            g.setColor(idleColor);
        }
        g.drawRoundRectangle(tr.pos,tr.size,12.0f, true);
        g.setColor(textColor);
        Vec2 final_pos = new Vec2(tr.pos.x+tr.size.x*0.5f, tr.pos.y+tr.size.y*0.75f);
        g.drawText(button_text,final_pos);
    }

    public boolean clickedDown(){
        //Set hover if its the case
        return false;
    }
}
