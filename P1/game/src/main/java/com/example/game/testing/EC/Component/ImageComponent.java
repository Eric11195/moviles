package com.example.game.testing.EC.Component;

import com.example.engine.GraphicsInterface;
import com.example.engine.ImageEng;
import com.example.game.testing.EC.ComponentId;
import com.example.game.testing.EC.mcd_Component;
import com.example.game.testing.EC.mcd_Entity;

public class ImageComponent implements mcd_Component {
    private ImageEng img;
    private boolean dirty = true;
    private String filePath;
    public ImageComponent(String _filePath){
        setImg(_filePath);
    }

    @Override
    public void update(mcd_Entity ent, float t, float dt) {}
    public void setImg(String _filePath){
        filePath = _filePath;
        dirty=true;
    }

    @Override
    public void render(mcd_Entity ent, GraphicsInterface g) {
        if(ent.hasComponent(ComponentId.TRANSFORM_COMPONENT)){
            TransformComponent tr = (TransformComponent) ent.getComponent(ComponentId.TRANSFORM_COMPONENT);
            if(dirty) img = g.createImage(filePath);
            g.drawImage(img,tr.pos,tr.size);
        }
    }
}
