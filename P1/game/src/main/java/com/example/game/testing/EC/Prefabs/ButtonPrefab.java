package com.example.game.testing.EC.Prefabs;

import com.example.game.testing.EC.Component.ButtonComponent;
import com.example.game.testing.EC.Component.TransformComponent;
import com.example.game.testing.EC.ComponentId;
import com.example.game.testing.EC.mcd_Entity;
import com.example.game.testing.Scenes.mcd_Scene;
import com.example.utils.Vec2;

public class ButtonPrefab {
    public static mcd_Entity createButton(mcd_Scene scene, Vec2 pos, String text, ButtonComponent.ButtonClickFunction clickFunc){
        mcd_Entity ent = scene.createEntity();
        ent.addComponent(ComponentId.TRANSFORM_COMPONENT, new TransformComponent(pos, new Vec2(100,40)));
        ent.addComponent(ComponentId.BUTTON_COMPONENT, new ButtonComponent(text,clickFunc));
        return ent;
    }
}
