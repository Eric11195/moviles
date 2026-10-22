package com.example.game.testing.Systems.levelGenerator;

import static com.example.game.testing.constants.Constants.LASER_HEIGHT;
import static com.example.game.testing.constants.Constants.LASER_WIDTH;
import static com.example.game.testing.constants.Constants.MIN_ZAPPER_HEIGHT;
import static com.example.game.testing.constants.Constants.MISSILE_RADIUS;

import com.example.game.testing.EC.Component.ColliderComponent;
import com.example.game.testing.EC.Component.LaserComponent;
import com.example.game.testing.EC.Component.MissileComponent;
import com.example.game.testing.EC.Component.ObstacleId;
import com.example.game.testing.EC.Component.TransformComponent;
import com.example.game.testing.EC.Component.VelocityComponent;
import com.example.game.testing.EC.Component.WarningSign;
import com.example.game.testing.EC.Component.ZapperComponent;
import com.example.game.testing.EC.ComponentId;
import com.example.game.testing.EC.mcd_Entity;
import com.example.game.testing.Scenes.mcd_Scene;
import com.example.utils.Vec2;

import java.util.ArrayList;

public class HazardGenerator extends Generator{
    public HazardGenerator(mcd_Scene _scene, LevelGeneratorParams _params, DangerMatrix _dangerMatrix) {
        super(_scene, _params,_dangerMatrix);
    }

    @Override
    public boolean setUpNewObstacle(mcd_Entity obstacle, float t, float dt, float speedMult){
        //TransformComponent tr = (TransformComponent) obstacle.getComponent(ComponentId.TRANSFORM_COMPONENT);
        //ImageComponent img = (ImageComponent) obstacle.getComponent(ComponentId.IMAGE_COMPONENT);
        if(!createRandomObstacleComp(obstacle,t,dt,speedMult)) return false;
        return true;
    }

    @Override
    public void addProbabilityToObstacleSpawn(float dt) {
        obstacleAcumSpawn += (dt*Math.random() < params.obstacleExplosiveCreationChance) ? params.obstacleExplosiveCreationBoost : 0;
        float probability = params.obstacleLinearCreationPerSecond*dt;
        obstacleAcumSpawn += probability;
    }

    @Override
    protected void initializeObstacleList(mcd_Scene scene){
        _obstacleList = new ArrayList<mcd_Entity>(params.maxNumberOfSimultaneousObstacles);
        for(int i = 0; i < params.maxNumberOfSimultaneousObstacles; ++i){
            mcd_Entity ent = scene.createEntity();
            ent.addComponent(ComponentId.TRANSFORM_COMPONENT, new TransformComponent());
            ent.addComponent(ComponentId.VELOCITY_COMPONENT, new VelocityComponent());
            ent.addComponent(ComponentId.COLLIDER_COMPONENT, new ColliderComponent());
            ent.addComponent(ComponentId.WARNING_COMPONENT, new WarningSign());
            //ent.addComponent(ComponentId.IMAGE_COMPONENT, new ImageComponent(""));
            ent.setActive(false);
            _obstacleList.add(ent);
        }
        _obstacleArrayIndex = 0;
        _freeIdxCount = params.maxNumberOfSimultaneousObstacles;
    }

    public boolean createRandomObstacleComp(mcd_Entity ent, float t, float dt, float speedMult){
        switch(chooseObstacleType()){
            case OBSTACLE_LASER:
                return createLaser(ent, t, dt, speedMult);
            case OBSTACLE_MISSILE:
                return createMissile(ent, t, dt, speedMult);
            case OBSTACLE_ZAPPER:
                return createZapper(ent,t, dt, speedMult);
            default:
                throw new RuntimeException("Invalid obstacle type");
        }
    }
    public ObstacleId chooseObstacleType(){
        double d= Math.random();
        if(d<0.2) return ObstacleId.OBSTACLE_LASER;
        if(d<0.7) return ObstacleId.OBSTACLE_ZAPPER;
        return ObstacleId.OBSTACLE_MISSILE;
    }
    public boolean createMissile(mcd_Entity ent, float t, float dt, float speedMult){
        Vec2 space =
                getSafeVerticalSpaceForObstacleSoPlayerCanPass(
                        getVerticalSafeZone(),
                        MISSILE_RADIUS*2
                );
        float y = space.x + (space.y-space.x)*(float)Math.random();
        MissileComponent mis = (MissileComponent) ent.setComponent(
                ComponentId.OBSTACLE_COMPONENT,
                new MissileComponent(ent, t, speedMult, y)
        );
        mis.drawSelfInDangerMatrix((TransformComponent) ent.getComponent(ComponentId.TRANSFORM_COMPONENT), dangerMatrix,t,dt);
        return dangerMatrix.ensureTraversability(player,speedMult,dt);
    }
    public  boolean createLaser(mcd_Entity ent, float t, float dt, float speedMult)
    {
        Vec2 space =
                getSafeVerticalSpaceForObstacleSoPlayerCanPass(
                        getVerticalSafeZone(),
                        LASER_HEIGHT*1.5f
                );
        TransformComponent tr =(TransformComponent) ent.getComponent(ComponentId.TRANSFORM_COMPONENT);
        float y = space.x + (space.y-space.x)*(float)Math.random();
        tr.pos.x = 0;
        tr.pos.y = y;
        tr.size.x = LASER_WIDTH;
        tr.size.y = LASER_HEIGHT;

        LaserComponent laser = (LaserComponent) ent.setComponent(ComponentId.OBSTACLE_COMPONENT
                ,new LaserComponent(ent,t,2,speedMult));
        laser.drawSelfInDangerMatrix((TransformComponent) ent.getComponent(ComponentId.TRANSFORM_COMPONENT), dangerMatrix,t,dt);
        if (!dangerMatrix.ensureTraversability(player,speedMult,dt)) {
            ent.rmComponent(ComponentId.DRAW_RECTANGLE_COMPONENT);
            return false;
        }
        return true;
    }
    public boolean createZapper(mcd_Entity ent, float t, float dt, float speedMult){
        Vec2 space =
                getSafeVerticalSpaceForObstacleSoPlayerCanPass(
                        getVerticalSafeZone(),
                        MIN_ZAPPER_HEIGHT
                );
        //Space is really the from and to of y axis that the zapper will ocuppy
        //So x does not mean x axis at all
        Vec2 start=new Vec2(),end=new Vec2();
        float delta = Math.abs(space.y-space.x);
        space.y = space.x + delta * (float)Math.random();
        space.x = space.x + delta * (float)Math.random();
        //System.out.println("Delta: "+ delta+ " Y_RANGE: ("+space.x+","+space.y+')');
        ZapperComponent.getProvisionalZapperData(space,start,end);
        if(!dangerMatrix.checkLine(start,end)) return false;
        dangerMatrix.drawLine(start,end);
        if(!dangerMatrix.ensureTraversability(player,speedMult,dt)) return false;
        ent.setComponent(
                ComponentId.OBSTACLE_COMPONENT,
                new ZapperComponent(ent, t, speedMult, start, end)
        );
        return true;
    }
}
