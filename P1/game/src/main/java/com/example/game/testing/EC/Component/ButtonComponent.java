package com.example.game.testing.EC.Component;

import com.example.engine.ColorEng;
import com.example.engine.GraphicsInterface;
import com.example.engine.TouchEvent;
import com.example.engine.TouchEventType;
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
    public void update(mcd_Entity ent, float t, float dt) {
        clicked = false;
        hovering = false;
        //Detect clic
        if(clickedDown(ent) && func != null) {
            clicked=true;
            func.onClick();
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

    public boolean clickedDown(mcd_Entity ent){
        if(!ent.hasComponent(ComponentId.TRANSFORM_COMPONENT)) return false;
        TransformComponent tr = (TransformComponent) ent.getComponent(ComponentId.TRANSFORM_COMPONENT);
        //Set hover if its the case
        for(TouchEvent evt : ent.getEngine().getInput().getTouchEvents()){
            //System.out.println("Event: "+evt.pos.x+" , "+evt.pos.y);
            if(inside(tr, evt.pos)) {
                hovering = true;
                if (evt.type == TouchEventType.UP){
                    return true;
                }
            }
        }
        return false;
    }
    public boolean inside(TransformComponent tr, Vec2 ptr){
        System.out.println("Position ("+tr.pos.x+","+tr.pos.y+")  Size ("+tr.size.x+","+tr.size.y+") -> Ptr ("+ptr.x+','+ptr.y+')');
        return tr.pos.x < ptr.x && tr.pos.x+tr.size.x >ptr.x &&
                tr.pos.y < ptr.y && tr.pos.y +tr.size.y > ptr.y;
    }
}
