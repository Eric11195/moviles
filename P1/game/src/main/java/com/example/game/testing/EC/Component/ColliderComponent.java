package com.example.game.testing.EC.Component;

import static com.example.utils.MathUtils.cos;
import static com.example.utils.MathUtils.sin;

import com.example.engine.ColorEng;
import com.example.engine.GraphicsInterface;
import com.example.game.testing.EC.ComponentId;
import com.example.game.testing.EC.mcd_Component;
import com.example.game.testing.EC.mcd_Entity;
import com.example.utils.Vec2;

import java.util.ArrayList;

public class ColliderComponent implements mcd_Component {
    public static class CollisionData{
        mcd_Entity entity;
    }
    ArrayList<CollisionData> collisions = new ArrayList<CollisionData>();

    public static class ColliderData{
        Vec2 offset;
        int rad;
    }
    ArrayList<ColliderData> colliders = new ArrayList<ColliderData>();

    Vec2 center;

    // Creates a single circular collider with _rad radius
    public ColliderComponent(int _rad){
        ColliderData single = new ColliderData();
        single.rad = _rad;
        single.offset = new Vec2(0,0);
        colliders.add(single);
    }

    // Creates a single circular collider with _rad radius and displaced _offset from its origin.
    public ColliderComponent(int _rad, Vec2 _offset){
        ColliderData single = new ColliderData();
        single.rad = _rad;
        single.offset = _offset;
        colliders.add(single);
    }

    // Creates _n circular colliders with _rad radius in a horizontal line, with _x spacing in between.
    public ColliderComponent(int _rad, int _x, int _n){
        center = new Vec2(0,0);
        Vec2 origin = new Vec2(center.x-((float) (_x * (_n - 1)) /2),center.y);

        for (int i = 0; i<_n; i++){
            ColliderData single = new ColliderData();
            single.rad = _rad;
            single.offset =  new Vec2(origin.x+(_x*i),origin.y);
            colliders.add(single);
        }
    }

    // Creates _n circular colliders with _rad radius in a horizontal line, with _x spacing in between and a _rad rotation (in radians) from its center (0,0).
    public ColliderComponent(int _rad, int _x, int _n, float _rot){
        center = new Vec2(0,0);
        Vec2 origin = new Vec2(center.x-((float) (_x * (_n - 1)) /2),center.y);

        for (int i = 0; i<_n; i++){
            ColliderData single = new ColliderData();
            single.rad = _rad;
            Vec2 pos = new Vec2(origin.x+(_x*i),origin.y);
            pos = new Vec2(center.x + (pos.x-center.x)*cos(_rot)-(pos.y-center.y)*sin(_rot),
                    center.y + (pos.x-center.x)*sin(_rot)+(pos.y-center.y)*cos(_rot));
            single.offset = pos;
            colliders.add(single);
        }
    }

    // Creates _n circular colliders with _rad radius in a horizontal line, with _x spacing in between and a _rad rotation (in radians) from its _center.
    public ColliderComponent(int _rad, int _x, int _n, float _rot, Vec2 _center){
        center = _center;
        Vec2 origin = new Vec2(center.x-((float) (_x * (_n - 1)) /2),center.y);

        for (int i = 0; i<_n; i++){
            ColliderData single = new ColliderData();
            single.rad = _rad;
            Vec2 pos = new Vec2(origin.x+(_x*i),origin.y);
            pos = new Vec2(center.x + (pos.x-center.x)*cos(_rot)-(pos.y-center.y)*sin(_rot),
                    center.y + (pos.x-center.x)*sin(_rot)+(pos.y-center.y)*cos(_rot));
            single.offset = pos;
            colliders.add(single);
        }
    }

    // Rotates all colliders _rot degrees from their center point.
    public void rotate(float _rot){
        colliders.forEach(col -> {
            col.offset = new Vec2(center.x + (col.offset.x-center.x)*cos(_rot)-(col.offset.y-center.y)*sin(_rot),
                    center.y + (col.offset.x-center.x)*sin(_rot)+(col.offset.y-center.y)*cos(_rot));
        });
    }

    public void addCollision(mcd_Entity ent){
        var temp = new CollisionData();
        temp.entity = ent;
        collisions.add(temp);
    }
    public final ArrayList<CollisionData> getCollisions(){
        var temp = new ArrayList<>(collisions);
        collisions = new ArrayList<>();
        return temp;
    }

    private static boolean overlaps(TransformComponent _tr, TransformComponent _other_tr,
                                   ColliderData _cd, ColliderData _other_cd){
        return ((_tr.pos.add(_cd.offset)).sub(_other_tr.pos.add(_other_cd.offset)).length()
                < _cd.rad + _other_cd.rad);
    }

    // Checks if any colliders between two entities are overlapping.
    public static boolean checkAllOverlaps(mcd_Entity ent, mcd_Entity other_ent){
        boolean collided = false;
        TransformComponent _tr = (TransformComponent) ent.getComponent(ComponentId.TRANSFORM_COMPONENT);
        TransformComponent _other_tr = (TransformComponent) other_ent.getComponent(ComponentId.TRANSFORM_COMPONENT);
        ArrayList<ColliderData> _cc = ((ColliderComponent) ent.getComponent(ComponentId.COLLIDER_COMPONENT)).colliders;
        ArrayList<ColliderData> _other_cc = ((ColliderComponent) other_ent.getComponent(ComponentId.COLLIDER_COMPONENT)).colliders;

        int i = 0;
        int j = 0;
        while (!collided && i<_cc.size()){
            while (!collided && j<_other_cc.size()){
                collided = overlaps(_tr,_other_tr,_cc.get(i),_other_cc.get(j));
            }
        }

        return collided;
    }


    @Override
    public void update(mcd_Entity ent, double dt) {

    }

    @Override
    public void render(mcd_Entity ent, GraphicsInterface g) {
        colliders.forEach(col ->{
            g.setColor(new ColorEng(255,0,0,255));
            g.drawCircle(((TransformComponent)ent.getComponent(ComponentId.TRANSFORM_COMPONENT)).pos.add(col.offset),
                    col.rad, false);
        });
    }
}
