package com.example.game.testing.EC.Component;

import com.example.engine.GraphicsInterface;
import com.example.game.testing.EC.ComponentId;
import com.example.game.testing.EC.mcd_Component;
import com.example.game.testing.EC.mcd_Entity;

public class PlayerInputComponent implements mcd_Component {
    @Override
    public void update(mcd_Entity ent, float t, float dt) {
        JetpackComponent jet = (JetpackComponent) ent.getComponent(ComponentId.JETPACK_COMPONENT);
        jet.setIsFlying(ent.getEngine().getInput().pressed());
    }

    @Override
    public void render(mcd_Entity ent, GraphicsInterface g) {

    }
}
