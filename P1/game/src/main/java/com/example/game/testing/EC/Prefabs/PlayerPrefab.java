package com.example.game.testing.EC.Prefabs;

import static com.example.game.testing.constants.Constants.PLAYER_DIMENSIONS;
import static com.example.game.testing.constants.Constants.PLAYER_STARTING_Y_POS;
import static com.example.game.testing.constants.Constants.PLAYER_X_POS;

import com.example.game.testing.EC.Component.ColliderComponent;
import com.example.game.testing.EC.Component.ImageComponent;
import com.example.game.testing.EC.Component.JetpackComponent;
import com.example.game.testing.EC.Component.PlayerInputComponent;
import com.example.game.testing.EC.Component.TransformComponent;
import com.example.game.testing.EC.Component.VelocityComponent;
import com.example.game.testing.EC.ComponentId;
import com.example.game.testing.EC.mcd_Entity;
import com.example.game.testing.Scenes.mcd_Scene;
import com.example.utils.Vec2;
public class PlayerPrefab {

    public static mcd_Entity createPlayer(mcd_Scene my_scene)
    {
        mcd_Entity ent = my_scene.createEntity();
        ent.addComponent(ComponentId.JETPACK_COMPONENT, new JetpackComponent());
        ent.addComponent(ComponentId.TRANSFORM_COMPONENT, new TransformComponent(new Vec2(PLAYER_X_POS,PLAYER_STARTING_Y_POS), PLAYER_DIMENSIONS));
        ent.addComponent(ComponentId.IMAGE_COMPONENT, new ImageComponent("test_image.jpg"));
        ent.addComponent(ComponentId.VELOCITY_COMPONENT, new VelocityComponent());
        ent.addComponent(ComponentId.PLAYER_INPUT_COMPONENT, new PlayerInputComponent());
        float playerRad = Math.min(PLAYER_DIMENSIONS.x,PLAYER_DIMENSIONS.y)/2;
        Vec2 playerCenter = new Vec2(
          PLAYER_DIMENSIONS.x/2,
          PLAYER_DIMENSIONS.y/2
        );
        ent.addComponent(ComponentId.COLLIDER_COMPONENT, new ColliderComponent(playerRad,playerCenter));
        return ent;
    }
}
