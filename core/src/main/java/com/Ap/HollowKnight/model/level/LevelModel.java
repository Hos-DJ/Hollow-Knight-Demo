package com.Ap.HollowKnight.model.level;

import com.Ap.HollowKnight.model.map.Block;
import com.Ap.HollowKnight.model.map.TiledMapHelper;

import java.util.ArrayList;

public class LevelModel {
    private ArrayList<Block> blocks;
    private float mapWidth;
    private float mapHeight;

    public LevelModel(ArrayList<Block> blocks, float mapWidth, float mapHeight) {
        this.blocks = blocks;
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
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
}
