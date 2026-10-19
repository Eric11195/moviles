package com.example.game.testing.EC.Prefabs;
import static com.example.game.testing.constants.Constants.LASER_HEIGHT;
import static com.example.game.testing.constants.Constants.LOGIC_WINDOW_SIZE;

import com.example.game.testing.EC.Component.ColliderComponent;
import com.example.game.testing.EC.Component.ImageComponent;
import com.example.game.testing.EC.Component.LaserComponent;
import com.example.game.testing.EC.Component.TransformComponent;
import com.example.game.testing.EC.ComponentId;
import com.example.game.testing.EC.mcd_Entity;
import com.example.game.testing.Scenes.mcd_Scene;
import com.example.utils.Vec2;
public class LaserPrefab {
    public static mcd_Entity createLaser(mcd_Scene my_scene, float yPos)
    {
        mcd_Entity ent = my_scene.createEntity();
        ent.addComponent(ComponentId.TRANSFORM_COMPONENT, new TransformComponent(new Vec2(0,yPos), new Vec2(LOGIC_WINDOW_SIZE.x,LASER_HEIGHT)));
        ent.addComponent(ComponentId.LASER_COMPONENT, new LaserComponent(ent,0,0,0));
        ent.addComponent(ComponentId.COLLIDER_COMPONENT, new ColliderComponent());
        return ent;

    }
}
