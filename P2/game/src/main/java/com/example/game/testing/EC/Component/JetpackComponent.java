package com.example.game.testing.EC.Component;

import static com.example.game.testing.constants.Constants.GRAVITY_FORCE;
import static com.example.game.testing.constants.Constants.LOGIC_WINDOW_SIZE;
import static com.example.game.testing.constants.Constants.MAX_PLAYER_FALL_SPEED;
import static com.example.game.testing.constants.Constants.MAX_PLAYER_UPWARDS_SPEED;
import static com.example.game.testing.constants.Constants.PLAYER_DIMENSIONS;
import static com.example.game.testing.constants.Constants.Y_PLAYER_ACCEL;

import com.example.engine.GraphicsInterface;
import com.example.engine.ImageEng;
import com.example.game.testing.EC.ComponentId;
import com.example.game.testing.EC.mcd_Component;
import com.example.game.testing.EC.mcd_Entity;
import com.example.game.testing.Systems.Shop.ColorManager;
import com.example.game.testing.Systems.Shop.SkinManager;
import com.example.game.testing.utils.ColorLibrary;
import com.example.utils.Vec2;

public class JetpackComponent implements mcd_Component{
    boolean isFlying;
    float playerRad = Math.min(PLAYER_DIMENSIONS.x,PLAYER_DIMENSIONS.y)/2;
    Vec2 halfsizes = new Vec2(PLAYER_DIMENSIONS.x/2,PLAYER_DIMENSIONS.y/2);
    public JetpackComponent(){
        this.img = SkinManager.getInstance().getImage(SkinManager.Images.PLAYER);
    }
    private void checkSpeed(mcd_Entity playerEnt)
    {
        VelocityComponent velComp = (VelocityComponent) playerEnt.getComponent(ComponentId.VELOCITY_COMPONENT);
        velComp.velocity.y=Math.max(-MAX_PLAYER_UPWARDS_SPEED,Math.min(MAX_PLAYER_FALL_SPEED,velComp.velocity.y));
    }
    private  void respectBorders(mcd_Entity ent)
    {
        TransformComponent tr = (TransformComponent) ent.getComponent(ComponentId.TRANSFORM_COMPONENT);
        VelocityComponent vel = (VelocityComponent) ent.getComponent(ComponentId.VELOCITY_COMPONENT);
        tr.pos.y=Math.max(0,Math.min(LOGIC_WINDOW_SIZE.y-tr.size.y,tr.pos.y));
        if(tr.pos.y<=0){
            vel.velocity.y = Math.max(vel.velocity.y, 0);
        }
        else if(tr.pos.y>=LOGIC_WINDOW_SIZE.y - tr.size.y)
        {
            vel.velocity.y = Math.min(vel.velocity.y,0);
        }
    }

    public void applyThrustAndGravity(mcd_Entity ent, float dt){
        TransformComponent tr = (TransformComponent) ent.getComponent(ComponentId.TRANSFORM_COMPONENT);
        VelocityComponent vel = (VelocityComponent) ent.getComponent(ComponentId.VELOCITY_COMPONENT);
        if (isFlying) {
            vel.velocity.y += -Y_PLAYER_ACCEL * dt;
        }
        vel.velocity.y+= GRAVITY_FORCE*dt;
    }
    ImageEng img = null;
    @Override
    public void render(mcd_Entity ent, GraphicsInterface g)
    {
        TransformComponent tr = (TransformComponent) ent.getComponent(ComponentId.TRANSFORM_COMPONENT);
        Vec2 pos = new Vec2(tr.pos.add(halfsizes));
        if(img==null) {
            g.setStrokeWidth(3);

            g.setColor(ColorManager.getInstance().getColor(ColorManager.Colors.PLAYER_COLOR));
            g.drawCircle(pos, playerRad, true);

            g.setColor(ColorLibrary.DarkGrey);
            g.drawCircle(pos, playerRad, false);
        }else{
            g.drawImage(img, pos.add(new Vec2(-playerRad,-playerRad)), PLAYER_DIMENSIONS);
        }
    }

    @Override
    public void update(mcd_Entity ent, float t , float dt) {
        if(ent.hasComponent(ComponentId.TRANSFORM_COMPONENT)){
            respectBorders(ent);
            applyThrustAndGravity(ent, dt);
            checkSpeed(ent);
        }
    }
    public void setIsFlying(boolean value){
        isFlying = value;
    }
}
