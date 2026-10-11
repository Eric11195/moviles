package com.example.game.testing.EC.Component;

import static com.example.game.testing.constants.Constants.LOGIC_WINDOW_SIZE;
import static com.example.game.testing.constants.Constants.MIN_ZAPPER_WIDTH;
import static com.example.game.testing.constants.Constants.ZAPPER_DELTA_WIDTH;

import com.example.engine.ColorEng;
import com.example.engine.GraphicsInterface;
import com.example.game.testing.EC.ComponentId;
import com.example.game.testing.EC.mcd_Entity;
import com.example.game.testing.Systems.DangerMatrix;
import com.example.utils.Vec2;

/**
 * The electrical lines danger
 */
public class ZapperComponent extends Obstacle{
    public final Vec2 start;
    public final Vec2 end;

    public ZapperComponent(mcd_Entity ent, float spawnTime, float speedMult, Vec2 yStartEnd) {
        super(spawnTime, speedMult);
        float random_zaper_width = MIN_ZAPPER_WIDTH + (float)Math.random()*ZAPPER_DELTA_WIDTH;
        assert(ent.hasComponent(ComponentId.TRANSFORM_COMPONENT));
        TransformComponent tr = (TransformComponent) ent.getComponent(ComponentId.TRANSFORM_COMPONENT);
        tr.pos = new Vec2(LOGIC_WINDOW_SIZE.x, yStartEnd.x);
        this.start = new Vec2(0,0);
        this.end = new Vec2(random_zaper_width,yStartEnd.y-yStartEnd.x);
        assert(this.start.x >= this.end.x);
        scrolledTime = calculateScrolledTime(spawnTime, speedMult);
    }

    @Override
    public float calculateScrolledTime(float spawnTime, float speedMult) {
        return spawnTime+ (width()+LOGIC_WINDOW_SIZE.x)/speedMult;
    }

    @Override
    public void drawSelfInDangerMatrix(TransformComponent tr, DangerMatrix dm) {
        dm.drawLine(getActualStartPos(tr),getActualEndPos(tr));
    }
    public Vec2 getActualStartPos(TransformComponent tr){
        return tr.pos.add(start);
    }
    public Vec2 getActualEndPos(TransformComponent tr){
        return tr.pos.add(end);
    }

    @Override
    public void render(mcd_Entity ent, GraphicsInterface g){
        g.setColor(ColorEng.RED);
        TransformComponent tr = (TransformComponent) ent.getComponent(ComponentId.TRANSFORM_COMPONENT);
        g.drawLine(getActualStartPos(tr),getActualEndPos(tr));
    }

    public float width(){
        return end.x-start.x;
    }
    public float height(){
        return Math.abs(end.y-start.y);
    }

}
