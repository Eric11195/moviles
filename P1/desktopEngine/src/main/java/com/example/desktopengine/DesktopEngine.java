package com.example.desktopengine;

import java.util.function.Consumer;
import com.example.engine.Engine;

public class DesktopEngine extends Engine{
    public DesktopEngine(int width, int height){
        super(new GraphicsDesktop(), new InputDesktop());
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
}
