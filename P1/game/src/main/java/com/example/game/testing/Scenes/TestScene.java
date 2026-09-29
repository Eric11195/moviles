package com.example.game.testing.Scenes;

import com.example.engine.ColorEng;
import com.example.engine.Engine;
import com.example.engine.GraphicsInterface;
import com.example.engine.ImageEng;
import com.example.engine.Scene;

public class TestScene implements Scene {
    ImageEng testImage;
    @Override
    public void start(Engine eng){
        GraphicsInterface g = eng.getGraphics();
        testImage = g.createImage("test_image.jpg");
        g.setFontSize(128);
    }

    @Override
    public void update(Engine eng, double dt) {

    }

    @Override
    public void render(GraphicsInterface g, double dt) {
        g.clear(new ColorEng(0,0,255,255));
        g.setColor(new ColorEng(255,255,255,255));
        g.drawCircle(50,50,50,true);
        g.drawNSidePolygon(6, 250,50,50,true);
        g.drawRectangle(0,100,100,100,true);
        g.setStrokeWidth(10);
        g.drawCircle(150,50,50,false);
        g.drawNSidePolygon(3,350,50,50,false);
        g.drawRectangle(100,100,100,100,false);
        g.drawLine(200,100,300,200);

        //g.save();
        //g.rotate(300,300,145);
        //g.drawImage(testImage, 300,300,100,100);
        //g.restore();

        g.drawText("Miau", 200,400);
    }
}
