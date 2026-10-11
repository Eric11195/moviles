package com.example.game.testing.EC.Component;

import com.example.engine.GraphicsInterface;
import com.example.game.testing.EC.ComponentId;
import com.example.game.testing.EC.mcd_Component;
import com.example.game.testing.EC.mcd_Entity;
import com.example.utils.Vec2;

public class VelocityComponent implements mcd_Component {
    public Vec2 velocity;
    public VelocityComponent(){
        this.velocity = new Vec2(0,0);
    }
    public VelocityComponent(float vx, float vy){
        this.velocity = new Vec2(vx,vy);
    }
    public VelocityComponent(Vec2 velocity){
        this.velocity = velocity.clone();
    }

    @Override
    public void update(mcd_Entity ent, float t, float dt) {
        TransformComponent tr = ((TransformComponent)ent.getComponent(ComponentId.TRANSFORM_COMPONENT));
        tr.pos = tr.pos.add(velocity.mul(dt));
    }

    @Override
    public void render(mcd_Entity ent, GraphicsInterface g) {
    }
}
