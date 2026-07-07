package com.Ap.HollowKnight.model.boss;

import com.Ap.HollowKnight.model.game.FacingDirection;
import com.Ap.HollowKnight.model.game.PhysicalPart;
import com.Ap.HollowKnight.model.map.Block;
import com.Ap.HollowKnight.model.map.BlockType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class ShockWave extends PhysicalPart {
    private static final float INITIAL_SPEED = 300f;
    private static final float ACCELERATION = 350f;
    private float currentSpeed;
    private boolean isActive = true;
    private boolean hasDealtDamage = false;

    public ShockWave(Vector2 position, Rectangle hitBox, Vector2 spawnPoint, FacingDirection direction) {
        super(position, hitBox, spawnPoint);
        this.setFacingDirection(direction);
        this.currentSpeed = INITIAL_SPEED;
        this.setGravityIncluded(true);
    }

    @Override
    public void update(float delta, ArrayList<Block> blocks) {
        if (!isActive) return;

        currentSpeed += ACCELERATION * delta;
        float dir = (getFacingDirection() == FacingDirection.RIGHT) ? 1f : -1f;
        this.getVelocity().x = dir * currentSpeed;

        applyPhysics(delta, blocks);

        if (isHittingWall(blocks)) {
            isActive = false;
        }
    }

    @Override
    public void takeDamage(int amount) {

    }

    @Override
    public void hazardReact() {

    }

    private boolean isHittingWall(ArrayList<Block> blocks) {
        float checkX = (getFacingDirection() == FacingDirection.RIGHT)
            ? getHitBox().x + getHitBox().width + 2f
            : getHitBox().x - 2f;
        float checkY = getHitBox().y + getHitBox().height / 2f;

        for (Block block : blocks) {
            if (block.getType() != BlockType.WALL) continue;
            if (block.getBound().contains(checkX, checkY)) {
                return true;
            }
        }
        return false;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        this.isActive = active;
    }

    public boolean hasDealtDamage() {
        return hasDealtDamage;
    }

    public void setHasDealtDamage(boolean hasDealtDamage) {
        this.hasDealtDamage = hasDealtDamage;
    }
}
