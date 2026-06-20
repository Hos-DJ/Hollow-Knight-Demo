package com.Ap.HollowKnight.controller;

import com.Ap.HollowKnight.model.map.Block;
import com.Ap.HollowKnight.model.player.Knight;
import com.badlogic.gdx.maps.tiled.TiledMap;

import java.util.ArrayList;

public class GameFlowController {
    private Knight knight;
    private TiledMap map;
    private ArrayList<Block> blocks ;

    public GameFlowController(Knight knight, TiledMap map, ArrayList<Block> blocks) {
        this.knight = knight;
        this.map = map;
        this.blocks = blocks;
    }

    public void update(float delta){
        knight.update(delta,map.getLayers().get("collision objects"),blocks);
    }
}
