package com.example.game.testing.Systems.Shop;

import com.example.engine.GraphicsInterface;
import com.example.engine.IFileStorage;
import com.example.game.testing.utils.JSON_HELPER;
import com.example.game.testing.utils.mcd_Serializable;

import org.json.JSONObject;

import java.io.IOException;

public class ManagerManager implements mcd_Serializable {
    static boolean initialized = false;
    public static boolean init(IFileStorage fileStorage, GraphicsInterface g){
        if(initialized)return true;

        JSON_HELPER.deserialize(fileStorage);

        ColorManager.setInstance(ColorManager.deserialize(fileStorage));

        SkinManager.setInstance(SkinManager.deserialize(g,fileStorage));

        FontManager.setInstance(FontManager.deserialize(g,fileStorage));

        PowerUpManager.setInstance(PowerUpManager.deserialize(fileStorage));

        ShopManager.setInstance(ShopManager.deserialize(fileStorage));

        return initialized = true;
    }
    public static boolean isInitialized(){
        return initialized;
    }

    @Override
    public JSONObject serialize() {
        //Serialize the current skins and colors, and the rest of the stuff
        return null;
    }
}
