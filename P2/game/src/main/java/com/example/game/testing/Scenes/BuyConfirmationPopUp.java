package com.example.game.testing.Scenes;

import static com.example.game.testing.constants.Constants.LOGIC_WINDOW_SIZE;

import com.example.engine.ColorEng;
import com.example.engine.Engine;
import com.example.engine.GraphicsInterface;
import com.example.engine.Scene;
import com.example.game.testing.EC.Component.DrawRectangleComponent;
import com.example.game.testing.EC.Component.TransformComponent;
import com.example.game.testing.EC.Component.UI.ButtonComponent;
import com.example.game.testing.EC.Component.shop.Buyable;
import com.example.game.testing.EC.ComponentId;
import com.example.game.testing.EC.Prefabs.ButtonPrefab;
import com.example.game.testing.EC.Prefabs.ShopItemPrefab;
import com.example.game.testing.EC.mcd_Entity;
import com.example.game.testing.Systems.RubySystem;
import com.example.game.testing.utils.ColorLibrary;
import com.example.utils.Vec2;

//Push this scene. DO NOT SET THIS. This will pop itself after finished
public class BuyConfirmationPopUp extends mcd_Scene {
    Buyable elementToBuy;
    Scene previousScene;
    public BuyConfirmationPopUp(Engine myEngine, mcd_Scene previousScene, Buyable elementToBuy) {
        super(myEngine);
        this.elementToBuy = elementToBuy;
        this.previousScene = previousScene;
    }

    @Override
    public void start(Engine eng) {
        createEntity().addComponent(
            ComponentId.DRAW_RECTANGLE_COMPONENT,
            new DrawRectangleComponent(
                ColorLibrary.Yellow,
                LOGIC_WINDOW_SIZE.x*0.10f,
                LOGIC_WINDOW_SIZE.y*0.10f,
                LOGIC_WINDOW_SIZE.x*0.8f,
                LOGIC_WINDOW_SIZE.y*0.8f
            )
        );

        ButtonComponent cancelBut = (ButtonComponent) ButtonPrefab.createButton(
            this,
                new Vec2(
                    LOGIC_WINDOW_SIZE.x*0.25f,
                    LOGIC_WINDOW_SIZE.y*0.7f
                ),
                new Vec2(
                    LOGIC_WINDOW_SIZE.x*0.2f,
                    LOGIC_WINDOW_SIZE.y*0.12f
                ),
                "CANCEL",
                eng::popScene
        ).getComponent(ComponentId.BUTTON_COMPONENT);
        cancelBut.setIdleColor(new ColorEng(0xFFAA3939));

        ButtonComponent acceptBut = (ButtonComponent) ButtonPrefab.createButton(
                this,
                new Vec2(
                        LOGIC_WINDOW_SIZE.x*0.55f,
                        LOGIC_WINDOW_SIZE.y*0.7f
                ),
                new Vec2(
                        LOGIC_WINDOW_SIZE.x*0.2f,
                        LOGIC_WINDOW_SIZE.y*0.12f
                ),
                "CONFIRM",
                ()->{
                    eng.popScene();
                    RubySystem.pay(elementToBuy.price);
                    elementToBuy.price = -1;
                }
        ).getComponent(ComponentId.BUTTON_COMPONENT);
        acceptBut.setIdleColor(new ColorEng(0xFF2D882D));

        mcd_Entity ent = ShopItemPrefab.createShopItem(this, this.elementToBuy);
        TransformComponent tr = (TransformComponent) ent.getComponent(
            ComponentId.TRANSFORM_COMPONENT
        );
        tr.pos.x = LOGIC_WINDOW_SIZE.x * 0.25f;
        tr.pos.y = LOGIC_WINDOW_SIZE.y * 0.15f;
        tr.size.x = LOGIC_WINDOW_SIZE.x * 0.5f;
        tr.size.y = LOGIC_WINDOW_SIZE.y * 0.5f;
        addEntity(ent);
    }

    @Override
    public void render(GraphicsInterface g){
        this.previousScene.render(g);
        super.render(g);
    }
}
