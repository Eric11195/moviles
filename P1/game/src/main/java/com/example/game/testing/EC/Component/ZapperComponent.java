package com.example.game.testing.EC.Component;

import static com.example.game.testing.constants.Constants.LOGIC_WINDOW_SIZE;
import static com.example.game.testing.constants.Constants.MIN_ZAPPER_WIDTH;
import static com.example.game.testing.constants.Constants.ZAPPER_DELTA_WIDTH;
import static com.example.game.testing.constants.Constants.ZAPPER_HEAD_RADIUS;
import static com.example.game.testing.constants.Constants.ZAPPER_LINE_WIDTH;
import static com.example.game.testing.constants.Constants.ZAPPER_MAX_COLLIDERS;

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

    /**
     * @param outStart returns here the first point of the zapper
     * @param outEnd returns here the second point of the zapper
     * @param yStartEnd gets here the info of the height allowed Vec2(miny,maxy)
     */
    public static void getProvisionalZapperData(Vec2 yStartEnd, Vec2 outStart, Vec2 outEnd){
        float random_zaper_width = MIN_ZAPPER_WIDTH + (float)Math.random()*ZAPPER_DELTA_WIDTH;
        outStart.x = LOGIC_WINDOW_SIZE.x;
        outStart.y = yStartEnd.x;
        outEnd.x = LOGIC_WINDOW_SIZE.x+random_zaper_width;
        outEnd.y = yStartEnd.y;
    }

    public ZapperComponent(mcd_Entity ent, float spawnTime, float speedMult, Vec2 start, Vec2 end) {
        super(spawnTime, speedMult);
        assert(ent.hasComponent(ComponentId.TRANSFORM_COMPONENT));
        TransformComponent tr = (TransformComponent) ent.getComponent(ComponentId.TRANSFORM_COMPONENT);
        tr.pos = new Vec2(start.x, start.y);
        this.start = new Vec2(0,0);
        this.end = new Vec2(end.x-start.x,end.y-start.y);
        scrolledTime = calculateScrolledTime(spawnTime, speedMult);
        generateColliders(ent);
    }
    private void generateColliders(mcd_Entity ent){
        if(!ent.hasComponent(ComponentId.COLLIDER_COMPONENT)){
            ent.addComponent(ComponentId.COLLIDER_COMPONENT, new ColliderComponent());
        }
        ColliderComponent col = (ColliderComponent)ent.getComponent(ComponentId.COLLIDER_COMPONENT);
        col.clearColliders();
        //Create one bigger on the first point
        col.addSingleCollider(start, ZAPPER_HEAD_RADIUS);
        //Other one bigger on the last point
        col.addSingleCollider(end, ZAPPER_HEAD_RADIUS);
        //Create several smaller ones in between them. So that the player cannot cross it
        Vec2 delta = end.sub(start);
        delta.normalize();
        delta.mul(ZAPPER_HEAD_RADIUS+ZAPPER_LINE_WIDTH/2);
        col.addColliderLine(
            start.add(delta),
            end.add(delta.mul(-1)),
            ZAPPER_LINE_WIDTH,
            ZAPPER_MAX_COLLIDERS
        );
    }

    @Override
    public float calculateScrolledTime(float spawnTime, float speedMult) {
        return spawnTime+ (width()+LOGIC_WINDOW_SIZE.x)/speedMult;
    }

    @Override
    public void drawSelfInDangerMatrix(TransformComponent tr, DangerMatrix dm) {
        Vec2 firstPt = getActualStartPos(tr);
        Vec2 secondPt = getActualEndPos(tr);
        //System.out.println("Line From ("+firstPt.x+","+firstPt.y+") -> ("+secondPt.x+','+secondPt.y+')');
        dm.drawLine(
                getActualStartPos(tr),
                getActualEndPos(tr)
        );
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
