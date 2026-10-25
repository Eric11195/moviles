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
import com.example.game.testing.Systems.DistanceSystem;
import com.example.game.testing.Systems.LevelSpeedSystem;
import com.example.game.testing.Systems.mcd_System;
import com.example.game.testing.constants.Constants;
import com.example.game.testing.utils.FontLibrary;
import com.example.utils.Vec2;

import java.util.ArrayList;

public abstract class BaseLevelGenerator implements mcd_System {
    protected LevelGeneratorParams params;
    public ArrayList<Generator> generators = new ArrayList<>();

    protected void addGenerator(Generator gen){
        generators.add(gen);
    }
    mcd_Entity player = null;
    public void setPlayer(mcd_Entity player){
        this.player = player;
        generators.forEach(gnr -> gnr.setPlayer(player));
    }
    protected BaseLevelGenerator(mcd_Scene scene, LevelGeneratorParams params){
        this.params = params;
        com.example.game.testing.Systems.LevelSpeedSystem lss = LevelSpeedSystem.getInstance();
        lss.reset();
        lss.addValue(params.levelInitialSpeed);
        DistanceSystem.getInstance().reset();
    }

    @Override
    public void update(mcd_Scene scene, float t, float dt){
        com.example.game.testing.Systems.LevelSpeedSystem lss = LevelSpeedSystem.getInstance();
        float speedMult = lss.getSpeedMult();
        generators.forEach(gnr->gnr.update(t,dt));
        float toAdd = dt*speedMult;
        DistanceSystem ds = DistanceSystem.getInstance();
        float distance = ds.getDistance();
        if(((int)distance / params.augmentSpeedEachXPixels != ((int)(distance+toAdd))/ params.augmentSpeedEachXPixels)){
            speedMult += params.augmentSpeedByYEachXPixels;
            toAdd = dt*speedMult;
        }
        ds.addValue(toAdd);
    }
    Vec2 distanceTextPos = new Vec2(0,0);
    @Override
    public void render(GraphicsInterface g){
//        g.setFont(FontLibrary.getFont(STANDARD));
//        g.drawText(Integer.toString((int)this.distanceAdvanced), distanceTextPos);
    }
}
