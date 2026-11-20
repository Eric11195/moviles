package com.example.game.testing.Scenes;

import com.example.engine.Engine;
import com.example.engine.IFileStorage;
import com.example.game.testing.EC.Component.TextRectComponent;
import com.example.game.testing.EC.Component.TransformComponent;
import com.example.game.testing.EC.ComponentId;
import com.example.game.testing.EC.Prefabs.BackButtonPrefab;
import com.example.game.testing.EC.Prefabs.RubyDisplayPrefab;
import com.example.game.testing.EC.mcd_Entity;
import com.example.game.testing.Systems.RubySystem;
import com.example.game.testing.Systems.Shop.ManagerManager;
import com.example.game.testing.constants.Constants;
import com.example.utils.Vec2;

public class RubyRewardScene extends mcd_Scene{
    int rewardAmount;
    public RubyRewardScene(Engine myEngine, int amount) {
        super(myEngine);
        rewardAmount = amount;
    }

    @Override
    public void start(Engine eng) {
        if (!ManagerManager.init(eng)) {
            eng.setScene(new CorruptedSaveFileScene(eng));
            return;
        }

        RubySystem.gainRubies(eng.getFileStorage(),rewardAmount);

        mcd_Entity ent = addEntity(new mcd_Entity(this));
        ent.addComponent(ComponentId.TRANSFORM_COMPONENT,new TransformComponent(
                new Vec2(Constants.LOGIC_WINDOW_SIZE.x*0.2f,Constants.LOGIC_WINDOW_SIZE.y * 0.3f),
                new Vec2(Constants.LOGIC_WINDOW_SIZE.x*0.6f,Constants.LOGIC_WINDOW_SIZE.y*0.2f)));
        ent.addComponent(ComponentId.TEXT_COMPONENT,new TextRectComponent("REWARD GET!!!"));

        addEntity(RubyDisplayPrefab.createRubyDisplay(this,
                new Vec2(Constants.LOGIC_WINDOW_SIZE.x*0.3f,Constants.LOGIC_WINDOW_SIZE.y*0.5f),
                new Vec2(Constants.LOGIC_WINDOW_SIZE.y*0.2f,Constants.LOGIC_WINDOW_SIZE.y*0.2f),
                new Vec2(Constants.LOGIC_WINDOW_SIZE.x*0.2f,0),
                new Vec2(Constants.LOGIC_WINDOW_SIZE.y*0.2f,Constants.LOGIC_WINDOW_SIZE.y*0.2f)
                ));

        BackButtonPrefab.createBackButton(eng,this);
    }
}
