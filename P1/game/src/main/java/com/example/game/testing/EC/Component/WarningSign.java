package com.example.game.testing.EC.Component;

import static com.example.game.testing.constants.Constants.LOGIC_WINDOW_SIZE;
import static com.example.game.testing.constants.Constants.WARNING_RADIUS;

import com.example.engine.GraphicsInterface;
import com.example.game.testing.EC.ComponentId;
import com.example.game.testing.EC.mcd_Component;
import com.example.game.testing.EC.mcd_Entity;
import com.example.game.testing.utils.ColorLibrary;
import com.example.utils.Vec2;

public class WarningSign implements mcd_Component {
    boolean active = false;
    public void activateWarning(){
        active = true;
    }
    public void deactivateWarning(){
        active = false;
    }
    @Override
    public void update(mcd_Entity ent, float t, float dt) {}

    @Override
    public void render(mcd_Entity ent, GraphicsInterface g) {
        if(active){
            TransformComponent tr = (TransformComponent) ent.getComponent(ComponentId.TRANSFORM_COMPONENT);
            final Vec2 middlePoint = new Vec2(
                    LOGIC_WINDOW_SIZE.x-WARNING_RADIUS,
                    tr.pos.y + WARNING_RADIUS
            );
            drawTriangles(g, middlePoint);
            drawExclamation(g,middlePoint);
        }
    }
    private void drawTriangles(GraphicsInterface g, Vec2 middlePoint){
        g.save();
        g.rotate(middlePoint.x, middlePoint.y, -90);
        g.setColor(ColorLibrary.Red);
        g.drawNSidePolygon(3, middlePoint, WARNING_RADIUS, true);
        g.setColor(ColorLibrary.White);
        g.drawNSidePolygon(3, middlePoint, WARNING_RADIUS*0.72f, true);
        g.restore();
    }
    private void drawExclamation(GraphicsInterface g, Vec2 middlePoint){
        g.setColor(ColorLibrary.Black);
        middlePoint.x -= WARNING_RADIUS*0.1f;
        middlePoint.y -= WARNING_RADIUS*0.5f;
        Vec2 size = new Vec2(WARNING_RADIUS*0.2f,WARNING_RADIUS*0.6f);
        g.drawRectangle(middlePoint, size, true);
        middlePoint.y += WARNING_RADIUS*0.7f;
        size.y = size.x;
        g.drawRectangle(middlePoint,size,true);
    }
}
