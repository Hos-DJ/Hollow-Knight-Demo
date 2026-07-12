package com.Ap.HollowKnight.model.level;

import com.Ap.HollowKnight.model.boss.FalseKnight;
import com.Ap.HollowKnight.model.enemy.EnemyModel;
import com.Ap.HollowKnight.model.map.Block;
import com.Ap.HollowKnight.model.map.DestructibleWall;
import com.Ap.HollowKnight.model.map.GateBlock;
import com.Ap.HollowKnight.model.charms.CollectibleCharm;
import com.Ap.HollowKnight.model.Knight.Knight;
import com.Ap.HollowKnight.model.zote.Zote;
import com.Ap.HollowKnight.view.PlayerHUD;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class LevelModel {
    private static LevelModel instance;

    private ArrayList<Block> blocks;
    private DestructibleWall destructibleWall;
    private ArrayList<Vector2> safeSpots;
    private Vector2 spawnPoint;
    private ArrayList<EnemyModel> enemies;
    private Knight knight;
    private FalseKnight falseKnight;
    private PlayerHUD hud;
    private Zote zote;
    private float mapWidth;
    private float mapHeight;
    private GateBlock gateBlock;
    private ArrayList<CollectibleCharm> collectibleCharms;
    private Rectangle cameraBound;
    private LevelModel() {
    }

    public static LevelModel getInstance() {
        if (instance == null) {
            throw new IllegalStateException("LevelModel must be initialized before use.");
        }
        return instance;
    }

    public static LevelModel getInstance(ArrayList<Block> blocks, ArrayList<EnemyModel> enemies, float mapWidth,
                                         float mapHeight, ArrayList<Vector2> safeSpots, Vector2 spawnPoint,
                                         Zote zote, DestructibleWall wall, GateBlock gateBlock,
                                         ArrayList<CollectibleCharm> charms ,Rectangle cameraBound) {
        if (instance == null) {
            instance = new LevelModel();
        }
        instance.initialize(blocks, enemies, mapWidth, mapHeight, safeSpots, spawnPoint, zote,
            wall, gateBlock, charms,cameraBound);
        return instance;
    }

    private void initialize(ArrayList<Block> blocks, ArrayList<EnemyModel> enemies, float mapWidth,
                            float mapHeight, ArrayList<Vector2> safeSpots, Vector2 spawnPoint,
                            Zote zote, DestructibleWall wall, GateBlock gateBlock, ArrayList<CollectibleCharm> charms,
                            Rectangle cameraBound) {
        this.blocks = blocks;
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        this.safeSpots = safeSpots;
        this.spawnPoint = spawnPoint;
        this.zote = zote;
        this.hud = new PlayerHUD();
        this.knight = new Knight(new Vector2(spawnPoint),
            new Rectangle(0, 0, 35, 75),
            new Vector2(spawnPoint), this.safeSpots, hud);
        this.enemies = enemies;
        this.destructibleWall = wall;
        this.blocks.add(wall);
        this.gateBlock = gateBlock;
        this.collectibleCharms = charms;
        this.cameraBound = cameraBound;
    }


    public ArrayList<Block> getBlocks() {
        return blocks;
    }

    public void setBlocks(ArrayList<Block> blocks) {
        this.blocks = blocks;
    }

    public float getMapWidth() {
        return mapWidth;
    }

    public void setMapWidth(float mapWidth) {
        this.mapWidth = mapWidth;
    }

    public float getMapHeight() {
        return mapHeight;
    }

    public void setMapHeight(float mapHeight) {
        this.mapHeight = mapHeight;
    }

    public ArrayList<Vector2> getSafeSpots() {
        return safeSpots;
    }

    public void setSafeSpots(ArrayList<Vector2> safeSpots) {
        this.safeSpots = safeSpots;
    }

    public Vector2 getSpawnPoint() {
        return spawnPoint;
    }

    public void setSpawnPoint(Vector2 spawnPoint) {
        this.spawnPoint = spawnPoint;
    }

    public Knight getKnight() {
        return knight;
    }

    public void setKnight(Knight knight) {
        this.knight = knight;
    }

    public ArrayList<EnemyModel> getEnemies() {
        return enemies;
    }

    public void setEnemies(ArrayList<EnemyModel> enemies) {
        this.enemies = enemies;
    }

    public Zote getZote() {
        return zote;
    }

    public void setZote(Zote zote) {
        this.zote = zote;
    }

    public PlayerHUD getHud() {
        return hud;
    }

    public DestructibleWall getDestructibleWall() {
        return destructibleWall;
    }

    public void setDestructibleWall(DestructibleWall destructibleWall) {
        this.destructibleWall = destructibleWall;
    }

    public GateBlock getGateBlock() {
        return gateBlock;
    }

    public void setGateBlock(GateBlock gateBlock) {
        this.gateBlock = gateBlock;
    }

    public ArrayList<CollectibleCharm> getCollectibleCharms() {
        return collectibleCharms;
    }

    public Rectangle getCameraBound() {
        return cameraBound;
    }

    public void setCameraBound(Rectangle cameraBound) {
        this.cameraBound = cameraBound;
    }
}
