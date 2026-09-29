package com.example.game.testing.EC.Component;

import com.example.engine.GraphicsInterface;
import com.example.engine.ImageEng;
import com.example.game.testing.EC.ComponentId;
import com.example.game.testing.EC.mcd_Component;
import com.example.game.testing.EC.mcd_Entity;

public class ImageComponent implements mcd_Component {
    private ImageEng img;
    private String filePath;
    public ImageComponent(String _filePath){
        filePath = _filePath;
    }

    @Override
    public void update(mcd_Entity ent, double dt) {}

    @Override
    public void render(mcd_Entity ent, GraphicsInterface g) {
        if(ent.hasComponent(ComponentId.TRANSFORM_COMPONENT)){
            TransformComponent tr = (TransformComponent) ent.getComponent(ComponentId.TRANSFORM_COMPONENT);
            if(img==null) img = g.createImage(filePath);
            g.drawImage(img,tr.pos,tr.size);
        }
    }
}
