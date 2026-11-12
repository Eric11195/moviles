package com.example.game.testing.Scenes;

import static com.example.game.testing.constants.Constants.BACK_BUTTON_SQUARE_SIZE;
import static com.example.game.testing.constants.Constants.COLS_IN_SHOP;
import static com.example.game.testing.constants.Constants.LOGIC_WINDOW_SIZE;
import static com.example.game.testing.constants.Constants.ROWS_IN_SHOP;
import static com.example.game.testing.constants.Constants.TAB_HEIGHT;

import com.example.engine.ColorEng;
import com.example.engine.Engine;
import com.example.engine.IFileStorage;
import com.example.game.testing.EC.Component.UI.TabComponent;
import com.example.game.testing.EC.Component.shop.Buyable;
import com.example.game.testing.EC.ComponentId;
import com.example.game.testing.EC.Prefabs.BackButtonPrefab;
import com.example.game.testing.EC.Prefabs.ShopItemPrefab;
import com.example.game.testing.EC.Prefabs.ShopTab;
import com.example.game.testing.EC.mcd_Entity;
import com.example.game.testing.Systems.menu.Scrollable;
import com.example.game.testing.utils.ColorLibrary;
import com.example.game.testing.utils.JSON_HELPER;
import com.example.utils.Vec2;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ShopScene extends mcd_Scene implements TabComponent.TabbedMenu {
    protected ShopScene(Engine myEngine) {
        super(myEngine);
    }
    List<Scrollable> scrollableList;
    List<mcd_Entity> tabList;
    private static final Vec2 pos = new Vec2(
        BACK_BUTTON_SQUARE_SIZE,
        TAB_HEIGHT
    );
    private static final Vec2 size = new Vec2(
        LOGIC_WINDOW_SIZE.x-BACK_BUTTON_SQUARE_SIZE,
        LOGIC_WINDOW_SIZE.y  - BACK_BUTTON_SQUARE_SIZE-TAB_HEIGHT
    );

    @Override
    public void start(Engine eng) {
        scrollableList = new ArrayList<>();
        tabList = new ArrayList<>();
        //TODO: create an object that displays rubies
        IFileStorage fs = eng.getFileStorage();
        fs.setRelativePath("unlockables");
        final int[] i = {0};
        fs.forEachFileOnDirectory((filename)->{
            fs.setRelativePath("unlockables/"+filename+"/");
            int finalI = i[0];
            tabList.add(ShopTab.createShopTab(this, this, filename,finalI, new ColorEng(0xFFFFFFFF)));
            // Open folder
            // Count number of elements
            List<mcd_Entity> entOfThisTab = new ArrayList<>();

            fs.forEachFileOnDirectory((fileNameInner)->{
                JSONObject jo =
                        JSON_HELPER.fromJSON(
                                fs,
                                fileNameInner
                        );
                if(jo==null) return;
                entOfThisTab.add(
                        ShopItemPrefab.createShopItem(
                                this,
                                Buyable.deserialize(
                                        fileNameInner,
                                        filename,
                                        eng.getGraphics(),
                                        jo
                                )
                        )
                );
            });
            scrollableList.add(new Scrollable(pos,size, ROWS_IN_SHOP, COLS_IN_SHOP, entOfThisTab));
            ++i[0];
        });
        final int numberOfTabs = i[0];
        tabList.forEach((ent)->{
            TabComponent t = (TabComponent)ent.getComponent(ComponentId.BUTTON_COMPONENT);
            ShopTab.positionTab(ent,t.idx,numberOfTabs);
        });
        if(numberOfTabs > 0) {
            openTab(0);
        }

        BackButtonPrefab.createBackButton(eng, this);
    }

    @Override
    public void openTab(int idx) {
        //TODO: hide all previous objects
        scrollableList.forEach((scrollable -> {
            scrollable.setActive(false);
        }));
        tabList.forEach((ent)->{
            TabComponent t = (TabComponent) ent.getComponent(ComponentId.BUTTON_COMPONENT);
            t.unselect(ent);
        });
        //TODO: show only the objects on this tab
        systemList.clear();
        mcd_Entity ent =tabList.get(idx);
        ((TabComponent) ent.getComponent(ComponentId.BUTTON_COMPONENT)).select(ent);
        Scrollable activeScrollable = scrollableList.get(idx);
        addSystem(activeScrollable);
        activeScrollable.setActive(true);
    }
}
