package com.example.game.testing.EC.Prefabs;
import com.example.game.testing.EC.Component.ImageComponent;
import com.example.game.testing.EC.Component.LaserComponent;
import com.example.game.testing.EC.Component.TransformComponent;
import com.example.game.testing.EC.ComponentId;
import com.example.game.testing.EC.mcd_Entity;
import com.example.game.testing.Scenes.mcd_Scene;
import com.example.utils.Vec2;
public class LaserPrefab {
    public static mcd_Entity createLaser(mcd_Scene my_scene, Vec2 pos)
    {
        mcd_Entity ent = my_scene.createEntity();
        ent.addComponent(ComponentId.TRANSFORM_COMPONENT, new TransformComponent(pos, new Vec2(600,100)));
        ent.addComponent(ComponentId.LASER_COMPONENT, new LaserComponent(3,4));
        return ent;

    }
}
