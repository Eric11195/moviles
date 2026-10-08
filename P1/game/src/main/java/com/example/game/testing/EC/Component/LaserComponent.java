package com.example.game.testing.EC.Component;

import com.example.engine.ColorEng;
import com.example.game.testing.EC.ComponentId;
import com.example.game.testing.EC.mcd_Component;
import com.example.game.testing.EC.mcd_Entity;
import com.example.engine.GraphicsInterface;

import com.example.utils.Vec2;

import javax.swing.ComponentInputMap;

public class LaserComponent implements mcd_Component{

    //number of times it has to appear and dissapear for it to "attack".
    int num_rep;
    //duration of 1 of the numRep animations.
    double anim_dur;
    double anim_timer;

    double attack_dur;

    boolean is_attacking;

    int index_rep;

    ColorEng color;
    LaserComponent(int numRepetitions, int anim_duration)
    {
        num_rep=numRepetitions;
        anim_timer = anim_dur =anim_duration;
        attack_dur = 10;
        is_attacking = false;
        index_rep = 0;
        color = new ColorEng(255,0,0,255);
    }
    @Override
    public void render(mcd_Entity ent, GraphicsInterface g) {
        if(ent.hasComponent(ComponentId.TRANSFORM_COMPONENT))
        {
            if(!is_attacking)
            {
                if(anim_timer>anim_dur/2)
                {

                //here we need to change the color
                float progress =(float)( (anim_timer - anim_dur / 2f) / (anim_dur / 2f));
                color.a = (int)(255 - 155 * progress);
                }
                else
                {
                float progress = (float) (anim_timer / (anim_dur / 2f));
                color.a = (int) (100 + 155 * progress);
                }
            }
            TransformComponent tr = (TransformComponent) ent.getComponent(ComponentId.TRANSFORM_COMPONENT);

            g.setColor(color);
            g.drawRectangle(tr.pos,tr.size,true);

        }
    }

    @Override
    public void update(mcd_Entity ent, float t,float dt) {
        //here we need to figure out if it is time to attack or not
        //the time would be after
        if(!is_attacking)
        {
            if(anim_timer>0)
            {
                anim_dur -=dt;
            }
            else
            {
                anim_timer = anim_dur;
                num_rep--;
                if(num_rep == 0){is_attacking = true;}
            }
        }
        else
        {
            if(ent.hasComponent(ComponentId.COLLIDER_COMPONENT))
            {
                if(attack_dur>0)
                {
                    attack_dur-=dt;
                }
                else{  ent.rmComponent(ComponentId.COLLIDER_COMPONENT);}
            }
            else{
                ent.addComponent(ComponentId.COLLIDER_COMPONENT, new ColliderComponent(10,10,1));
            }
        }
    }
}
