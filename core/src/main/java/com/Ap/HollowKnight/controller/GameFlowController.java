package com.Ap.HollowKnight.controller;

import com.Ap.HollowKnight.model.enemy.EnemyModel;
import com.Ap.HollowKnight.model.map.Block;
import com.Ap.HollowKnight.model.player.Knight;
import com.badlogic.gdx.maps.tiled.TiledMap;

import java.util.ArrayList;

public class GameFlowController {
    private Knight knight;
    private TiledMap map;
    private ArrayList<Block> blocks;
    private ArrayList<EnemyModel> enemies;
    private GameProcessor gameProcessor;

    public GameFlowController(Knight knight, TiledMap map, ArrayList<Block> blocks, GameProcessor gameProcessor,
                              ArrayList<EnemyModel> enemies) {
        this.knight = knight;
        this.map = map;
        this.blocks = blocks;
        this.gameProcessor = gameProcessor;
        this.enemies = enemies;
    }

    public void update(float delta) {
        gameProcessor.pollMovement();
        knight.update(delta, blocks);
        for (EnemyModel enemy : enemies) {
            if (!enemy.isDead()) {
                enemy.update(delta, blocks);
            }
        }
    }
}
