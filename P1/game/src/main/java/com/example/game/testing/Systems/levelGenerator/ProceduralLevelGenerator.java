package com.example.game.testing.Systems.levelGenerator;

import static com.example.game.testing.constants.Constants.DANGER_MATRIX_PX_TILE_SIZE;
import static com.example.game.testing.constants.Constants.LOGIC_WINDOW_SIZE;
import static com.example.game.testing.constants.Constants.MIN_ZAPPER_HEIGHT;
import static com.example.game.testing.constants.Constants.PLAYER_DIMENSIONS;
import static com.example.game.testing.constants.Constants.PLAYER_MARGINS;

import com.example.game.testing.EC.Component.Obstacle;
import com.example.game.testing.EC.Component.TransformComponent;
import com.example.game.testing.EC.Component.ZapperComponent;
import com.example.game.testing.EC.ComponentId;
import com.example.game.testing.EC.mcd_Entity;
import com.example.game.testing.Scenes.mcd_Scene;
import com.example.game.testing.Systems.ILevelGenerator;
import com.example.utils.Vec2;

public class ProceduralLevelGenerator extends ILevelGenerator {
    mcd_Entity playerEnt;
    //@param density marks the highest amount of obstacles in screen at the same time
    public ProceduralLevelGenerator(mcd_Scene scene, mcd_Entity playerEnt, LevelGeneratorParams params){
        super(scene, params);
        this.playerEnt = playerEnt;
    }


    float obstacleAcumSpawn = 0;
    public void addProbabilityToObstacleSpawn(float dt){
        obstacleAcumSpawn += (dt*Math.random() < params.obstacleExplosiveCreationChance) ? params.obstacleExplosiveCreationBoost : 0;
        float probability = params.obstacleLinearCreationPerSecond*dt;
        obstacleAcumSpawn += probability;
    }


    @Override
    public boolean setUpNewObstacle(mcd_Entity obstacle, float t, float dt){
        //TransformComponent tr = (TransformComponent) obstacle.getComponent(ComponentId.TRANSFORM_COMPONENT);
        //ImageComponent img = (ImageComponent) obstacle.getComponent(ComponentId.IMAGE_COMPONENT);
        if(!createRandomObstacleComp(obstacle,t,dt)) return false;
        obstacle.setActive(true);
        --obstacleAcumSpawn;
        return true;
    }

    DangerMatrix dangerMatrix= new DangerMatrix(DANGER_MATRIX_PX_TILE_SIZE);
    public void setupDangerMatrixWithAlreadyExistingObjects(){
        dangerMatrix.reset();
        for(mcd_Entity ent : _obstacleList){
            if(!ent.isActive()) continue;
            ((Obstacle)ent.getComponent(ComponentId.OBSTACLE_COMPONENT)).drawSelfInDangerMatrix(
                    (TransformComponent) ent.getComponent(ComponentId.TRANSFORM_COMPONENT),
                    dangerMatrix
            );
        }
    }
    /**
     *
     * @return (y1,y2) so that y1<y2 and player will not be harmed in that zone.
     * Consider y1,y2 as points at the rightmost screen position
     */
    public Vec2 getVerticalSafeZone(){
        return dangerMatrix.getFreeHeightRange(LOGIC_WINDOW_SIZE.x);
    }
    public boolean createRandomObstacleComp(mcd_Entity ent, float t, float dt){
        return createZapper(ent,t, dt);

    }
    public boolean createZapper(mcd_Entity ent, float t, float dt){
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
        if(!dangerMatrix.ensureTraversability(playerEnt,this.speedMult,dt)) return false;
        ent.setComponent(
                ComponentId.OBSTACLE_COMPONENT,
                new ZapperComponent(ent, t, this.speedMult, start, end)
        );
        return true;
    }

    /**
     * returns a vec 2 with y coordinates where it would be ok to put an obstacle.
     * This ensures player can pass between obstacles
     * @return Vec2(yUp, yDown)
     */
    public Vec2 getSafeVerticalSpaceForObstacleSoPlayerCanPass(Vec2 freeYRange, float minObstacleHeight){
        float randomRange = (float)Math.random() * (LOGIC_WINDOW_SIZE.y-PLAYER_DIMENSIONS.y- PLAYER_MARGINS.y);
        if(randomRange < minObstacleHeight){
            //choose space under randomRange
            return new Vec2(randomRange, freeYRange.y);
        }else if(LOGIC_WINDOW_SIZE.y-randomRange < minObstacleHeight){
            //choose space above randomRange
            return new Vec2(freeYRange.x, randomRange);
        }else{
            //Choose either
            return (Math.random() > 0.5) ? new Vec2(freeYRange.x, randomRange) : new Vec2(randomRange, freeYRange.y);
        }
    }


    @Override
    protected boolean askIfSpawnNewObstacle(float t, float dt) {
        return obstacleAcumSpawn > 1 && _freeIdxCount > 0;
    }

    @Override
    protected void updateObstacleSpawnConditions(float t, float dt) {
        setupDangerMatrixWithAlreadyExistingObjects();
        addProbabilityToObstacleSpawn(dt);
    }
}
