package com.example.game.testing.EC.Component.shop;

import com.example.engine.GraphicsInterface;
import com.example.engine.IFileStorage;
import com.example.game.testing.EC.Component.UI.ButtonComponent;
import com.example.game.testing.EC.Component.TransformComponent;
import com.example.game.testing.EC.ComponentId;
import com.example.game.testing.EC.mcd_Entity;
import com.example.game.testing.Scenes.BuyConfirmationPopUp;
import com.example.game.testing.Scenes.NotEnoughMoneyPopUp;
import com.example.game.testing.Scenes.mcd_Scene;
import com.example.game.testing.Systems.RubySystem;
import com.example.game.testing.Systems.Shop.ColorManager;
import com.example.game.testing.utils.ColorLibrary;
import com.example.utils.Vec2;

public class ShopObject extends ButtonComponent {
    Buyable buyable;
    public ShopObject(mcd_Scene scene, Buyable b) {
        super(()->{
            if(b.price==-1){
                equip(scene.getEngine().getFileStorage(), b);
            }else {
                buy(scene, b);
            }
        });
        this.buyable = b;
        this.idleColor = ColorLibrary.LightGrey;
    }

    @Override
    public void update(mcd_Entity ent, float t, float dt) {
        //TODO: detect click
        super.update(ent, t,dt);
    }

    @Override
    public void render(mcd_Entity ent, GraphicsInterface g) {
        super.render(ent, g);
        TransformComponent tr = (TransformComponent) ent.getComponent(ComponentId.TRANSFORM_COMPONENT);

        g.setColor(ColorLibrary.Black);
        g.drawRoundRectangle(tr.pos, tr.size, 12.0f, false);
        this.buyable.render(
            g,
            tr.pos.x +tr.size.x*0.1f,
            tr.pos.y+tr.size.y*0.1f,
            tr.size.x*0.8f,
            tr.size.y*0.7f
        );
        String text ="";
        if(this.buyable.price == -1){
            text = selected() ? "Equipped":"Bought";
        }else{
            text = Integer.toString(this.buyable.price);
        }
        g.drawText(
            text,
            new Vec2(tr.pos.x+tr.size.x*0.5f, tr.pos.y+tr.size.y*1.0f)
        );
    }
    boolean selected(){
        return buyable.selected();
    }

    private static void buy(mcd_Scene scene, Buyable b){
        if(RubySystem.canPay(b.price)){
            scene.getEngine().pushScene(new BuyConfirmationPopUp(scene.getEngine(),scene, b));
        }else{
            scene.getEngine().pushScene(new NotEnoughMoneyPopUp(scene.getEngine(), scene, b));
        }
    }
    private static void equip(IFileStorage fs, Buyable b){
        b.equip(fs);
    }
}
