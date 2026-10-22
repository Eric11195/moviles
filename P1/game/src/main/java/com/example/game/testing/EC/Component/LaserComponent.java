package com.example.game.testing.EC.Component;

import static com.example.game.testing.constants.Constants.LASER_ANIM_NUM;
import static com.example.game.testing.constants.Constants.LASER_ANIM_TIME;
import static com.example.game.testing.constants.Constants.LASER_ATTACK_DUR;
import static com.example.game.testing.constants.Constants.LASER_HEIGHT;
import static com.example.game.testing.constants.Constants.LASER_WIDTH;
import static com.example.game.testing.constants.Constants.LOGIC_WINDOW_SIZE;
import static com.example.game.testing.utils.StartPlayerDead.startPlayerDead;
import static com.example.game.testing.constants.Constants.PLAYER_X_POS;

import com.example.engine.ColorEng;
import com.example.game.testing.EC.ComponentId;
import com.example.game.testing.EC.mcd_Entity;
import com.example.engine.GraphicsInterface;
import com.example.game.testing.Scenes.LoseMenuScene;
import com.example.game.testing.Scenes.mcd_Scene;
import com.example.game.testing.Systems.levelGenerator.DangerMatrix;
import com.example.game.testing.Systems.levelGenerator.ObstacleFactory;
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
        public void instantiate(mcd_Scene scene, mcd_Entity ent, float levelSpeed, float t) {
            TransformComponent tr = (TransformComponent) ent.getComponent(ComponentId.TRANSFORM_COMPONENT);
            ent.setComponent(ComponentId.OBSTACLE_COMPONENT, new LaserComponent(ent,t,2,yPos));
        }
    }
    public LaserComponent(mcd_Entity ent,float spawnTime, float speed,float posY)
    {
        super(ent, spawnTime,0);

        if(!ent.hasComponent(ComponentId.DRAW_RECTANGLE_COMPONENT))
        {
            ent.addComponent(ComponentId.DRAW_RECTANGLE_COMPONENT,new DrawRectangleComponent());
        }


        anim_timer =0;
        is_attacking = false;
        index_rep = 0;
        loop_num = 0;
        color = new ColorEng(255,255,0,255);

        setUpCollider((ColliderComponent) ent.getComponent(ComponentId.COLLIDER_COMPONENT));
        setUpSpeed((VelocityComponent) ent.getComponent(ComponentId.VELOCITY_COMPONENT));
        speedMult =speed;
        scrolledTime = calculateScrolledTime(spawnTime, speedMult);
        myEnt = ent;
        TransformComponent tr = (TransformComponent) ent.getComponent(ComponentId.TRANSFORM_COMPONENT);
        tr.pos.y = posY;
        tr.size.x = LASER_WIDTH;
        tr.size.y = LASER_HEIGHT;
        tr.pos.x =0;
        DrawRectangleComponent draw=  (DrawRectangleComponent)  ent.getComponent(ComponentId.DRAW_RECTANGLE_COMPONENT);
        Vec2 rectSize = tr.size;
        rectSize.x = rectSize.x/7.5f;
        draw.addRectangle(tr.pos,rectSize);
        draw.addRectangle(new Vec2((LOGIC_WINDOW_SIZE.x-rectSize.x),tr.pos.y),rectSize);

        draw.color = color;
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
    private  void disable(mcd_Entity ent)
    {
        ent.rmComponent(ComponentId.DRAW_RECTANGLE_COMPONENT);
        ColliderComponent colliderComponent= (ColliderComponent) ent.getComponent(ComponentId.COLLIDER_COMPONENT);
        colliderComponent.clearColliders();

        ent.setActive(false);

    }
    @Override
    public void onPlayerCollision(mcd_Entity player, mcd_Entity ent, mcd_Scene scene) {
        // FATAL
        startPlayerDead(scene.getEngine(), player);
    }

    @Override
    public float calculateScrolledTime(float spawnTime, float speedMult) {
        return (spawnTime+(LASER_ANIM_TIME*LASER_ANIM_NUM)+LASER_ATTACK_DUR);
        //return 0;
    }

    @Override
    public void drawSelfInDangerMatrix(TransformComponent tr, DangerMatrix dm,float t, float dt) {
        //draw a rectangle in a position far away but takinf into account the point in time,
        //the number of loops left and the time it keeps attacking
        float pos_left =  (t-scrolledTime);
        //need to re-write this
        Vec2 size= new Vec2(LASER_ATTACK_DUR,LASER_HEIGHT);
        Vec2 pos = new Vec2(tr.pos.x+pos_left,tr.pos.y);
        dm.drawSquare(pos,size);
    }

    @Override
    public void update(mcd_Entity ent,float t,float dt) {
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

                    //need the player_pos in the X axis for this
                    Vec2 aux = new Vec2(PLAYER_X_POS,LASER_HEIGHT/2);
                    colliderComponent.addSingleCollider(aux,LASER_HEIGHT/2);
                    DrawRectangleComponent draw = (DrawRectangleComponent) ent.getComponent(ComponentId.DRAW_RECTANGLE_COMPONENT);
                    TransformComponent tr = (TransformComponent) ent.getComponent(ComponentId.TRANSFORM_COMPONENT);
                    draw.clearRectangles();
                    draw.addRectangle(tr.pos,new Vec2(LOGIC_WINDOW_SIZE.x,tr.size.y));
                    draw.color.r =255;
                    draw.color.b =0;
                    draw.color.g =0;
                }

            }
        }
        else
        {
            if(ent.hasComponent(ComponentId.COLLIDER_COMPONENT))
            {
                if(t >= scrolledTime)
                {disable(ent);}
                //time to die == t;
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
