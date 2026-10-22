package com.example.game.testing.EC.Component;

import static com.example.utils.MathUtils.cos;
import static com.example.utils.MathUtils.sin;

import com.example.engine.ColorEng;
import com.example.engine.GraphicsInterface;
import com.example.game.testing.EC.ComponentId;
import com.example.game.testing.EC.mcd_Component;
import com.example.game.testing.EC.mcd_Entity;
import com.example.game.testing.utils.Rotator;
import com.example.utils.Vec2;

import java.util.ArrayList;

public class ColliderComponent implements mcd_Component {

    private static final int COLLIDER_COUNT_HARD_CAP_PER_ENTITY = 20;
    public static class CollisionData{
        mcd_Entity entity;
    }
    ArrayList<CollisionData> collisions = new ArrayList<CollisionData>();

    public static class ColliderData extends Vec2{
        float rad;
        public ColliderData(Vec2 offset, float rad){
            this.x = offset.x;
            this.y = offset.y;
            this.rad = rad;
        }
    }
    ArrayList<ColliderData> colliders = new ArrayList<ColliderData>();

    Vec2 center = new Vec2(0,0);
    public ColliderComponent(){
    }

    // Creates a single circular collider with _rad radius
    public ColliderComponent(float _rad){
        addSingleCollider(new Vec2(0,0),_rad);
    }

    // Creates a single circular collider with _rad radius and displaced _offset from its origin.
    public ColliderComponent(float _rad, Vec2 _offset){
        addSingleCollider(_offset,_rad);
    }

    /**
     * Creates a line of colliders and adds all of them to the colliders of this component.
     * This will not modify any other of the colliders this component already had.
     * This function will create as many collides as needed to ensure the line is completely covered
     * IMPORTANT. This will ensure that the number of colliders is not greater than the max cap of colliders per entity.
     * That number is equal to COLLIDER_COUNT_HARD_CAP_PER_ENTITY
     * @param startPos The first point that the Colliders cover in the line
     * @param endPos The last point that the Colliders cover in the line
     * @param lineWidth the width of the collider line. This means, the radius of each collider
     *                  will be lineWidth/2
     */
    public ColliderComponent(Vec2 startPos, Vec2 endPos, float lineWidth){
        addColliderLine(startPos,endPos,lineWidth);
    }
    /**
     * Creates a line of colliders and adds all of them to the colliders of this component.
     * This will not modify any other of the colliders this component already had.
     * @param startPos The center of the first Collider in the line
     * @param endPos The center of the last Collider in the line
     * @param lineWidth the width of the collider line. This means, the radius of each collider
     *                  will be lineWidth/2
     * @param maxColliderCount the maximum amount of colliders this function will add.
     *                          It will space the colliders evenly through the line
     *                          It will create less colliders if the line would already be completely
     *                          covered
     *                          This will not allow an entity to have more than
     *                          COLLIDER_COUNT_HARD_CAP_PER_ENTITY colliders.
     */

    public ColliderComponent(Vec2 startPos, Vec2 endPos, float lineWidth, int maxColliderCount){
        addColliderLine(startPos,endPos,lineWidth,maxColliderCount);
    }

    /**
     * Allows to set a center for the rotation of the colliders
     * @param rotationCenter center. Relative to the transform position of this entity
     */
    public void setCenter(Vec2 rotationCenter){
        center = rotationCenter;
    }

    // Rotates all colliders _rot degrees from their center point.
    public void rotate(float _rot){
        Rotator.rotate(_rot, center, colliders);
    }

    /**
     * Eliminates all colliders on this component
     */
    public void clearColliders(){
        colliders.clear();
    }

    /**
     * Creates a single collider and adds it to the colliders of this component.
     * This will not modify any other of the colliders this component already had.
     * @param relativeCenter the center point. It will get added the position of the
     *                       transform of this entity before checking for collisions
     * @param radius the radius of the collider from center
     */
    public void addSingleCollider(Vec2 relativeCenter, float radius){
        if(colliders.size() < COLLIDER_COUNT_HARD_CAP_PER_ENTITY)
            colliders.add(new ColliderData(relativeCenter, radius));
    }

    /**
     * Creates a line of colliders and adds all of them to the colliders of this component.
     * This will not modify any other of the colliders this component already had.
     * @param startPos The center of the first Collider in the line
     * @param endPos The center of the last Collider in the line
     * @param lineWidth the width of the collider line. This means, the radius of each collider
     *                  will be lineWidth/2
     * @param maxColliderCount the maximum amount of colliders this function will add.
     *                          It will space the colliders evenly through the line
     *                          It will create less colliders if the line would already be completely
     *                          covered
     *                          This will not allow an entity to have more than
     *                          COLLIDER_COUNT_HARD_CAP_PER_ENTITY colliders.
     */
    public void addColliderLine(Vec2 startPos, Vec2 endPos, float lineWidth, int maxColliderCount){
        Vec2 deltaDir = endPos.sub(startPos);
        deltaDir.normalize();
        float rad = lineWidth/2.0f;
        Vec2 realStartPos = startPos.add(deltaDir.mul(rad));
        Vec2 realEndPos = endPos.add(deltaDir.mul(-rad));
        // The min between maxColliderNumber &
        // the number of colliders that will completely cover the line without gaps
        // This will ensure the number of collider added its still lesser than
        // COLLIDER_COUNT_HARD_CAP_PER_ENTITY
        int realNumberOfColliders =
            Math.min(
                Math.min(
                    (int)Math.ceil(realEndPos.sub(realStartPos).length()/lineWidth),
                    maxColliderCount
                ),
                COLLIDER_COUNT_HARD_CAP_PER_ENTITY-colliders.size()
            );
        Vec2 delta = realEndPos.sub(realStartPos).mul(1.0f/(realNumberOfColliders-1));
        for(int i = 0; i < realNumberOfColliders; ++i){
            colliders.add(
                new ColliderData(
                    realStartPos.add(delta.mul(i)),
                    rad
                )
            );
        }
    }
    /**
     * Creates a line of colliders and adds all of them to the colliders of this component.
     * This will not modify any other of the colliders this component already had.
     * This function will create as many collides as needed to ensure the line is completely covered
     * IMPORTANT. This will ensure that the number of colliders is not greater than the max cap of colliders per entity.
     * That number is equal to COLLIDER_COUNT_HARD_CAP_PER_ENTITY
     * @param startPos The first point that the Colliders cover in the line
     * @param endPos The last point that the Colliders cover in the line
     * @param lineWidth the width of the collider line. This means, the radius of each collider
     *                  will be lineWidth/2
     */
    public void addColliderLine(Vec2 startPos, Vec2 endPos, float lineWidth){
        addColliderLine(startPos,endPos,lineWidth, COLLIDER_COUNT_HARD_CAP_PER_ENTITY-colliders.size());
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
        return ((_tr.pos.add(_cd)).sub(_other_tr.pos.add(_other_cd)).length()
                < _cd.rad + _other_cd.rad);
    }

    // Checks if any colliders between two entities are overlapping.
    public static boolean checkAllOverlaps(mcd_Entity ent, mcd_Entity other_ent){
        boolean collided = false;
        TransformComponent _tr = (TransformComponent) ent.getComponent(ComponentId.TRANSFORM_COMPONENT);
        TransformComponent _other_tr = (TransformComponent) other_ent.getComponent(ComponentId.TRANSFORM_COMPONENT);
        ArrayList<ColliderData> _cc = ((ColliderComponent) ent.getComponent(ComponentId.COLLIDER_COMPONENT)).colliders;
        ArrayList<ColliderData> _other_cc = ((ColliderComponent) other_ent.getComponent(ComponentId.COLLIDER_COMPONENT)).colliders;

        for(ColliderData collider1 : _cc){
            for(ColliderData collider2 : _other_cc){
                if(overlaps(_tr, _other_tr, collider1, collider2)){
                    return true;
                }
            }
        }
        return false;
    }


    @Override

    public void update(mcd_Entity ent, float t, float dt) {}

    @Override
    public void render(mcd_Entity ent, GraphicsInterface g) {
//        colliders.forEach(col ->{
//            g.setColor(new ColorEng(255,0,0,255));
//            g.drawCircle(((TransformComponent)ent.getComponent(ComponentId.TRANSFORM_COMPONENT)).pos.add(col),
//                    col.rad, false);
//        });
    }
}
