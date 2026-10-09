package com.example.game.testing.EC.Component;

import com.example.engine.GraphicsInterface;
import com.example.game.testing.EC.ComponentId;
import com.example.game.testing.EC.mcd_Component;
import com.example.game.testing.EC.mcd_Entity;

public class JetpackComponent implements mcd_Component{
    float accel;
    float gravity =15.0f;

    float velocity = 0.f;

    float max_falling_speed =30;
    float max_flying_speed = -20;
    boolean is_flying;
    public JetpackComponent(float f)
    {
        accel = f;
    }

    private void checkSpeed()
    {
        velocity=Math.max(max_flying_speed,Math.min(max_falling_speed,velocity));
    }
    private  void RespectBorders(TransformComponent tr)
    {
        tr.pos.y=Math.max(0,Math.min(600-tr.size.y,tr.pos.y));
        if(tr.pos.y==0||tr.pos.y==600)
        {
            velocity=0;
        }
    }
    private void isInputDown(mcd_Entity ent)
    {
        is_flying = ent.getEngine().getInput().pressed();
    }
    @Override
    public void render(mcd_Entity ent, GraphicsInterface g)
    {}

    @Override
    public void update(mcd_Entity ent, float t , float dt) {
        isInputDown(ent);
        if(ent.hasComponent(ComponentId.TRANSFORM_COMPONENT)){
            TransformComponent tr = (TransformComponent) ent.getComponent(ComponentId.TRANSFORM_COMPONENT);
           if (is_flying) {
               velocity += (float) (accel * dt);
           }
           velocity+= (float) (gravity*dt);
            tr.pos.y+=velocity*dt;
            RespectBorders(tr);
            checkSpeed();
        }

    }
}
