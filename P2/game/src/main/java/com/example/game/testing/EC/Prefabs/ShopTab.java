package com.example.game.testing.EC.Prefabs;

import static com.example.game.testing.constants.Constants.LOGIC_WINDOW_SIZE;

import com.example.game.testing.EC.Component.TextRectComponent;
import com.example.game.testing.EC.Component.TransformComponent;
import com.example.game.testing.EC.Component.UI.TabComponent;
import com.example.game.testing.EC.ComponentId;
import com.example.game.testing.EC.mcd_Entity;
import com.example.game.testing.Scenes.mcd_Scene;
import com.example.game.testing.utils.ColorLibrary;
import com.example.utils.Vec2;

public class ShopTab {
    public static mcd_Entity createShopTab(mcd_Scene scene, TabComponent.TabbedMenu tabbedMenu, String text, int tabIdx, int totalTabs){
        mcd_Entity ent = scene.createEntity();
        TransformComponent tr = (TransformComponent) ent.addComponent(ComponentId.TRANSFORM_COMPONENT, new TransformComponent());
        tr.size.x = LOGIC_WINDOW_SIZE.x / (float)totalTabs;
        tr.size.y = LOGIC_WINDOW_SIZE.y / 14.0f;
        tr.pos.x = tr.size.x*tabIdx;
        tr.pos.y=0;
        ent.addComponent(ComponentId.TEXT_COMPONENT, new TextRectComponent(text, ColorLibrary.Black));
        ent.addComponent(ComponentId.BUTTON_COMPONENT, new TabComponent(tabbedMenu,tabIdx));
        return ent;
    }
}
