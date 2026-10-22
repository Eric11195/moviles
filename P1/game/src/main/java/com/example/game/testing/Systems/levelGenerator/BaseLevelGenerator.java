package com.example.game.testing.Systems.levelGenerator;

import static com.example.game.testing.utils.FontLibrary.FontName.STANDARD;

import com.example.engine.GraphicsInterface;
import com.example.game.testing.EC.Component.ColliderComponent;
import com.example.game.testing.EC.Component.TransformComponent;
import com.example.game.testing.EC.Component.VelocityComponent;
import com.example.game.testing.EC.Component.WarningSign;
import com.example.game.testing.EC.ComponentId;
import com.example.game.testing.EC.mcd_Entity;
import com.example.game.testing.Scenes.mcd_Scene;
import com.example.game.testing.Systems.mcd_System;
import com.example.game.testing.constants.Constants;
import com.example.game.testing.utils.FontLibrary;
import com.example.utils.Vec2;

import java.util.ArrayList;

public abstract class BaseLevelGenerator implements mcd_System {
    protected LevelGeneratorParams params;
    protected float speedMult;
    public ArrayList<Generator> generators = new ArrayList<>();

    mcd_Entity player = null;
    float distanceAdvanced = 0;
    public void setPlayer(mcd_Entity player){
        this.player = player;
        generators.forEach(gnr -> gnr.setPlayer(player));
    }
    protected BaseLevelGenerator(mcd_Scene scene, LevelGeneratorParams params){
        this.params = params;
        this.speedMult = this.params.levelInitialSpeed;
    }

    @Override
    public void update(mcd_Scene scene, float t, float dt){
        generators.forEach(gnr->gnr.update(t,dt,speedMult));
        float toAdd = dt*speedMult;
        if(((int)distanceAdvanced) / params.augmentSpeedEachXPixels != ((int)(distanceAdvanced+toAdd))/ params.augmentSpeedEachXPixels){
            speedMult += params.augmentSpeedByYEachXPixels;
            toAdd = dt*speedMult;
        }
        distanceAdvanced += toAdd;
    }
    Vec2 distanceTextPos = new Vec2(0,0);
    @Override
    public void render(GraphicsInterface g){
        g.setFont(FontLibrary.getFont(STANDARD));
        g.drawText(Integer.toString((int)this.distanceAdvanced), distanceTextPos);
    }
}
