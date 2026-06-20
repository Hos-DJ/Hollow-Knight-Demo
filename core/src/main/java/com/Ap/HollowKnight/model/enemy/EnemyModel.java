package com.Ap.HollowKnight.model.enemy;

import com.Ap.HollowKnight.model.game.FacingDirection;
import com.Ap.HollowKnight.model.game.PhysicalPart;
import com.Ap.HollowKnight.model.level.LevelModel;
import com.Ap.HollowKnight.model.map.Block;
import com.Ap.HollowKnight.model.map.BlockType;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public abstract class EnemyModel extends PhysicalPart {
    private int hp;
    private static final float CLIFF_PROBE_AHEAD = 5f;
    private static final float CLIFF_PROBE_DEPTH = 5f;
    public EnemyModel(Vector2 position, float maxVelocity, Rectangle hitBox, Vector2 spawnPoint, int hp) {
        super(position, maxVelocity, hitBox, spawnPoint);
        this.hp = hp;
    }

    public boolean isHeadingForCliff(ArrayList<Block> blocks) {
        if(!isOnGround()){
            return false;
        }
        float checkX = (getFacingDirection()==FacingDirection.RIGHT)? this.getHitBox().x+ this.getHitBox().width + CLIFF_PROBE_AHEAD
            :this.getPosition().x - CLIFF_PROBE_AHEAD;
        float checkY = this.getHitBox().y -  CLIFF_PROBE_DEPTH;
        for(Block block : blocks) {
            if(block.getType()!= BlockType.GROUND) continue;
            if(block.getBound().contains(checkX, checkY)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void update(float delta, MapLayer layer,ArrayList<Block> blocks) {



        applyPhysics(delta,blocks);
    }

    @Override
    public void takeDamage(int amount) {
        this.hp = Math.max(this.hp - amount, 0);
        if (this.hp == 0) {
            this.die();
        }
    }

    public int getHp() {
        return hp;
    }

    protected void die() {

    }
}
