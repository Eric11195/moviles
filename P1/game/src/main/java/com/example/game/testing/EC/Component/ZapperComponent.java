package com.example.game.testing.EC.Component;

import static com.example.game.testing.EC.Component.ObstacleId.OBSTACLE_ZAPPER;
import static com.example.game.testing.constants.Constants.LOGIC_WINDOW_SIZE;
import static com.example.game.testing.constants.Constants.MAX_ZAPPER_LENGTH;
import static com.example.game.testing.constants.Constants.MIN_ZAPPER_WIDTH;
import static com.example.game.testing.constants.Constants.ZAPPER_DELTA_WIDTH;
import static com.example.game.testing.constants.Constants.ZAPPER_HEAD_RADIUS;
import static com.example.game.testing.constants.Constants.ZAPPER_LINE_WIDTH;
import static com.example.game.testing.constants.Constants.ZAPPER_MAX_COLLIDERS;
import static com.example.game.testing.utils.JSON_HELPER.fromJSON;
import static com.example.game.testing.utils.StartPlayerDead.startPlayerDead;

import com.example.engine.GraphicsInterface;
import com.example.game.testing.EC.ComponentId;
import com.example.game.testing.EC.mcd_Entity;
import com.example.game.testing.Scenes.mcd_Scene;
import com.example.game.testing.Systems.LevelSpeedSystem;
import com.example.game.testing.Systems.levelGenerator.DangerMatrix;
import com.example.game.testing.Systems.levelGenerator.ObstacleFactory;
import com.example.game.testing.utils.ColorLibrary;
import com.example.game.testing.utils.JSON_HELPER;
import com.example.utils.Vec2;

import org.json.JSONObject;

/**
 * The electrical lines danger
 */
public class ZapperComponent extends Obstacle{
    public final Vec2 start;
    public final Vec2 end;

    /**
     * @param outStart returns here the first point of the zapper
     * @param outEnd returns here the second point of the zapper
     * @param yStartEnd gets here the info of the height allowed Vec2(minY,maxY)
     */
    public static void getProvisionalZapperData(Vec2 yStartEnd, Vec2 outStart, Vec2 outEnd){
        float random_zaper_width = MIN_ZAPPER_WIDTH + (float)Math.random()*ZAPPER_DELTA_WIDTH;
        outStart.x = LOGIC_WINDOW_SIZE.x;
        outStart.y = yStartEnd.x;
        outEnd.x = LOGIC_WINDOW_SIZE.x+random_zaper_width;
        outEnd.y = yStartEnd.y;
        Vec2 delta = outEnd.sub(outStart);
        float excessLength = delta.length() - MAX_ZAPPER_LENGTH;
        if(excessLength > 0){
            delta.normalize();
            Vec2 toSubstract = delta.mul(-excessLength);
            outEnd.set(outEnd.add(toSubstract));
        }
    }
    public static class ZapperComponentSerialized extends ObstacleFactory {
        Vec2 start,end;
        @Override
        public void instantiate(mcd_Scene scene, mcd_Entity ent, float t) {
            ent.setComponent(ComponentId.OBSTACLE_COMPONENT, new ZapperComponent(ent,t, start,end));
        }
    }
    mcd_Entity myEnt;

    public ZapperComponent(mcd_Entity ent, float spawnTime, Vec2 start, Vec2 end) {
        super(ent, spawnTime);
        assert(ent.hasComponent(ComponentId.TRANSFORM_COMPONENT));
        TransformComponent tr = (TransformComponent) ent.getComponent(ComponentId.TRANSFORM_COMPONENT);
        tr.pos = new Vec2(start.x, start.y);
        this.start = new Vec2(0,0);
        this.end = new Vec2(end.x-start.x,end.y-start.y);
        scrolledTime = calculateScrolledTime(spawnTime);
        this.myEnt = ent;
        generateColliders(ent);
    }
    private void generateColliders(mcd_Entity ent){
        ColliderComponent col = (ColliderComponent)ent.getComponent(ComponentId.COLLIDER_COMPONENT);
        col.clearColliders();
        //Create one bigger on the first point
        col.addSingleCollider(start, ZAPPER_HEAD_RADIUS);
        //Other one bigger on the last point
        col.addSingleCollider(end, ZAPPER_HEAD_RADIUS);
        //Create several smaller ones in between them. So that the player cannot cross it
        col.addColliderLine(
            start,
            end,
            ZAPPER_LINE_WIDTH,
            ZAPPER_MAX_COLLIDERS
        );
    }

    @Override
    public float calculateScrolledTime(float spawnTime) {
        return spawnTime+ (width()+LOGIC_WINDOW_SIZE.x)/ LevelSpeedSystem.getInstance().getSpeedMult();
    }

    @Override
    public void drawSelfInDangerMatrix(TransformComponent tr, DangerMatrix dm, float t, float dt) {
        Vec2 firstPt = getActualStartPos(tr);
        Vec2 secondPt = getActualEndPos(tr);
        dm.drawLine(
            firstPt,
            secondPt
        );
        float half_size = ZAPPER_HEAD_RADIUS/2.0f;
        dm.drawSquare(
            firstPt.add(new Vec2(-half_size,-half_size)),
            new Vec2(ZAPPER_HEAD_RADIUS, ZAPPER_HEAD_RADIUS)
        );
        dm.drawSquare(
            secondPt.add(new Vec2(-half_size,-half_size)),
            new Vec2(ZAPPER_HEAD_RADIUS, ZAPPER_HEAD_RADIUS)
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
        g.setStrokeWidth(2);
        TransformComponent tr = (TransformComponent) ent.getComponent(ComponentId.TRANSFORM_COMPONENT);
        ColliderComponent collider = (ColliderComponent) ent.getComponent(ComponentId.COLLIDER_COMPONENT);
        collider.colliders.forEach(col ->{
            g.setColor(ColorLibrary.Yellow);
            g.drawCircle(tr.pos.add(col),col.rad,true);
            g.setColor(ColorLibrary.Gold);
            g.drawCircle(tr.pos.add(col),col.rad,false);
        });
    }

    @Override
    public void onPlayerCollision(mcd_Entity player, mcd_Entity ent, mcd_Scene scene) {
        startPlayerDead(scene.getEngine(), player);
    }

    public float width(){
        return Math.abs(end.x-start.x);
    }
    public float height(){
        return Math.abs(end.y-start.y);
    }

    @Override
    public JSONObject serialize(){
        JSONObject jo = super.serialize();
        TransformComponent tr = (TransformComponent) myEnt.getComponent(ComponentId.TRANSFORM_COMPONENT);
        jo.put("start", JSON_HELPER.toJSON(tr.pos));
        jo.put("end", JSON_HELPER.toJSON(end.add(tr.pos)));
        jo.put("id",OBSTACLE_ZAPPER);
        return jo;
    }
    public static ZapperComponentSerialized deserialize(JSONObject obj){
        ZapperComponentSerialized zcs = new ZapperComponentSerialized();
        zcs.end = fromJSON((JSONObject) obj.getJSONObject("end"));
        zcs.start = fromJSON((JSONObject) obj.getJSONObject("start"));
        zcs.spawnTime = getSpawnTime(obj);
        return zcs;
    }

}
