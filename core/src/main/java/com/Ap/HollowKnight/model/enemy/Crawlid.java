package com.Ap.HollowKnight.model.enemy;

import com.Ap.HollowKnight.model.game.FacingDirection;
import com.Ap.HollowKnight.model.map.Block;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class Crawlid extends EnemyModel {
    private final float SPEED = 150.0f;

    public Crawlid(Vector2 position, Rectangle hitBox, Vector2 spawnPoint) {
        super(position, hitBox, spawnPoint, 30);
        this.setVelocity(new Vector2(SPEED, 0));
    }

    @Override
    public void update(float delta, ArrayList<Block> blocks) {
        if (this.isHeadingForCliff(blocks) || this.isHeadingForWall(blocks)) {
            turn();
        }
        super.update(delta, blocks);
    }

    @Override
    public void takeDamage(int amount) {
        super.takeDamage(amount);
    }

    private void turn() {
        if (getFacingDirection() == FacingDirection.RIGHT) {
            setFacingDirection(FacingDirection.LEFT);
            getVelocity().x = -SPEED;
        } else {
            setFacingDirection(FacingDirection.RIGHT);
            getVelocity().x = SPEED;
        }

    }

    @Override
    public void resetHp() {
        this.setHp(30);
    }
}
