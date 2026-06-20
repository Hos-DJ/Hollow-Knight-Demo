package com.Ap.HollowKnight.model;

import com.Ap.HollowKnight.model.game.PhysicalPart;
import com.Ap.HollowKnight.model.map.Block;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class Nail extends PhysicalPart {
    public Nail(Vector2 position,Rectangle hitBox,Vector2 spawnPoint) {
        super(position,0,hitBox , spawnPoint);
    }

    @Override
    public void update(float delta, MapLayer layer ,ArrayList<Block> blocks) {

    }

    @Override
    public void takeDamage(int amount) {
        // no damage for nail
    }


}
