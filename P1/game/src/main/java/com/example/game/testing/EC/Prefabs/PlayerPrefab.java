package com.example.game.testing.EC.Prefabs;

import com.example.game.testing.EC.Component.ButtonComponent;
import com.example.game.testing.EC.Component.ImageComponent;
import com.example.game.testing.EC.Component.JetpackComponent;
import com.example.game.testing.EC.Component.TransformComponent;
import com.example.game.testing.EC.ComponentId;
import com.example.game.testing.EC.mcd_Entity;
import com.example.game.testing.Scenes.mcd_Scene;
import com.example.utils.Vec2;
public class PlayerPrefab {

    public static mcd_Entity createPlayer(mcd_Scene my_scene, Vec2 pos)
    {
        mcd_Entity ent = my_scene.createEntity();
        ent.addComponent(ComponentId.JETPACK_COMPONENT, new JetpackComponent(-35.0f));
        ent.addComponent(ComponentId.TRANSFORM_COMPONENT, new TransformComponent(pos, new Vec2(200,40)));
        ent.addComponent(ComponentId.IMAGE_COMPONENT, new ImageComponent("test_image.jpg"));
        return ent;

    }
}
