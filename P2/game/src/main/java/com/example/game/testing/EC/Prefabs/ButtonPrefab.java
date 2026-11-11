package com.example.game.testing.EC.Prefabs;

import com.example.engine.ColorEng;
import com.example.engine.ImageEng;
import com.example.game.testing.EC.Component.DrawSingleRectComponent;
import com.example.game.testing.EC.Component.ImageComponent;
import com.example.game.testing.EC.Component.UI.ButtonComponent;
import com.example.game.testing.EC.Component.TextRectComponent;
import com.example.game.testing.EC.Component.TransformComponent;
import com.example.game.testing.EC.ComponentId;
import com.example.game.testing.EC.mcd_Entity;
import com.example.game.testing.Scenes.mcd_Scene;
import com.example.game.testing.Systems.Shop.ColorManager;
import com.example.game.testing.Systems.Shop.SkinManager;
import com.example.game.testing.utils.ColorLibrary;
import com.example.utils.Vec2;

public class ButtonPrefab {
    public static mcd_Entity createButton(mcd_Scene scene, Vec2 pos, Vec2 size, String text, ButtonComponent.ButtonClickFunction clickFunc){
        mcd_Entity ent = scene.createEntity();
        ent.addComponent(ComponentId.TRANSFORM_COMPONENT, new TransformComponent(pos.clone(), size.clone()));
        ImageEng buttonSkin = SkinManager.getInstance().getImage(SkinManager.Images.BUTTON);
        if(buttonSkin != null) {
            ent.addComponent(ComponentId.IMAGE_COMPONENT, new ImageComponent(buttonSkin));
        }else{
            ent.addComponent(ComponentId.DRAW_RECTANGLE_COMPONENT, new DrawSingleRectComponent(ColorManager.getInstance().getColor(ColorManager.Colors.BUTTONS_COLOR)));
        }
        ent.addComponent(ComponentId.BUTTON_COMPONENT, new ButtonComponent(clickFunc));
        ent.addComponent(ComponentId.TEXT_COMPONENT, new TextRectComponent(text));
        return ent;
    }
    public static mcd_Entity createButton(mcd_Scene scene, Vec2 pos, Vec2 size, String text, ButtonComponent.ButtonClickFunction clickFunc, ColorEng clr){
        mcd_Entity ent = scene.createEntity();
        ent.addComponent(ComponentId.TRANSFORM_COMPONENT, new TransformComponent(pos.clone(), size.clone()));
        ent.addComponent(ComponentId.DRAW_RECTANGLE_COMPONENT, new DrawSingleRectComponent(clr));
        ent.addComponent(ComponentId.BUTTON_COMPONENT, new ButtonComponent(clickFunc));
        ent.addComponent(ComponentId.TEXT_COMPONENT, new TextRectComponent(text));
        return ent;
    }
}
