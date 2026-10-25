package com.example.game.testing.EC.Component;


import com.example.engine.ColorEng;
import com.example.game.testing.EC.ComponentId;
import com.example.game.testing.EC.mcd_Component;
import com.example.engine.GraphicsInterface;
import com.example.game.testing.EC.mcd_Entity;
import com.example.game.testing.utils.ColorLibrary;
import com.example.utils.Vec2;

import java.util.List;

class Rectangle
{
    public Vec2 myPos,mySize;
  public  Rectangle(Vec2 pos, Vec2 size)
  {
    myPos = pos;
    mySize = size;
  }

};
public class DrawRectangleComponent implements mcd_Component {
    public ColorEng color;
    List<Rectangle> rectanglesToDraw;
    public DrawRectangleComponent() {
        color = ColorLibrary.Red.clone();
        rectanglesToDraw = new java.util.ArrayList<Rectangle>();
    }
    public void addRectangle(Vec2 pos, Vec2 size)
    {
        rectanglesToDraw.add(new Rectangle(pos,size));
    }

    @Override
    public void render(mcd_Entity ent, GraphicsInterface g) {
        g.setColor(color);
        for(int i=0;i<rectanglesToDraw.size();++i)
        {
            g.drawRectangle(rectanglesToDraw.get(i).myPos,rectanglesToDraw.get(i).mySize,true);
        }
    }
    public void clearRectangles()
    {
        rectanglesToDraw.clear();
    }

    @Override
    public void update(mcd_Entity ent, float t, float dt) {

    }
}
