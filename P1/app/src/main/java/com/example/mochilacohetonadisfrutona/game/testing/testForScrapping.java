package com.example.mochilacohetonadisfrutona.game.testing;

import android.graphics.Color;

import com.example.mochilacohetonadisfrutona.intermedium.Engine;
import com.example.mochilacohetonadisfrutona.intermedium.GraphicsInterface;

public class testForScrapping {
    public static void renderTest(Engine eng){
        GraphicsInterface g = eng.getGraphics();
        g.startRender();
        g.clear(Color.BLUE);
        g.endRender();
    }
}
