package com.example.game.testing.EC.Component.UI;

import com.example.engine.ColorEng;
import com.example.engine.GraphicsInterface;
import com.example.engine.TouchEvent;
import com.example.engine.TouchEventType;
import com.example.game.testing.EC.Component.TransformComponent;
import com.example.game.testing.EC.ComponentId;
import com.example.game.testing.EC.mcd_Component;
import com.example.game.testing.EC.mcd_Entity;
import com.example.game.testing.Systems.Shop.ColorManager;
import com.example.game.testing.utils.ColorLibrary;
import com.example.game.testing.utils.SoundLibrary;
import com.example.utils.Vec2;

// All buttons have same color, but different text
public class ButtonComponent implements mcd_Component {
    public interface ButtonClickFunction{
        void onClick();
    }
    public void setIdleColor(ColorEng clr){
        idleColor = clr.clone();
    }
    public void onClick(){}
    ButtonClickFunction func;
    boolean hovering = false;
    boolean clicked = false;
    protected ColorEng idleColor = ColorManager.getInstance().getColor(ColorManager.Colors.BUTTONS_COLOR);
    protected ColorEng hoverColor = ColorLibrary.Green.clone();
    protected ColorEng clickedColor = ColorLibrary.Blue.clone();
    public ButtonComponent(ButtonClickFunction onClickFunc){
        func = onClickFunc;
    }

    @Override
    public void update(mcd_Entity ent, float t, float dt) {
        clicked = false;
        hovering = false;
        //Detect clic
        if(clickedDown(ent) && func != null) {
            clicked=true;
            ent.getEngine().getAudio().playSound(SoundLibrary.getSound(SoundLibrary.SoundsId.BUTTON));
            func.onClick();
            this.onClick();
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
    }

    public boolean clickedDown(mcd_Entity ent){
        if(!ent.hasComponent(ComponentId.TRANSFORM_COMPONENT)) return false;
        TransformComponent tr = (TransformComponent) ent.getComponent(ComponentId.TRANSFORM_COMPONENT);
        //Set hover if it's the case
        for(TouchEvent evt : ent.getEngine().getInput().getTouchEvents()){
            //System.out.println("Event: "+evt.pos.x+" , "+evt.pos.y);
            if(inside(tr, evt.pos)) {
                hovering = true;
                if (evt.type == TouchEventType.DOWN){
                    return true;
                }
            }
        }
        return false;
    }
    public boolean inside(TransformComponent tr, Vec2 ptr){
        //System.out.println("Position ("+tr.pos.x+","+tr.pos.y+")  Size ("+tr.size.x+","+tr.size.y+") -> Ptr ("+ptr.x+','+ptr.y+')');
        return tr.pos.x < ptr.x && tr.pos.x+tr.size.x >ptr.x &&
                tr.pos.y < ptr.y && tr.pos.y +tr.size.y > ptr.y;
    }
}
