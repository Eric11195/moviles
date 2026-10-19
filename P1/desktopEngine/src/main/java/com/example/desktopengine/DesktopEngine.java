package com.example.desktopengine;

import com.example.desktopengine.audio.AudioDesktop;
import com.example.engine.Engine;
import com.example.engine.GraphicsInterface;

public class DesktopEngine extends Engine{
    public DesktopEngine(int width, int height){
        super(new GraphicsDesktop(width, height), new InputDesktop(), new AudioDesktop(), new FileStorageDesktop());
        ((InputDesktop)getInput()).registerCanvas(((GraphicsDesktop)getGraphics()).getCanvas());
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
    protected void render(GraphicsInterface g){
        GraphicsDesktop gd = (GraphicsDesktop) g;
        do {
            do {
                try {
                    gd.prepareFrame();
                    this.getCurrentScene().render(g);
                }
                finally {
                    gd.endFrame();
                    //Elimina el contexto gráfico y libera recursos del sistema realacionado
                }
            } while(gd.buf.contentsRestored());
            gd.buf.show();
        } while(gd.buf.contentsLost());
    }
}
