package com.example.game.testing.EC.Component;

import com.example.engine.GraphicsInterface;
import com.example.game.testing.EC.mcd_Component;
import com.example.game.testing.EC.mcd_Entity;
import com.example.utils.Vec2;

public class TransformComponent implements mcd_Component {
    public Vec2 pos;
    public Vec2 size;
    public TransformComponent(Vec2 pos, Vec2 size){
        this.pos = pos;
        this.size = size;
    }
    public TransformComponent(){
        this.pos = new Vec2(0,0);
        this.size = new Vec2(0,0);
    }

    @Override
    public void update(mcd_Entity ent, float t, float dt) {

    }

    @Override
    public void render(mcd_Entity ent, GraphicsInterface g) {

    }
}
