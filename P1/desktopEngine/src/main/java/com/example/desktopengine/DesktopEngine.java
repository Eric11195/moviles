package com.example.desktopengine;

import com.example.desktopengine.audio.AudioDesktop;
import com.example.engine.Engine;

public class DesktopEngine extends Engine{
    public DesktopEngine(int width, int height){
        super(new GraphicsDesktop(), new InputDesktop(), new AudioDesktop());
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
