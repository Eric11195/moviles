package com.example.game.testing.Systems;

import static com.example.game.testing.constants.Constants.LOGIC_WINDOW_SIZE;
import static com.example.game.testing.constants.Constants.PLAYER_X_POS;

import com.example.engine.GraphicsInterface;
import com.example.game.testing.EC.Component.ImageComponent;
import com.example.game.testing.EC.Component.Obstacle;
import com.example.game.testing.EC.Component.TransformComponent;
import com.example.game.testing.EC.Component.ZapperComponent;
import com.example.game.testing.EC.ComponentId;
import com.example.game.testing.EC.mcd_Entity;
import com.example.game.testing.Scenes.mcd_Scene;
import com.example.utils.Vec2;

import java.awt.Component;
import java.awt.Image;
import java.util.ArrayList;

public class SceneGenerator implements mcd_System{
    int _circularArrayIndex;
    int _freeIdxCount;
    float speedMult = 5.0f;
    ArrayList<mcd_Entity> _obstacleList;
    //@param density marks the highest amount of obstacles in screen at the same time
    public SceneGenerator(mcd_Scene scene, int obstacleScreenDensity){
        initializeObstacleList(scene,obstacleScreenDensity);
    }

    @Override
    public void update(mcd_Scene scene, float t, float dt){
        int i = _circularArrayIndex;
        int lastIterableElem = (i+_obstacleList.size()-1)% _obstacleList.size();
        if(i >= _obstacleList.size()) throw new RuntimeException("This should never happen");
        updateActiveObstacleCount();
        addProbabilityToObstacleSpawn(dt);
        while(!_obstacleList.get(i).isActive()){
            mcd_Entity ent = _obstacleList.get(i);
            assert(ent!=null);
            //if we create a new obstacle we try to repeat the process for the next free
            //Otherwise, we dont want to create new obstacles this frame
            if(!decideOnPossibleNewObstacle(ent, t, dt)) return;
            if(i==lastIterableElem) return;
            i = (i+1) % _obstacleList.size();
        }
    }
    float obstacleAcumSpawn = 0;
    final float AVERAGE_OBSTACLES_PER_SECOND = 1;
    public void addProbabilityToObstacleSpawn(float dt){
        float probability = AVERAGE_OBSTACLES_PER_SECOND*(float)Math.random()*dt*((float) _freeIdxCount /_obstacleList.size());
        obstacleAcumSpawn += probability;
        //System.out.println("Obstacle Acum: "+obstacleAcumSpawn);
    }
    public void updateActiveObstacleCount(){
        _freeIdxCount = 0;
        for(mcd_Entity ent : _obstacleList){
            _freeIdxCount += ent.isActive() ? 0 : 1;
        }
    }
    @Override
    public void render(GraphicsInterface g){}

    /**
     * Decides whether to create a new obstacle or not, and which type and position in the first case
     * @param ent entity
     * @param t time
     * @param dt delta time
     * @return true if new obstacle was created, false otherwise
     */
    private boolean decideOnPossibleNewObstacle(mcd_Entity ent, float t, float dt){
        int currentIdx = _circularArrayIndex;
        _circularArrayIndex = (_circularArrayIndex +1) % _obstacleList.size();
        if(askIfSpawnNewObstacle(dt)){
            System.out.println("Creame un obstaculo pibe");
            setUpNewObstacle(_obstacleList.get(currentIdx), t, dt);
            return true;
        }
        return false;
    }
    public void setUpNewObstacle(mcd_Entity obstacle, float t, float dt){
        TransformComponent tr = (TransformComponent) obstacle.getComponent(ComponentId.TRANSFORM_COMPONENT);
        //ImageComponent img = (ImageComponent) obstacle.getComponent(ComponentId.IMAGE_COMPONENT);
        Obstacle obs = (Obstacle) obstacle.setComponent(ComponentId.OBSTACLE_COMPONENT, getRandomObstacleComp(obstacle, t,dt));
        obstacle.setActive(true);
    }

    DangerMatrix dangerMatrix= new DangerMatrix(3);
    /**
     *
     * @return (y1,y2) so that y1<y2 and player will not be harmed in that zone.
     * Consider y1,y2 as points at the rightmost screen position
     */
    public Vec2 getVerticalSafeZone(){
        dangerMatrix.reset();
        for(mcd_Entity ent : _obstacleList){
            if(!ent.isActive()) continue;
            ((Obstacle)ent.getComponent(ComponentId.OBSTACLE_COMPONENT)).drawSelfInDangerMatrix(
                    (TransformComponent) ent.getComponent(ComponentId.TRANSFORM_COMPONENT),
                    dangerMatrix
            );
        }
        return dangerMatrix.getFreeHeightRange(LOGIC_WINDOW_SIZE.x);
    }
    public Obstacle getRandomObstacleComp(mcd_Entity ent, float t, float dt){
        Vec2 freeYRange =getVerticalSafeZone();
        return new ZapperComponent(ent, t, speedMult, freeYRange);
    }
    public boolean askIfSpawnNewObstacle(float dt){
        if(obstacleAcumSpawn > 1 && _freeIdxCount > 0){
            --obstacleAcumSpawn;
            return true;
        }else return false;
    }
    private void initializeObstacleList(mcd_Scene scene, int obstacleScreenDensity){
        _obstacleList = new ArrayList<mcd_Entity>(obstacleScreenDensity);
        for(int i = 0; i < obstacleScreenDensity; ++i){
            mcd_Entity ent = scene.createEntity();
            ent.addComponent(ComponentId.TRANSFORM_COMPONENT, new TransformComponent());
            //ent.addComponent(ComponentId.IMAGE_COMPONENT, new ImageComponent(""));
            ent.setActive(false);
            _obstacleList.add(ent);
        }
        _circularArrayIndex =0;
        _freeIdxCount = obstacleScreenDensity;
    }
}
