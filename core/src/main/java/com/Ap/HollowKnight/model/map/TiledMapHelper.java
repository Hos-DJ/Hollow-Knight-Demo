package com.Ap.HollowKnight.model.map;

import com.Ap.HollowKnight.model.boss.FalseKnight;
import com.Ap.HollowKnight.model.enemy.*;
import com.Ap.HollowKnight.model.zote.Zote;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.PointMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class TiledMapHelper {
    private TiledMap tiledMap;

    public TiledMap loadMap(String path) {
        tiledMap = new TmxMapLoader().load(path);
        return tiledMap;
    }

    public ArrayList<Block> getRectangles() {
        ArrayList<Block> blocks = new ArrayList<>();
        MapLayer layer = tiledMap.getLayers().get("collision objects");
        for (MapObject object : layer.getObjects()) {

            if (object instanceof RectangleMapObject) {

                Rectangle rect = ((RectangleMapObject) object).getRectangle();
                String type = object.getProperties().get("type", String.class);
                BlockType blockType = BlockType.fromName(type);
                if(blockType == BlockType.DESTRUCTIBLE_WALL||blockType == BlockType.GATE){
                    continue;
                }else{
                    blocks.add(new Block(rect, blockType));
                }
            }
        }
        return blocks;
    }

    public DestructibleWall getDestructibleWall() {
        MapLayer layer = tiledMap.getLayers().get("collision objects");
        for (MapObject object : layer.getObjects()) {

            if (object instanceof RectangleMapObject) {

                Rectangle rect = ((RectangleMapObject) object).getRectangle();
                String type = object.getProperties().get("type", String.class);
                BlockType blockType = BlockType.fromName(type);
                if(blockType == BlockType.DESTRUCTIBLE_WALL){
                    DestructibleWall wall = new DestructibleWall(rect, blockType);
                    return new DestructibleWall(rect, blockType);
                }
            }
        }
        return null;
    }

    public GateBlock getTheGate(){
        MapLayer layer = tiledMap.getLayers().get("collision objects");
        for (MapObject object : layer.getObjects()) {

            if (object instanceof RectangleMapObject) {

                Rectangle rect = ((RectangleMapObject) object).getRectangle();
                String type = object.getProperties().get("type", String.class);
                BlockType blockType = BlockType.fromName(type);
                if(blockType == BlockType.GATE){
                    return new GateBlock(rect, blockType);
                }
            }
        }
        return null;
    }

    public ArrayList<Vector2> getSafeSpots() {
        ArrayList<Vector2> safeSpots = new ArrayList<>();
        MapLayer layer = tiledMap.getLayers().get("collision objects");

        for (MapObject object : layer.getObjects()) {
            if (object instanceof PointMapObject) {
                if (!object.getProperties().containsKey("spawnPointOf")) {
                    float x = object.getProperties().get("x", Float.class);
                    float y = object.getProperties().get("y", Float.class);
                    Vector2 safeSpot = new Vector2(x, y);
                    safeSpots.add(safeSpot);
                }
            }
        }
        return safeSpots;
    }

    public Vector2 spawnPoint() {
        Vector2 point = new Vector2();
        MapLayer layer = tiledMap.getLayers().get("collision objects");
        MapObject object = layer.getObjects().get("spawnPoint");
        float spawnX = object.getProperties().get("x", Float.class);
        float spawnY = object.getProperties().get("y", Float.class);
        point.x = spawnX;
        point.y = spawnY;
        return point;
    }

    public ArrayList<EnemyModel> getEnemies() {
        ArrayList<EnemyModel> enemies = new ArrayList<>();
        MapLayer layer = tiledMap.getLayers().get("collision objects");
        for (MapObject object : layer.getObjects()) {
            if (object instanceof PointMapObject &&
                object.getProperties().containsKey("spawnPointOf")) {
                float x = object.getProperties().get("x", Float.class);
                float y = object.getProperties().get("y", Float.class);
                String enemyType = object.getProperties().get("spawnPointOf", String.class);
                switch (enemyType) {
                    case "Crawlid":
                        enemies.add(new Crawlid(new Vector2(x, y), new Rectangle(x, y, 90f, 60f), new Vector2(x, y)));
                        break;
                    case "HuskHornHead":
                        enemies.add(new HuskHornHead(new Vector2(x, y), new Rectangle(x, y, 70f, 120f), new Vector2(x, y), 30));
                        break;
                    case "Mossfly":
                        enemies.add(new Mossfly(new Vector2(x, y), new Rectangle(x, y, 90f, 90f), new Vector2(x, y)));
                        break;
                    case "CrystalGuardian":
                        enemies.add(new CrystalGuardian(new Vector2(x, y), new Rectangle(x, y, 60f, 110f), new Vector2(x, y), 60));
                        break;
                    case "FalseKnight":
                        enemies.add(new FalseKnight(new Vector2 (x,y) , new Rectangle(x,y , 320,350),  new Vector2(x, y)));
                        break;
                }
            }
        }
        return enemies;
    }

    public Zote getZote() {
        MapLayer layer = tiledMap.getLayers().get("collision objects");
        for (MapObject object : layer.getObjects()) {
            if (object instanceof PointMapObject &&
                object.getProperties().containsKey("spawnPointOf")) {
                float x = object.getProperties().get("x", Float.class);
                float y = object.getProperties().get("y", Float.class);
                String enemyType = object.getProperties().get("spawnPointOf", String.class);
                if (enemyType.equals("Zote")) {
                    return new Zote(new Vector2(x, y), new Rectangle(x, y, 120, 60f), new Vector2(x, y));
                }
            }
        }
        return null;

    }

}
