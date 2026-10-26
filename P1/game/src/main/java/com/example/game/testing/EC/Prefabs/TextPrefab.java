package com.example.game.testing.EC.Prefabs;

import com.example.engine.ColorEng;
import com.example.game.testing.EC.Component.TextRectComponent;
import com.example.game.testing.EC.Component.TransformComponent;
import com.example.game.testing.EC.ComponentId;
import com.example.game.testing.EC.mcd_Entity;
import com.example.game.testing.Scenes.mcd_Scene;
import com.example.utils.Vec2;

public class TextPrefab {
    public static mcd_Entity createText(mcd_Scene scene, Vec2 pos, Vec2 size, String text, ColorEng clr){
        mcd_Entity ent = scene.createEntity();
        ent.addComponent(ComponentId.TRANSFORM_COMPONENT, new TransformComponent(pos.clone(),size.clone()));
        ent.addComponent(ComponentId.TEXT_COMPONENT, new TextRectComponent(text, clr));
        return ent;
    }
}
