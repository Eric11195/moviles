package com.example.game.testing.EC.Prefabs;

import com.example.game.testing.EC.Component.TransformComponent;
import com.example.game.testing.EC.Component.UI.RubyDisplayComponent;
import com.example.game.testing.EC.ComponentId;
import com.example.game.testing.EC.mcd_Entity;
import com.example.game.testing.Scenes.mcd_Scene;
import com.example.utils.Vec2;

public class RubyDisplayPrefab {
    public static mcd_Entity createRubyDisplay(mcd_Scene scene, Vec2 pos, Vec2 size){
        mcd_Entity ent = new mcd_Entity(scene);
        ent.addComponent(ComponentId.TRANSFORM_COMPONENT,new TransformComponent(pos,size));
        ent.addComponent(ComponentId.RUBY_DISPLAY_COMPONENT, new RubyDisplayComponent());
        return ent;
    }
}
