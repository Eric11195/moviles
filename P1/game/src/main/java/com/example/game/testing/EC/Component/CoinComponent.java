package com.example.game.testing.EC.Component;

import static com.example.game.testing.constants.Constants.LOGIC_WINDOW_SIZE;

import com.example.game.testing.EC.mcd_Entity;
import com.example.game.testing.Scenes.mcd_Scene;
import com.example.game.testing.Systems.levelGenerator.DangerMatrix;

import org.json.JSONObject;

public class CoinComponent extends Obstacle {

    int value;

    public CoinComponent(float spawnTime, int _value) {
        super(spawnTime, 1);
        value = _value;
    }

    public CoinComponent(float spawnTime) {
        super(spawnTime, 1);
        value = 1;
    }


    @Override
    public float calculateScrolledTime(float spawnTime, float speedMult) {
        return spawnTime+ (LOGIC_WINDOW_SIZE.x)/speedMult;
    }

    @Override
    public void drawSelfInDangerMatrix(TransformComponent tr, DangerMatrix dm) {
        dm.drawSquare(tr.pos, tr.size);
    }

    @Override
    public void onPlayerCollision(mcd_Entity ent, mcd_Scene scene) {
        // ADD COIN
    }

    public void deserialize(JSONObject obj) {

    }
}
