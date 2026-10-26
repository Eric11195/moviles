package com.example.game.testing.EC.Prefabs;

import com.example.game.testing.EC.Component.ButtonComponent;
import com.example.game.testing.EC.Component.TextRectComponent;
import com.example.game.testing.EC.Component.TransformComponent;
import com.example.game.testing.EC.ComponentId;
import com.example.game.testing.EC.mcd_Entity;
import com.example.game.testing.Scenes.mcd_Scene;
import com.example.game.testing.utils.ColorLibrary;
import com.example.utils.Vec2;

import java.awt.Color;

public class ButtonPrefab {
    public static mcd_Entity createButton(mcd_Scene scene, Vec2 pos, Vec2 size, String text, ButtonComponent.ButtonClickFunction clickFunc){
        mcd_Entity ent = scene.createEntity();
        ent.addComponent(ComponentId.TRANSFORM_COMPONENT, new TransformComponent(pos.clone(), size.clone()));
        ent.addComponent(ComponentId.BUTTON_COMPONENT, new ButtonComponent(clickFunc));
        ent.addComponent(ComponentId.TEXT_COMPONENT, new TextRectComponent(text, ColorLibrary.Black));
        return ent;
    }
}
