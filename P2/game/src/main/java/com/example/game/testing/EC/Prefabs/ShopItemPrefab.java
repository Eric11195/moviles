package com.example.game.testing.EC.Prefabs;

import static com.example.game.testing.constants.Constants.BACK_BUTTON_SQUARE_SIZE;
import static com.example.game.testing.constants.Constants.COLS_IN_SHOP;
import static com.example.game.testing.constants.Constants.LOGIC_WINDOW_SIZE;
import static com.example.game.testing.constants.Constants.ROWS_IN_SHOP;
import static com.example.game.testing.constants.Constants.TAB_HEIGHT;

import com.example.engine.ColorEng;
import com.example.engine.GraphicsInterface;
import com.example.game.testing.EC.Component.TransformComponent;
import com.example.game.testing.EC.Component.shop.Buyable;
import com.example.game.testing.EC.Component.shop.ColorBuyable;
import com.example.game.testing.EC.Component.shop.ShopObject;
import com.example.game.testing.EC.Component.shop.SpriteBuyable;
import com.example.game.testing.EC.ComponentId;
import com.example.game.testing.EC.mcd_Entity;
import com.example.game.testing.Scenes.mcd_Scene;
import com.example.game.testing.Systems.Shop.ColorManager;
import com.example.game.testing.Systems.Shop.SkinManager;

public class ShopItemPrefab {
    public static mcd_Entity createShopItem(mcd_Scene scene,String fileName, int price, ColorEng color){
        mcd_Entity ent = new mcd_Entity(scene);
        TransformComponent tr = (TransformComponent) ent.addComponent(ComponentId.TRANSFORM_COMPONENT, new TransformComponent());
        tr.size.x = (LOGIC_WINDOW_SIZE.x-BACK_BUTTON_SQUARE_SIZE)/COLS_IN_SHOP;
        tr.size.y = (LOGIC_WINDOW_SIZE.y-BACK_BUTTON_SQUARE_SIZE-TAB_HEIGHT)/ROWS_IN_SHOP;
        ent.addComponent(ComponentId.SHOP_OBJECT_COMPONENT, new ShopObject(scene,new ColorBuyable(fileName,price, ColorManager.Colors.BACKGROUND_COLOR,color.clone())));
        return ent;
    }
    public static mcd_Entity createShopItem(mcd_Scene scene, String fileName, int price, GraphicsInterface g, String path, SkinManager.Images imgFor){
        mcd_Entity ent = new mcd_Entity(scene);
        TransformComponent tr = (TransformComponent) ent.addComponent(ComponentId.TRANSFORM_COMPONENT, new TransformComponent());
        tr.size.x = (LOGIC_WINDOW_SIZE.x-BACK_BUTTON_SQUARE_SIZE)/COLS_IN_SHOP;
        tr.size.y = (LOGIC_WINDOW_SIZE.y-BACK_BUTTON_SQUARE_SIZE-TAB_HEIGHT)/ROWS_IN_SHOP;
        ent.addComponent(ComponentId.SHOP_OBJECT_COMPONENT, new ShopObject(scene, new SpriteBuyable(fileName,price,g,path, imgFor)));
        return ent;
    }
    public static mcd_Entity createShopItem(mcd_Scene scene, Buyable b){
        mcd_Entity ent = new mcd_Entity(scene);
        TransformComponent tr = (TransformComponent) ent.addComponent(ComponentId.TRANSFORM_COMPONENT, new TransformComponent());
        tr.size.x = (LOGIC_WINDOW_SIZE.x-BACK_BUTTON_SQUARE_SIZE)/COLS_IN_SHOP;
        tr.size.y = (LOGIC_WINDOW_SIZE.y-BACK_BUTTON_SQUARE_SIZE-TAB_HEIGHT)/ROWS_IN_SHOP;
        ent.addComponent(ComponentId.SHOP_OBJECT_COMPONENT, new ShopObject(scene, b));
        return ent;
    }
}
