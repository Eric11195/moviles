package com.example.game.testing.Systems;

import static com.example.game.testing.EC.Component.ColliderComponent.checkAllOverlaps;

import com.example.engine.GraphicsInterface;
import com.example.game.testing.EC.Component.ColliderComponent;
import com.example.game.testing.EC.ComponentId;
import com.example.game.testing.EC.mcd_Entity;
import com.example.game.testing.Scenes.LooseMenuScene;
import com.example.game.testing.Scenes.mcd_Scene;

public class PlayerCollisionDetector implements mcd_System {
    mcd_Entity player;
    public PlayerCollisionDetector(mcd_Entity player){
        this.player = player;
    }

    @Override
    public void update(mcd_Scene scene, float t, float dt) {
//        for(mcd_Entity ent : scene.getEntityList()){
//            if(ent == player || !ent.hasComponent(ComponentId.COLLIDER_COMPONENT)) continue;
//            if(checkAllOverlaps(player,ent)){
//                //Player dies
//                playerCollided(scene);
//                break;
//            }
//        }
    }

    public void playerCollided(mcd_Scene scene){
        scene.getEngine().setScene(new LooseMenuScene());
    }

    @Override
    public void render(GraphicsInterface g) {

    }
}
