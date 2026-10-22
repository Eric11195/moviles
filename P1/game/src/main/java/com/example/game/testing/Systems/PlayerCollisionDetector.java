package com.example.game.testing.Systems;

import static com.example.game.testing.EC.Component.ColliderComponent.checkAllOverlaps;

import com.example.engine.GraphicsInterface;
import com.example.game.testing.EC.Component.Obstacle;
import com.example.game.testing.EC.ComponentId;
import com.example.game.testing.EC.mcd_Entity;
import com.example.game.testing.Scenes.LoseMenuScene;
import com.example.game.testing.Scenes.mcd_Scene;

import javax.swing.text.html.parser.Entity;

public class PlayerCollisionDetector implements mcd_System {
    mcd_Entity player;
    public PlayerCollisionDetector(mcd_Entity player){
        this.player = player;
    }

    @Override
    public void update(mcd_Scene scene, float t, float dt) {
        for(mcd_Entity ent : scene.getEntityList()){
            if(!ent.isActive() || ent == player || !ent.hasComponent(ComponentId.COLLIDER_COMPONENT)) continue;
            if(ent.hasComponent(ComponentId.OBSTACLE_COMPONENT) && checkAllOverlaps(player,ent)){
//                Player dies
//                playerCollided(scene);

                ((Obstacle)ent.getComponent(ComponentId.OBSTACLE_COMPONENT)).onPlayerCollision(player, ent, scene);
                break;
            }
        }
    }

    public void playerCollided(mcd_Scene scene){
        scene.getEngine().setScene(new LoseMenuScene(scene.getEngine()));
    }

    @Override
    public void render(GraphicsInterface g) {

    }
}
