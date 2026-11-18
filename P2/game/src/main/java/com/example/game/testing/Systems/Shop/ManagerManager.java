package com.example.game.testing.Systems.Shop;

import com.example.engine.Engine;
import com.example.engine.GraphicsInterface;
import com.example.engine.IFileStorage;
import com.example.game.testing.utils.JSON_HELPER;
import com.example.game.testing.utils.mcd_Serializable;
import com.example.game.testing.utils.ps1Runner;

import org.json.JSONObject;

public class ManagerManager implements mcd_Serializable {
    static boolean initialized = false;
    public static boolean init(Engine eng){
        if(initialized) return true;
        IFileStorage fileStorage = eng.getFileStorage();
        GraphicsInterface g = eng.getGraphics();

        try {
            JSON_HELPER.deserialize(eng, fileStorage);

            ColorManager.setInstance(ColorManager.deserialize(fileStorage));

            SkinManager.setInstance(SkinManager.deserialize(g, fileStorage));

            FontManager.setInstance(FontManager.deserialize(g, fileStorage));

            PowerUpManager.setInstance(PowerUpManager.deserialize(fileStorage));

            ShopManager.setInstance(ShopManager.deserialize(fileStorage));

            LevelKeyManager.setInstance(LevelKeyManager.deserialize(fileStorage));
        } catch (Throwable e) {
            e.printStackTrace();
            return false;
        }
        return initialized = true;
    }

    public static boolean isInitialized(){
        return initialized;
    }
    public static void reset(){
        initialized = false;
        try{
            ps1Runner.runScript(true, ".resetGameToDefault.ps1");
        }catch(Throwable t){
            t.printStackTrace();
        }
    }

    @Override
    public JSONObject serialize() {
        //Serialize the current skins and colors, and the rest of the stuff
        return null;
    }
}
