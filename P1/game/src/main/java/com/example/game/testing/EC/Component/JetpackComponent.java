package com.example.game.testing.EC.Component;

import static com.example.game.testing.constants.Constants.GRAVITY_FORCE;
import static com.example.game.testing.constants.Constants.LOGIC_WINDOW_SIZE;
import static com.example.game.testing.constants.Constants.MAX_PLAYER_FALL_SPEED;
import static com.example.game.testing.constants.Constants.MAX_PLAYER_UPWARDS_SPEED;
import static com.example.game.testing.constants.Constants.PLAYER_DIMENSIONS;
import static com.example.game.testing.constants.Constants.Y_PLAYER_ACCEL;

import com.example.engine.GraphicsInterface;
import com.example.game.testing.EC.ComponentId;
import com.example.game.testing.EC.mcd_Component;
import com.example.game.testing.EC.mcd_Entity;
import com.example.game.testing.utils.ColorLibrary;
import com.example.utils.Vec2;

public class JetpackComponent implements mcd_Component{
    boolean isFlying;
    float halfsize = PLAYER_DIMENSIONS.x/2;
    Vec2 halfsizes = new Vec2(halfsize,halfsize);
    private void checkSpeed(mcd_Entity playerEnt)
    {
        VelocityComponent velComp = (VelocityComponent) playerEnt.getComponent(ComponentId.VELOCITY_COMPONENT);
        velComp.velocity.y=Math.max(-MAX_PLAYER_UPWARDS_SPEED,Math.min(MAX_PLAYER_FALL_SPEED,velComp.velocity.y));
    }
    private  void respectBorders(mcd_Entity ent)
    {
        TransformComponent tr = (TransformComponent) ent.getComponent(ComponentId.TRANSFORM_COMPONENT);
        VelocityComponent vel = (VelocityComponent) ent.getComponent(ComponentId.VELOCITY_COMPONENT);
        tr.pos.y=Math.max(0,Math.min(LOGIC_WINDOW_SIZE.y-tr.size.y,tr.pos.y));
        if(tr.pos.y<=0){
            vel.velocity.y = Math.max(vel.velocity.y, 0);
        }
        else if(tr.pos.y>=LOGIC_WINDOW_SIZE.y - tr.size.y)
        {
            vel.velocity.y = Math.min(vel.velocity.y,0);
        }
    }

    public void applyThrustAndGravity(mcd_Entity ent, float dt){
        TransformComponent tr = (TransformComponent) ent.getComponent(ComponentId.TRANSFORM_COMPONENT);
        VelocityComponent vel = (VelocityComponent) ent.getComponent(ComponentId.VELOCITY_COMPONENT);
        if (isFlying) {
            vel.velocity.y += -Y_PLAYER_ACCEL * dt;
        }
        vel.velocity.y+= GRAVITY_FORCE*dt;
    }
    @Override
    public void render(mcd_Entity ent, GraphicsInterface g)
    {
        TransformComponent tr = (TransformComponent) ent.getComponent(ComponentId.TRANSFORM_COMPONENT);

        g.setStrokeWidth(3);

        Vec2 pos = new Vec2(tr.pos.add(halfsizes));
        g.setColor(ColorLibrary.Grey);
        g.drawCircle(pos,halfsize,true);

        g.setColor(ColorLibrary.DarkGrey);
        g.drawCircle(pos,halfsize,false);
    }

    @Override
    public void update(mcd_Entity ent, float t , float dt) {
        if(ent.hasComponent(ComponentId.TRANSFORM_COMPONENT)){
            respectBorders(ent);
            applyThrustAndGravity(ent, dt);
            checkSpeed(ent);
        }
    }
    public void setIsFlying(boolean value){
        isFlying = value;
    }
}
