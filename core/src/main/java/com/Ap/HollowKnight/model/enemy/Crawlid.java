package com.Ap.HollowKnight.model.enemy;

import com.Ap.HollowKnight.model.game.FacingDirection;
import com.Ap.HollowKnight.model.map.Block;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class Crawlid extends EnemyModel{
    public Crawlid(Vector2 position, float maxVelocity, Rectangle hitBox, Vector2 spawnPoint) {
        super(position, maxVelocity, hitBox, spawnPoint, 30);
        this.setVelocity(new Vector2(150,0));
    }

    @Override
    public void update(float delta, MapLayer layer,ArrayList<Block> blocks) {
        if(this.isHeadingForCliff(blocks)){
            float direction = (this.getFacingDirection()== FacingDirection.RIGHT) ? -1f : 1f;
            this.getVelocity().x *= direction;
        }
    }

    @Override
    public void takeDamage(int amount) {
        super.takeDamage(amount);
    }
}
