package com.example.game.testing.EC.Component;

import static com.example.game.testing.constants.Constants.LASER_ANIM_NUM;
import static com.example.game.testing.constants.Constants.LASER_ANIM_TIME;
import static com.example.game.testing.constants.Constants.LASER_ATTACK_DUR;
import static com.example.game.testing.constants.Constants.LASER_HEIGHT;
import static com.example.game.testing.constants.Constants.LASER_WIDTH;
import static com.example.game.testing.constants.Constants.LOGIC_WINDOW_SIZE;
import static com.example.game.testing.constants.Constants.WARNING_TIME;
import static com.example.game.testing.utils.StartPlayerDead.startPlayerDead;
import static com.example.game.testing.constants.Constants.PLAYER_X_POS;

import com.example.engine.ColorEng;
import com.example.game.testing.EC.ComponentId;
import com.example.game.testing.EC.mcd_Entity;
import com.example.engine.GraphicsInterface;
import com.example.game.testing.Scenes.mcd_Scene;
import com.example.game.testing.Systems.levelGenerator.DangerMatrix;
import com.example.game.testing.Systems.levelGenerator.ObstacleFactory;
import com.example.game.testing.utils.ColorLibrary;
import com.example.game.testing.utils.SoundLibrary;
import com.example.utils.Vec2;
import static com.example.game.testing.EC.Component.ObstacleId.OBSTACLE_LASER;
import org.json.JSONObject;

public class LaserComponent extends WarnedObstacle {

    //number of times it has to appear and dissapear for it to "attack".
    float anim_timer;

    boolean is_attacking;

    int index_rep;
    int loop_num;

    ColorEng color;
    mcd_Entity myEnt;


    public static class LaserComponentSerialized extends ObstacleFactory {
        float yPos;
        @Override
        public void instantiate(mcd_Scene scene, mcd_Entity ent, float t) {
            TransformComponent tr = (TransformComponent) ent.getComponent(ComponentId.TRANSFORM_COMPONENT);
            ent.setComponent(ComponentId.OBSTACLE_COMPONENT, new LaserComponent(ent,t,yPos));
        }
    }
    public LaserComponent(mcd_Entity ent,float spawnTime,float posY)
    {
        super(ent, spawnTime);

        if(!ent.hasComponent(ComponentId.DRAW_RECTANGLE_COMPONENT))
        {
            ent.addComponent(ComponentId.DRAW_RECTANGLE_COMPONENT,new DrawRectangleComponent());
        }


        anim_timer =0;
        is_attacking = false;
        index_rep = 0;
        loop_num = 0;
        color = ColorLibrary.Yellow.clone();

        setUpCollider((ColliderComponent) ent.getComponent(ComponentId.COLLIDER_COMPONENT));
        setUpSpeed((VelocityComponent) ent.getComponent(ComponentId.VELOCITY_COMPONENT));
        scrolledTime = calculateScrolledTime(spawnTime);
        myEnt = ent;
        TransformComponent tr = (TransformComponent) ent.getComponent(ComponentId.TRANSFORM_COMPONENT);
        tr.pos.y = posY;
        tr.size.x = LASER_WIDTH;
        tr.size.y = LASER_HEIGHT;
        tr.pos.x =0;
        drawInDangerMatrixPos.y = posY;
        drawInDangerMatrixSize.y = LASER_HEIGHT;
        DrawRectangleComponent draw=  (DrawRectangleComponent)  ent.getComponent(ComponentId.DRAW_RECTANGLE_COMPONENT);
        Vec2 rectSize = tr.size.clone();
        rectSize.x = rectSize.x/7.5f;
        draw.addRectangle(tr.pos,rectSize);
        draw.addRectangle(new Vec2((LOGIC_WINDOW_SIZE.x-rectSize.x),tr.pos.y),rectSize);
        draw.color = color;

        scrolledTime = calculateScrolledTime(spawnTime);
    }
    private void setUpSpeed(VelocityComponent vel){
        vel.velocity.x = 0;
        vel.velocity.y = 0;
    }
    private void setUpCollider(ColliderComponent col){
        col.clearColliders();

    }
    @Override
    public void render(mcd_Entity ent, GraphicsInterface g) {
        if(ent.hasComponent(ComponentId.TRANSFORM_COMPONENT))
        {
            if(!is_attacking)
            {
                if(loop_num%2!=0)
                {
                    float progress = (float) (anim_timer/(LASER_ANIM_TIME));
                    color.a = (int)(255 - 155 * progress);
                    color.a = Math.max(100,Math.min(255,color.a));
                }
                else{
                    float progress = (float) (anim_timer/(LASER_ANIM_TIME));
                    color.a = (int)(100 + 155 * progress);
                    color.a = Math.max(100,Math.min(255,color.a));
                }
            }
            else{color.a = 255;}
            DrawRectangleComponent draw = (DrawRectangleComponent) ent.getComponent(ComponentId.DRAW_RECTANGLE_COMPONENT);
            draw.color = color;
        }
    }
    private void disable(mcd_Entity ent)
    {
        ent.rmComponent(ComponentId.DRAW_RECTANGLE_COMPONENT);
        ColliderComponent colliderComponent= (ColliderComponent) ent.getComponent(ComponentId.COLLIDER_COMPONENT);
        colliderComponent.clearColliders();

        deactivateWarning(ent);

        ent.setActive(false);

    }
    @Override
    public void onPlayerCollision(mcd_Entity player, mcd_Entity ent, mcd_Scene scene) {
        // FATAL
        startPlayerDead(scene.getEngine(), player);
    }

    @Override
    public float calculateScrolledTime(float spawnTime) {
        return (spawnTime+WARNING_TIME+LASER_ATTACK_DUR);
        //return 0;
    }

    Vec2 drawInDangerMatrixPos = new Vec2();
    Vec2 drawInDangerMatrixSize = new Vec2();
    @Override
    public void drawSelfInDangerMatrix(TransformComponent tr, DangerMatrix dm,float t, float dt) {
        super.drawSelfInDangerMatrix(tr,dm,t,dt);
        drawInDangerMatrixPos.x = 0;

        drawInDangerMatrixSize.x = LOGIC_WINDOW_SIZE.x;

        dm.drawSquare(drawInDangerMatrixPos, drawInDangerMatrixSize);
    }

    final static Vec2 colliderRelativeCenter = new Vec2(PLAYER_X_POS, LASER_HEIGHT/2);
    final static Vec2 paintToScreenSize = new Vec2(LOGIC_WINDOW_SIZE.x,LASER_HEIGHT);
    @Override
    public void update(mcd_Entity ent,float t,float dt) {
        if(t >= scrolledTime) {
            disable(ent);
        }
        //here we need to figure out if it is time to attack or not
        //the time would be after
        if(!is_attacking)
        {
            if(anim_timer<LASER_ANIM_TIME)
            {
                anim_timer +=dt;
            }
            else
            {
                anim_timer = 0;
                loop_num++;
                if(loop_num == LASER_ANIM_NUM)
                {
                    is_attacking = true;
                    ColliderComponent colliderComponent= (ColliderComponent) ent.getComponent(ComponentId.COLLIDER_COMPONENT);

                    colliderComponent.addSingleCollider(colliderRelativeCenter,LASER_HEIGHT/2);
                    DrawRectangleComponent draw = (DrawRectangleComponent) ent.getComponent(ComponentId.DRAW_RECTANGLE_COMPONENT);
                    TransformComponent tr = (TransformComponent) ent.getComponent(ComponentId.TRANSFORM_COMPONENT);
                    draw.clearRectangles();
                    draw.addRectangle(tr.pos,paintToScreenSize);
                    draw.color.r =255;
                    draw.color.b =0;
                    draw.color.g =0;
                }

            }
        }
    }

    @Override
    public JSONObject serialize() {
        JSONObject jo = super.serialize();
        TransformComponent tr = (TransformComponent) myEnt.getComponent(ComponentId.TRANSFORM_COMPONENT);
        jo.put("id", OBSTACLE_LASER);
        jo.put("y", Double.valueOf(tr.pos.y));
        return jo;
    }
    public static LaserComponentSerialized deserialize(JSONObject obj) {
        LaserComponentSerialized lcs = new LaserComponentSerialized();
        lcs.yPos = (float)obj.getDouble("y");
        lcs.spawnTime = getSpawnTime(obj);
        return lcs;
    }
}
