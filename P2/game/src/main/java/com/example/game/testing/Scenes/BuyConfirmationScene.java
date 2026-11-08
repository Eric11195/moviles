package com.example.game.testing.Scenes;

import static com.example.game.testing.constants.Constants.LOGIC_WINDOW_SIZE;

import com.example.engine.ColorEng;
import com.example.engine.Engine;
import com.example.game.testing.EC.Component.DrawRectangleComponent;
import com.example.game.testing.EC.Component.TransformComponent;
import com.example.game.testing.EC.Component.UI.ButtonComponent;
import com.example.game.testing.EC.Component.shop.Buyable;
import com.example.game.testing.EC.ComponentId;
import com.example.game.testing.EC.Prefabs.ButtonPrefab;
import com.example.game.testing.EC.Prefabs.ShopItemPrefab;
import com.example.game.testing.EC.mcd_Entity;
import com.example.game.testing.utils.ColorLibrary;
import com.example.utils.Vec2;

import java.awt.Button;
import java.awt.Color;

//Push this scene. DO NOT SET THIS. This will pop itself after finished
public class BuyConfirmationScene extends mcd_Scene {
    Buyable elementToBuy;
    public BuyConfirmationScene(Engine myEngine, Buyable elementToBuy) {
        super(myEngine);
        this.elementToBuy = elementToBuy;
    }

    @Override
    public void start(Engine eng) {
        createEntity().addComponent(
            ComponentId.DRAW_RECTANGLE_COMPONENT,
            new DrawRectangleComponent(
                ColorLibrary.DarkGold,
                LOGIC_WINDOW_SIZE.x*0.20f,
                LOGIC_WINDOW_SIZE.y*0.20f,
                LOGIC_WINDOW_SIZE.x*0.6f,
                LOGIC_WINDOW_SIZE.y*0.6f
            )
        );

        ButtonComponent cancelBut = (ButtonComponent) ButtonPrefab.createButton(
            this,
                new Vec2(
                    LOGIC_WINDOW_SIZE.x*0.33f,
                    LOGIC_WINDOW_SIZE.y*0.6f
                ),
                new Vec2(
                    LOGIC_WINDOW_SIZE.x*0.2f,
                    LOGIC_WINDOW_SIZE.y*0.12f
                ),
                "CANCEL",
                eng::popScene
        ).getComponent(ComponentId.BUTTON_COMPONENT);
        cancelBut.setIdleColor(new ColorEng(0xAA3939FF));

        ButtonComponent acceptBut = (ButtonComponent) ButtonPrefab.createButton(
                this,
                new Vec2(
                        LOGIC_WINDOW_SIZE.x*0.66f,
                        LOGIC_WINDOW_SIZE.y*0.6f
                ),
                new Vec2(
                        LOGIC_WINDOW_SIZE.x*0.2f,
                        LOGIC_WINDOW_SIZE.y*0.12f
                ),
                "CONFIRM",
                ()->{
                    eng.popScene();
                }
        ).getComponent(ComponentId.BUTTON_COMPONENT);
        acceptBut.setIdleColor(new ColorEng(0x2D882DFF));

        mcd_Entity ent = ShopItemPrefab.createShopItem(this, this.elementToBuy);
        TransformComponent tr = (TransformComponent) ent.getComponent(
            ComponentId.TRANSFORM_COMPONENT
        );
        tr.pos.x = LOGIC_WINDOW_SIZE.x * 0.25f;
        tr.pos.y = LOGIC_WINDOW_SIZE.y * 0.25f;
        tr.size.x = LOGIC_WINDOW_SIZE.x * 0.25f;
        tr.size.y = LOGIC_WINDOW_SIZE.y * 0.25f;
        addEntity(ent);
    }
}
