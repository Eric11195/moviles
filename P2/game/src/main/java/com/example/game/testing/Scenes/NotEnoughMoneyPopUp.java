package com.example.game.testing.Scenes;

import static com.example.game.testing.constants.Constants.LOGIC_WINDOW_SIZE;

import com.example.engine.ColorEng;
import com.example.engine.Engine;
import com.example.engine.GraphicsInterface;
import com.example.engine.Scene;
import com.example.game.testing.EC.Component.DrawMultipleRectanglesComponent;
import com.example.game.testing.EC.Component.UI.ButtonComponent;
import com.example.game.testing.EC.Component.shop.Buyable;
import com.example.game.testing.EC.ComponentId;
import com.example.game.testing.EC.Prefabs.ButtonPrefab;
import com.example.game.testing.EC.Prefabs.TextPrefab;
import com.example.game.testing.Systems.RubySystem;
import com.example.game.testing.Systems.Shop.FontManager;
import com.example.game.testing.utils.ColorLibrary;
import com.example.utils.Vec2;

//Push this scene. DO NOT SET THIS. This will pop itself after finished
public class NotEnoughMoneyPopUp extends mcd_Scene {
    Buyable elementToBuy;
    Scene previousScene;
    public NotEnoughMoneyPopUp(Engine myEngine, mcd_Scene previousScene, Buyable elementToBuy) {
        super(myEngine);
        this.elementToBuy = elementToBuy;
        this.previousScene = previousScene;
    }

    @Override
    public void start(Engine eng) {
        createEntity().addComponent(
            ComponentId.DRAW_RECTANGLE_COMPONENT,
            new DrawMultipleRectanglesComponent(
                ColorLibrary.Yellow,
                LOGIC_WINDOW_SIZE.x*0.10f,
                LOGIC_WINDOW_SIZE.y*0.10f,
                LOGIC_WINDOW_SIZE.x*0.8f,
                LOGIC_WINDOW_SIZE.y*0.8f
            )
        );

        ButtonComponent okBut = (ButtonComponent) ButtonPrefab.createButton(
            this,
                new Vec2(
                    LOGIC_WINDOW_SIZE.x*0.25f,
                    LOGIC_WINDOW_SIZE.y*0.7f
                ),
                new Vec2(
                    LOGIC_WINDOW_SIZE.x*0.5f,
                    LOGIC_WINDOW_SIZE.y*0.12f
                ),
                "OK",
                eng::popScene,
                new ColorEng(0xFFAA3939)
        ).getComponent(ComponentId.BUTTON_COMPONENT);

        TextPrefab.createText(
                this,
                new Vec2(LOGIC_WINDOW_SIZE.x*0.1f, LOGIC_WINDOW_SIZE.y*0.2f),
                new Vec2(LOGIC_WINDOW_SIZE.x*0.8f,LOGIC_WINDOW_SIZE.y* 0.3f),
                "Not enough rubies to buy this item.",
                ColorLibrary.Black.clone(),
                FontManager.Fonts.TITLE_FONT
        );
        TextPrefab.createText(
                this,
                new Vec2(LOGIC_WINDOW_SIZE.x*0.1f, LOGIC_WINDOW_SIZE.y*0.3f),
                new Vec2(LOGIC_WINDOW_SIZE.x*0.8f,LOGIC_WINDOW_SIZE.y* 0.4f),
                "("+ Integer.toString(RubySystem.getRubies())+"/"+elementToBuy.price+")",
                ColorLibrary.Black.clone(),
                FontManager.Fonts.TITLE_FONT
        );
    }

    @Override
    public void render(GraphicsInterface g){
        this.previousScene.render(g);
        innerRender(g);
    }
}
