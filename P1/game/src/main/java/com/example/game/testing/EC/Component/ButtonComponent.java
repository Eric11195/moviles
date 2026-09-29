package com.example.game.testing.EC.Component;

import com.example.engine.ColorEng;
import com.example.engine.GraphicsInterface;
import com.example.game.testing.EC.mcd_Component;

import java.awt.Button;

// All buttons have same
public class ButtonComponent implements mcd_Component {
    interface ButtonClickFunction{
        void onClick();
    }
    ButtonClickFunction func;
    boolean hovering = false;
    boolean clicked = false;
    String button_text;
    ColorEng clr;
    public ButtonComponent(String text, ColorEng colorButton, ButtonClickFunction onClickFunc){
        func = onClickFunc;
    }

    @Override
    public void update(double dt) {
        clicked = false;
        hovering = false;
        //Detect clic
        if(clickedDown() && func != null) {
            func.onClick();
            clicked=true;
        }
    }

    @Override
    public void render(GraphicsInterface g) {
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
