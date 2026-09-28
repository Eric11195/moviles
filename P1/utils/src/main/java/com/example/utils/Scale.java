package com.example.utils;

public class Scale {
    double rotation=0;
    Vec2 offset=new Vec2(0,0);
    Vec2 scale=new Vec2(1,1);

    int expected_resolution_width=1280, expected_resolution_height=720;
    int virtual_resolution_width=1280, virtual_resolution_height=720;

    public void translate(int x, int y){
        offset.x += x;
        offset.y += y;
    }
    public void setOffset(int x, int y){
        offset.x = x;
        offset.y = y;
    }
    public void scale(double x_mult, double y_mult){
        scale.x *= x_mult;
        scale.y *= y_mult;
    }
    public void set_virtual_resolution(int width, int height){
        virtual_resolution_height=height;
        virtual_resolution_width=width;
        calculate_scale_with_current_resolutions();
    }
    public void rotate(float additive_rotation_in_degrees){
        rotation += Math.PI * additive_rotation_in_degrees/180;
    }
    public void setRotation(float new_rotation_in_degrees){
        rotation = Math.PI * new_rotation_in_degrees/180;
    }
    private void calculate_scale_with_current_resolutions(){
        scale.x = (double)virtual_resolution_width / expected_resolution_width;
        scale.y = (double)virtual_resolution_height / expected_resolution_height;
    }
    public void cleanup_all_transformations(){
        virtual_resolution_width=1280; virtual_resolution_height=720;
        scale.x=scale.y=1;
        offset.x=offset.y=0;
        rotation = 0;
    }
    //Rotation, offset, scale
    public void cleanup_regular_transformations(){
        calculate_scale_with_current_resolutions();
        offset.x=offset.y=0;
        rotation=0;
    }

    public static class Vec2{
        double x, y;
        public Vec2(double x, double y){
            this.x=x;
            this.y=y;
        }
        public Vec2 add(Vec2 toAdd){
            this.x += toAdd.x;
            this.y += toAdd.y;
            return this;
        }
        public Vec2 mult(Vec2 toMult){
            this.x *= toMult.x;
            this.y *= toMult.y;
            return this;
        }
    }
    public static class CoordRect{
        Vec2 ul; // up left corner pos
        Vec2 ur; // up right corner pos
        Vec2 dl; // down left corner pos
        Vec2 dr; // down right corner pos
        public CoordRect(Vec2 top_left, Vec2 down_right){
            this.ul = new Vec2(top_left.x, top_left.y);
            this.ur = new Vec2(down_right.x, top_left.y);
            this.dl = new Vec2(top_left.x, down_right.y);
            this.dr = new Vec2(down_right.x, down_right.y);
        }
        public CoordRect add(Vec2 toAdd){
            ul.add(toAdd);
            ur.add(toAdd);
            dl.add(toAdd);
            dr.add(toAdd);
            return this;
        }
        public CoordRect mult(Vec2 toMult){
            ul.mult(toMult);
            ur.mult(toMult);
            dl.mult(toMult);
            dr.mult(toMult);
            return this;
        }
        public CoordRect rotateFromCenter(double rotation){
            Vec2 center = new Vec2(
                    (ul.x + dr.x) / 2.0,
                    (ul.y + dr.y) / 2.0
            );
            double cos = Math.cos(rotation);
            double sin = Math.sin(rotation);
            rotatePoint(this.ul, center, cos,sin);
            rotatePoint(this.ur, center, cos,sin);
            rotatePoint(this.dl, center, cos,sin);
            rotatePoint(this.dr, center, cos,sin);
            return this;
        }
        private static void rotatePoint(Vec2 point, Vec2 center, double cos, double sin){
            // Position relative to center
            double x = point.x - center.x;
            double y = point.y - center.y;

            // Rotate
            double rotatedX = x * cos - y * sin;
            double rotatedY = x * sin + y * cos;

            // Move back to world coordinates
            point.x = rotatedX + center.x;
            point.y = rotatedY + center.y;
        }
    }
    public CoordRect transform_rect(CoordRect cr){
        return cr.add(this.offset).mult(this.scale).rotateFromCenter(this.rotation);
    }
}
