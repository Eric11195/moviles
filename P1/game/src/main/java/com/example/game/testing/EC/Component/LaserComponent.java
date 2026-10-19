package com.example.game.testing.EC.Component;

import static com.example.game.testing.constants.Constants.LASER_HEIGHT;
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
import com.example.utils.Vec2;

import org.json.JSONObject;

public class LaserComponent extends WarnedObstacle {

    //number of times it has to appear and dissapear for it to "attack".
    int num_rep;
    //duration of 1 of the numRep animations.
    float anim_dur;
    float anim_timer;

    float attack_dur;

    boolean is_attacking;

    int index_rep;

    ColorEng color;
    public LaserComponent(mcd_Entity ent, int numRepetitions, int anim_duration, float speed)
    {
        super(ent, 0,0);

        if(!ent.hasComponent(ComponentId.DRAW_RECTANGLE_COMPONENT))
        {
            ent.addComponent(ComponentId.DRAW_RECTANGLE_COMPONENT,new DrawRectangleComponent());
        }
        num_rep=numRepetitions;
        anim_dur = anim_duration;
        anim_timer =0;
        attack_dur = 10;
        is_attacking = false;
        index_rep = 0;
        color = new ColorEng(255,0,0,255);

        setUpCollider((ColliderComponent) ent.getComponent(ComponentId.COLLIDER_COMPONENT));
        setUpSpeed((VelocityComponent) ent.getComponent(ComponentId.VELOCITY_COMPONENT));
        speedMult =speed;
        scrolledTime = calculateScrolledTime(spawnTime, speedMult);
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
                if(anim_timer<=anim_dur/2)
                {
                    // progress can only be 1 here at the start, but afterwards goes back to 0
                    //here we need to change the color
                    //fist half = dur_total-time_pased = anim_timer-anim_dur/2/anim_dur
                    float progress = (float) (anim_timer/(anim_dur/2));
                    color.a = (int)(255 - 155 * progress);
                    color.a = Math.max(100,Math.min(255,color.a));
                }
                else
                {
                    float progress =(float)( (anim_timer-(anim_dur/2))/(anim_dur/2));
                    color.a = (int) (100 + 155 * progress);
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
    public void onPlayerCollision(mcd_Entity ent, mcd_Scene scene) {
        // FATAL
        startPlayerDead(scene.getEngine(), ent);
    }

    @Override
    public float calculateScrolledTime(float spawnTime, float speedMult) {

        return (spawnTime + (anim_dur*num_rep)*speedMult);
        //return 0;
    }

    @Override
    public void drawSelfInDangerMatrix(TransformComponent tr, DangerMatrix dm,float t, float dt) {
        //draw a rectangle in a position far away but takinf into account the point in time,
        //the number of loops left and the time it keeps attacking
        float pos_left =  (t-scrolledTime);
        float sizeX = attack_dur;
        Vec2 size= new Vec2(sizeX,LASER_HEIGHT);
        Vec2 pos = new Vec2(tr.pos.x+pos_left,tr.pos.y);
        dm.drawSquare(pos,size);
    }

    @Override
    public void update(mcd_Entity ent,float t,float dt) {
        //here we need to figure out if it is time to attack or not
        //the time would be after
        if(!is_attacking)
        {
            if(anim_timer<anim_dur)
            {
                anim_timer +=dt;
            }
            else
            {
                anim_timer = 0;
                num_rep--;
                if(num_rep == 0)
                {
                    is_attacking = true;
                    ColliderComponent colliderComponent= (ColliderComponent) ent.getComponent(ComponentId.COLLIDER_COMPONENT);

                    //need the player_pos in the X axis for this
                    Vec2 aux = new Vec2(PLAYER_X_POS,LASER_HEIGHT/2);
                    colliderComponent.addSingleCollider(aux,LASER_HEIGHT/2);
                }

            }
        }
        else
        {
            if(ent.hasComponent(ComponentId.COLLIDER_COMPONENT))
            {

                if(attack_dur>0)
                {
                    attack_dur-=dt;
                }
                else
                {
                    disable(ent);
                }
            }
        }
    }

    @Override
    public JSONObject serialize() {
        return null;
    }
    public void deserialize(JSONObject obj) {
    }
}
