package com.example.game.testing.EC.Component;

import com.example.game.testing.EC.ComponentId;
import com.example.game.testing.EC.mcd_Entity;
import com.example.game.testing.Systems.levelGenerator.DangerMatrix;
import com.example.game.testing.constants.Constants;
import com.example.utils.Vec2;

public class RotatingZapperComponent extends ZapperComponent{
    Vec2 center = null;
    float rad = -1.0f;

    float turn_rate = (float) Math.random()*(Constants.MAX_ZAPPER_TURN_RATE - Constants.MIN_ZAPPER_TURN_RATE) + Constants.MIN_ZAPPER_TURN_RATE;
    int direction = Math.random() < 0.5 ? -1 : 1;

    public RotatingZapperComponent(mcd_Entity ent, float spawnTime, Vec2 start, Vec2 end) {
        super(ent, spawnTime, start, end);
    }

    @Override
    public void update(mcd_Entity ent, float t, float dt){
        ColliderComponent col = (ColliderComponent) ent.getComponent(ComponentId.COLLIDER_COMPONENT);
        col.rotate(turn_rate * direction *dt );
        if (center == null) center = col.center;
        if (rad <= 0) rad = center.length();
        super.update(ent,t,dt);
    }
    @Override
    public void drawSelfInDangerMatrix(TransformComponent tr, DangerMatrix dm, float t, float dt) {
        dm.drawSquare(new Vec2(center.x-rad,center.y-rad),
                new Vec2(rad*2,rad*2));
    }
}
