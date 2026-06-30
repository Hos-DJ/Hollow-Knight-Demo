package com.Ap.HollowKnight.model.level;

import com.Ap.HollowKnight.model.enemy.EnemyModel;
import com.Ap.HollowKnight.model.map.Block;
import com.Ap.HollowKnight.model.player.Knight;
import com.Ap.HollowKnight.model.zote.Zote;
import com.Ap.HollowKnight.view.PlayerHUD;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class LevelModel {
    private static LevelModel instance;

    private ArrayList<Block> blocks;
    private ArrayList<Vector2> safeSpots;
    private Vector2 spawnPoint;
    private ArrayList<EnemyModel> enemies;
    private Knight knight;
    private PlayerHUD hud;
    private Zote zote;
    private float mapWidth;
    private float mapHeight;

    private LevelModel() {
    }

    public static LevelModel getInstance() {
        if (instance == null) {
            throw new IllegalStateException("LevelModel must be initialized before use.");
        }
        return instance;
    }

    public static LevelModel getInstance(ArrayList<Block> blocks, ArrayList<EnemyModel> enemies, float mapWidth, float mapHeight, ArrayList<Vector2> safeSpots, Vector2 spawnPoint,Zote zote) {
        if (instance == null) {
            instance = new LevelModel();
        }
        instance.initialize(blocks, enemies, mapWidth, mapHeight, safeSpots, spawnPoint,zote);
        return instance;
    }

    private void initialize(ArrayList<Block> blocks, ArrayList<EnemyModel> enemies, float mapWidth, float mapHeight, ArrayList<Vector2> safeSpots, Vector2 spawnPoint , Zote zote) {
        this.blocks = blocks;
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        this.safeSpots = safeSpots;
        this.spawnPoint = spawnPoint;
        this.zote = zote;
        this.hud = new PlayerHUD();
        this.knight = new Knight(spawnPoint, new Rectangle(0, 0, 32, 64), spawnPoint, this.safeSpots,hud);
        this.enemies = enemies;
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
}
