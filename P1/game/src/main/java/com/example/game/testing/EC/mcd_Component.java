package com.example.game.testing.EC;

import com.example.engine.GraphicsInterface;

public interface mcd_Component {
    public void update(mcd_Entity ent, double dt);
    public void render(mcd_Entity ent, GraphicsInterface g);
}
