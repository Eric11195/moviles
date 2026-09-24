package com.example.desktopengine;

import com.example.engine.Engine;
import com.example.engine.TouchEvent;

import java.util.ArrayList;

public class DesktopEngine extends Engine{
    public DesktopEngine(int width, int height){
        super(new GraphicsDesktop(), new InputDesktop());
        InputDesktop input = (InputDesktop) getInput();
        input.registerFrame(((GraphicsDesktop)getGraphics()).getFrame());
    }
    @Override
    public void resume() {
        super.resume();
    }
    @Override
    public void pause() {
        super.pause();
    }
    @Override
    protected boolean correctlyResumedBoolean(){
        return ((GraphicsDesktop)this.getGraphics()).correcltyInitialized();
    }

    @Override
    protected void update() {
        super.update();
        ArrayList<TouchEvent> events = getInput().getTouchEvents();

        events.forEach( touchEvent -> {System.out.println("Event!");});
    }
}
