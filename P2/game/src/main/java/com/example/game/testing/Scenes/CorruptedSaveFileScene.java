package com.example.game.testing.Scenes;

import static com.example.game.testing.constants.Constants.LOGIC_WINDOW_SIZE;

import com.example.engine.Engine;
import com.example.game.testing.EC.Component.DrawSingleRectComponent;
import com.example.game.testing.EC.Component.TextRectComponent;
import com.example.game.testing.EC.Component.TransformComponent;
import com.example.game.testing.EC.Component.UI.ButtonComponent;
import com.example.game.testing.EC.ComponentId;
import com.example.game.testing.EC.mcd_Entity;
import com.example.game.testing.Systems.Shop.FontManager;
import com.example.game.testing.Systems.Shop.ManagerManager;
import com.example.game.testing.utils.ColorLibrary;
import com.example.game.testing.utils.JSON_HELPER;
import com.example.utils.Vec2;

import org.json.JSONObject;

public class CorruptedSaveFileScene extends mcd_Scene {
    public CorruptedSaveFileScene(Engine eng) {
        super(eng);
    }

    @Override
    public void start(Engine eng) {
        String[] sentences = {
            "Trying to access corrupt save file.",
            "All progress would be deleted.",
            "Press anywhere to continue"
        };

        float startX = LOGIC_WINDOW_SIZE.x * 0.15f;
        float startY = LOGIC_WINDOW_SIZE.y * 0.15f;
        float totalWidth = LOGIC_WINDOW_SIZE.x * 0.7f;
        float totalHeight = LOGIC_WINDOW_SIZE.y * 0.7f;
        float entityHeight = totalHeight / 3.0f;

        ButtonComponent.ButtonClickFunction onContinue = () -> {
            JSONObject jo = new JSONObject();
            JSON_HELPER.initializeHasherDefault(jo);
            JSON_HELPER.storeValuesFromJSON(jo);
            eng.setScene(new MainMenuScene(eng));
            if(!ManagerManager.init(eng)){
                throw new RuntimeException("Error initializing managers after cleanup");
            }
        };

        for (int i = 0; i < sentences.length; i++) {
            mcd_Entity ent = createEntity();
            ent.addComponent(ComponentId.TRANSFORM_COMPONENT, new TransformComponent(
                new Vec2(startX, startY + i * entityHeight),
                new Vec2(totalWidth, entityHeight)
            ));
            ent.addComponent(ComponentId.DRAW_RECTANGLE_COMPONENT, new DrawSingleRectComponent(ColorLibrary.Black));
            ent.addComponent(ComponentId.TEXT_COMPONENT, new TextRectComponent(
                sentences[i],
                ColorLibrary.Red,
                FontManager.Fonts.TITLE_FONT
            ));
            ent.addComponent(ComponentId.BUTTON_COMPONENT, new ButtonComponent(onContinue));
        }
    }
}
