package com.example.game.testing.Systems.levelGenerator;

import static com.example.game.testing.constants.Constants.LASER_HEIGHT;
import static com.example.game.testing.constants.Constants.LASER_WIDTH;
import static com.example.game.testing.constants.Constants.LOGIC_WINDOW_SIZE;
import static com.example.game.testing.constants.Constants.MIN_ZAPPER_HEIGHT;
import static com.example.game.testing.constants.Constants.MISSILE_RADIUS;

import com.example.game.testing.EC.Component.ColliderComponent;
import com.example.game.testing.EC.Component.LaserComponent;
import com.example.game.testing.EC.Component.MissileComponent;
import com.example.game.testing.EC.Component.ObstacleId;
import com.example.game.testing.EC.Component.TransformComponent;
import com.example.game.testing.EC.Component.VelocityComponent;
import com.example.game.testing.EC.Component.WarnedObstacle;
import com.example.game.testing.EC.Component.WarningSign;
import com.example.game.testing.EC.Component.ZapperComponent;
import com.example.game.testing.EC.ComponentId;
import com.example.game.testing.EC.mcd_Entity;
import com.example.game.testing.Scenes.mcd_Scene;
import com.example.game.testing.Systems.LevelSpeedSystem;
import com.example.utils.Vec2;

import java.util.ArrayList;

public class HazardGenerator extends Generator{
    public HazardGenerator(mcd_Scene _scene, LevelGeneratorParams _params, DangerMatrix _dangerMatrix) {
        super(_scene, _params,_dangerMatrix);
    }

    @Override
    public boolean setUpNewObstacle(mcd_Entity obstacle, float t, float dt){
        //TransformComponent tr = (TransformComponent) obstacle.getComponent(ComponentId.TRANSFORM_COMPONENT);
        //ImageComponent img = (ImageComponent) obstacle.getComponent(ComponentId.IMAGE_COMPONENT);
        return createRandomObstacleComp(obstacle,t,dt);
    }

    @Override
    public void addProbabilityToObstacleSpawn(float dt) {
        obstacleAcumSpawn += (Math.random() < params.obstacleExplosiveCreationChance * dt) ? params.obstacleExplosiveCreationBoost : 0;
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

    public boolean createRandomObstacleComp(mcd_Entity ent, float t, float dt){
        ObstacleId chosen = chooseObstacleType();
        //System.out.println("Chosen obstacle is "+chosen.name());
        switch(chosen){
            case OBSTACLE_LASER:
                return createLaser(ent, t, dt);
            case OBSTACLE_MISSILE:
                return createMissile(ent, t, dt);
            case OBSTACLE_ZAPPER:
                return createZapper(ent,t, dt);
            default:
                throw new RuntimeException("Invalid obstacle type");
        }
    }
    public ObstacleId chooseObstacleType(){
        double d = Math.random();
        if(d < 0.33) return ObstacleId.OBSTACLE_LASER;
        if(d < 0.66) return ObstacleId.OBSTACLE_MISSILE;
        return ObstacleId.OBSTACLE_ZAPPER;
    }
    public boolean createMissile(mcd_Entity ent, float t, float dt){
        Vec2 space =
                getSafeVerticalSpaceForObstacleSoPlayerCanPass(
                        getVerticalSafeZone(),
                        MISSILE_RADIUS*2
                );
        if(space.y-space.x < MISSILE_RADIUS*2) return false;
        float y = space.x + (space.y-space.x)*(float)Math.random();
        WarnedObstacle.drawSelfInDangerMatrixPos.y = y;
        WarnedObstacle.drawSelfInDangerMatrixPos.x = LOGIC_WINDOW_SIZE.x;
        if(!dangerMatrix.checkRect(WarnedObstacle.drawSelfInDangerMatrixPos, WarnedObstacle.drawSelfInDangerMatrixSize))
            return false;

        if(y < 0 || y+MISSILE_RADIUS*2 > LOGIC_WINDOW_SIZE.y) return false;

        MissileComponent mis = (MissileComponent) ent.setComponent(
                ComponentId.OBSTACLE_COMPONENT,
                new MissileComponent(ent, t, y)
        );
        mis.drawSelfInDangerMatrix((TransformComponent) ent.getComponent(ComponentId.TRANSFORM_COMPONENT), dangerMatrix,t,dt);
        if(!dangerMatrix.ensureTraversability(player,dt)){
            mis.deactivateWarning(ent);
            return false;
        }
        return true;
    }
    final static Vec2 laserSizeInDangerMatrix = new Vec2(0, LASER_HEIGHT);
    final static Vec2 laserPosInDangerMatrix = new Vec2(0, 0);
    public boolean createLaser(mcd_Entity ent, float t, float dt)
    {
        Vec2 space =
                getSafeVerticalSpaceForObstacleSoPlayerCanPass(
                        getVerticalSafeZone(),
                        LASER_HEIGHT
                );
        if(space.y - space.x < LASER_HEIGHT) return false;

        TransformComponent tr =(TransformComponent) ent.getComponent(ComponentId.TRANSFORM_COMPONENT);
        float y = space.x + (space.y-space.x)*(float)Math.random();
        WarnedObstacle.drawSelfInDangerMatrixPos.y = y;
        WarnedObstacle.drawSelfInDangerMatrixPos.x = LOGIC_WINDOW_SIZE.x;
        if(!dangerMatrix.checkRect(WarnedObstacle.drawSelfInDangerMatrixPos, WarnedObstacle.drawSelfInDangerMatrixSize))
            return false;

        LaserComponent laser = (LaserComponent) ent.setComponent(ComponentId.OBSTACLE_COMPONENT
                ,new LaserComponent(ent,t,y));

        laserSizeInDangerMatrix.x = LevelSpeedSystem.getInstance().getSpeedMult();
        laserPosInDangerMatrix.y = y;
        laserPosInDangerMatrix.x = LOGIC_WINDOW_SIZE.x - laserSizeInDangerMatrix.x;

        if (!dangerMatrix.ensureTraversability(player,dt) || !dangerMatrix.checkRect(laserPosInDangerMatrix, laserSizeInDangerMatrix)) {
            ent.rmComponent(ComponentId.DRAW_RECTANGLE_COMPONENT);
            laser.deactivateWarning(ent);
            return false;
        }

        laser.drawSelfInDangerMatrix((TransformComponent) ent.getComponent(ComponentId.TRANSFORM_COMPONENT), dangerMatrix,t,dt);
        return true;
    }
    Vec2 zapperCreationStart = new Vec2(), zapperCreationEnd=new Vec2();
    public boolean createZapper(mcd_Entity ent, float t, float dt){
        Vec2 space =
                getSafeVerticalSpaceForObstacleSoPlayerCanPass(
                        getVerticalSafeZone(),
                        MIN_ZAPPER_HEIGHT
                );
        //Space is really the from and to of y axis that the zapper will ocuppy
        //So x does not mean x axis at all
        float delta = Math.abs(space.y-space.x);
        space.y = space.x + delta * (float)Math.random();
        space.x = space.x + delta * (float)Math.random();
        //System.out.println("Delta: "+ delta+ " Y_RANGE: ("+space.x+","+space.y+')');
        ZapperComponent.getProvisionalZapperData(space,zapperCreationStart,zapperCreationEnd);
        if(zapperCreationStart.y < 0 || zapperCreationStart.y > LOGIC_WINDOW_SIZE.y || zapperCreationEnd.y < 0 || zapperCreationEnd.y > LOGIC_WINDOW_SIZE.y) return false;
        if(!dangerMatrix.checkLine(zapperCreationStart,zapperCreationEnd)) return false;
        dangerMatrix.drawLine(zapperCreationStart,zapperCreationEnd);
        if(!dangerMatrix.ensureTraversability(player,dt)) return false;
        ent.setComponent(
                ComponentId.OBSTACLE_COMPONENT,
                new ZapperComponent(ent, t, zapperCreationStart, zapperCreationEnd)
        );
        return true;
    }
}
