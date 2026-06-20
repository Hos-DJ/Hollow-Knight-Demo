package com.Ap.HollowKnight.model.map;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;

public class TiledMapHelper {
    private TiledMap tiledMap;

    public TiledMap loadMap (String path){
        tiledMap = new TmxMapLoader().load(path);
        return tiledMap;
    }

    public ArrayList<Block> getRectangles(){
        ArrayList<Block> blocks = new ArrayList<>();
        MapLayer layer = tiledMap.getLayers().get("collision objects");
        for (MapObject object : layer.getObjects()) {

            if (object instanceof RectangleMapObject) {

                Rectangle rect = ((RectangleMapObject) object).getRectangle();
                String type = object.getProperties().get("type", String.class);
                BlockType blockType = BlockType.fromName(type);

                blocks.add(new Block(rect ,  blockType));
            }
        }
        return blocks;
    }

}
