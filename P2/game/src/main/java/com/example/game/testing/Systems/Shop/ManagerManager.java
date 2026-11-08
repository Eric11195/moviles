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
        fileStorage.setRelativePath("presets");

        JSONObject joColor = JSON_HELPER.fromJSON(fileStorage,"colors");
        if(joColor==null)return false;
        ColorManager.setInstance(ColorManager.deserialize(joColor));

        JSONObject joSkins = JSON_HELPER.fromJSON(fileStorage,"skins");
        if(joSkins==null)return false;
        SkinManager.setInstance(SkinManager.deserialize(g,joSkins));

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
