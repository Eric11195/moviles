package com.example.game.testing;

import com.example.engine.EngColor;
import com.example.engine.Engine;
import com.example.engine.GraphicsInterface;

public class testForScrapping {
    public static void renderTest(Engine eng){
        //DRAWING SIMPLE SHAPES
        GraphicsInterface g = eng.getGraphics();
        g.startRender();
        g.clear(new EngColor(0,0,255,255));
        g.setColor(new EngColor(255,255,255,255));
        g.drawCircle(50,50,50,true);
        g.drawNSidePolygon(6, 250,50,50,true);
        g.drawRectangle(0,100,100,100,true);
        g.setStrokeWidth(10);
        g.drawCircle(150,50,50,false);
        g.drawNSidePolygon(3,350,50,50,false);
        g.drawRectangle(100,100,100,100,false);
        g.drawLine(200,100,300,200);

        //CREATE IMAGE
        //ImageAndroid img = new ImageAndroid(eng.);
        //g.drawImage();
        g.endRender();
    }
}
