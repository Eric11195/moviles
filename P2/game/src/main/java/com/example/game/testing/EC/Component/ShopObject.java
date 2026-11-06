package com.example.game.testing.EC.Component;

import com.example.engine.GraphicsInterface;
import com.example.game.testing.EC.ComponentId;
import com.example.game.testing.EC.mcd_Component;
import com.example.game.testing.EC.mcd_Entity;
import com.example.game.testing.Systems.RubySystem;
import com.example.utils.Vec2;

import java.awt.Button;

public class ShopObject extends ButtonComponent {
    Buyable buyable;
    public ShopObject(Buyable b) {
        super(()->{
            buy(b);
        });
        this.buyable = b;
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

        this.buyable.render(
            g,
            tr.pos.x + tr.size.x*0.1f,
            tr.pos.y+tr.size.y*0.1f,
            tr.size.x*0.8f,
            tr.size.y*0.8f
        );
        g.drawText(Integer.toString(
                this.buyable.price),
                new Vec2(tr.pos.x+tr.size.x*0.5f, tr.pos.y+tr.size.y*0.9f)
        );
    }

    private static void buy(Buyable b){
        if(RubySystem.canPay(b.price)){
            RubySystem.pay(b.price);
            //TODO: registrar item comprado
        }else{
            //Todo: mostrar mensaje de dinero insuficiente
        }
    }
}
