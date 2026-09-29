package com.example.game.testing.EC.Component;

import com.example.engine.ColorEng;
import com.example.engine.GraphicsInterface;
import com.example.game.testing.EC.mcd_Component;
import com.example.game.testing.EC.mcd_Entity;

import java.awt.Button;

// All buttons have same color, but different text
public class ButtonComponent implements mcd_Component {
    interface ButtonClickFunction{
        void onClick();
    }
    ButtonClickFunction func;
    boolean hovering = false;
    boolean clicked = false;
    public String button_text;
    private final static ColorEng clr = new ColorEng(120,120,120, 255);
    public ButtonComponent(String text, ButtonClickFunction onClickFunc){
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
        if(clicked){

        }
        else if(hovering){

        }else{

        }
        renderButtonText();
    }
    public void renderButtonText(){

    }

    public boolean clickedDown(){
        //Set hover if its the case
        return false;
    }
}
