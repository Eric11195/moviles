package com.example.game.testing.EC.Prefabs;

import static com.example.game.testing.constants.Constants.LOGIC_WINDOW_SIZE;
import static com.example.game.testing.constants.Constants.TAB_HEIGHT;

import com.example.engine.ColorEng;
import com.example.game.testing.EC.Component.DrawSingleRectComponent;
import com.example.game.testing.EC.Component.TextRectComponent;
import com.example.game.testing.EC.Component.TransformComponent;
import com.example.game.testing.EC.Component.UI.TabComponent;
import com.example.game.testing.EC.ComponentId;
import com.example.game.testing.EC.mcd_Entity;
import com.example.game.testing.Scenes.mcd_Scene;
import com.example.game.testing.utils.ColorLibrary;
import com.example.utils.Vec2;

public class ShopTab {
    public static mcd_Entity createShopTab(mcd_Scene scene, TabComponent.TabbedMenu tabbedMenu, String text, int tabIdx, ColorEng clr){
        mcd_Entity ent = scene.createEntity();
        TransformComponent tr = (TransformComponent) ent.addComponent(ComponentId.TRANSFORM_COMPONENT, new TransformComponent());
        tr.size.y = TAB_HEIGHT;
        tr.pos.y=0;
        ent.addComponent(ComponentId.TEXT_COMPONENT, new TextRectComponent(text));
        ent.addComponent(ComponentId.BUTTON_COMPONENT, new TabComponent(tabbedMenu,tabIdx));
        ent.addComponent(ComponentId.DRAW_RECTANGLE_COMPONENT, new DrawSingleRectComponent(clr));
        return ent;
    }
    public static mcd_Entity positionTab(mcd_Entity ent, int tabIdx, int tabCount){
        TransformComponent tr = (TransformComponent) ent.getComponent(ComponentId.TRANSFORM_COMPONENT);
        tr.size.x = LOGIC_WINDOW_SIZE.x / (float)tabCount;
        tr.pos.x = tr.size.x*tabIdx;
        return ent;
    }
}
