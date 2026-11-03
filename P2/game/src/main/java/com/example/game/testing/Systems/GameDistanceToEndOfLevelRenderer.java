package com.example.game.testing.Systems;

import static com.example.game.testing.constants.Constants.LOGIC_WINDOW_SIZE;

import com.example.engine.GraphicsInterface;
import com.example.game.testing.Scenes.mcd_Scene;
import com.example.game.testing.utils.ColorLibrary;
import com.example.utils.Vec2;

public class GameDistanceToEndOfLevelRenderer implements mcd_System{
    float maxDist;
    public GameDistanceToEndOfLevelRenderer(float maxDist){
        this.maxDist = maxDist;
    }
    @Override
    public void update(mcd_Scene scene, float t, float dt) {
        float currentDist = DistanceSystem.getInstance().getDistance();
        cursorPos.x = xMargin+(currentDist/ maxDist)*xCursorDiff;
    }
    float xMargin = 60;
    float xCursorDiff = LOGIC_WINDOW_SIZE.x-2*xMargin-15;
    Vec2 cursorPos = new Vec2(0,20);
    Vec2[] lineStartEnd = {
            //Vertical Left
            new Vec2(xMargin, 10),
            new Vec2(xMargin, 30),
            //Vertical right
            new Vec2(LOGIC_WINDOW_SIZE.x-xMargin, 10),
            new Vec2(LOGIC_WINDOW_SIZE.x-xMargin, 30),
            //Horizontal
            new Vec2(xMargin,20),
            new Vec2(LOGIC_WINDOW_SIZE.x-xMargin,20)
    };
    @Override
    public void render(GraphicsInterface g) {
        g.setColor(ColorLibrary.Blue);
        g.setStrokeWidth(10);
        for(int i = 0; i < lineStartEnd.length; i+=2){
            g.drawLine(lineStartEnd[i], lineStartEnd[i+1]);
        }

        g.save();
        g.rotate(cursorPos.x, cursorPos.y, 0);
        g.setColor(ColorLibrary.Red);
        g.drawNSidePolygon(3, cursorPos, 15, true);
        g.restore();
    }
}
