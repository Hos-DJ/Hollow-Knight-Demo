package com.Ap.HollowKnight.model.enemy;

import com.Ap.HollowKnight.model.game.FacingDirection;
import com.Ap.HollowKnight.model.level.LevelModel;
import com.Ap.HollowKnight.model.map.Block;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class HuskHornHead extends EnemyModel {
    private static final float PATROL_SPEED = 100.0f;
    private static final float RUNNING_SPEED = 220.0f;
    private static final float WALK_DURATION = 2.5f;
    private static final float REST_DURATION = 6.0f;
    private static final float TURN_DURATION = 1.0f;
    private static final float FOV_WIDTH = 400f;
    private static final float FOV_VERTICAL_PAD = 16f;

    private float turningTimer = 0f;
    private float restingTimer = 0f;
    private float walkingTimer = WALK_DURATION;
    private final Rectangle fov = new Rectangle();

    public HuskHornHead(Vector2 position, Rectangle hitBox, Vector2 spawnPoint, int hp) {
        super(position, hitBox, spawnPoint, hp);
        this.getVelocity().x = PATROL_SPEED;
    }

    @Override
    public void update(float delta, ArrayList<Block> blocks) {
        updateFOV();
        Rectangle knightHitBox = LevelModel.getInstance().getKnight().getHitBox();
        if (fov.overlaps(knightHitBox)) {
            charge();
        }

        switch (this.getCurrentState()) {
            case IDLE -> {
                restingTimer -= delta;
                if (restingTimer <= 0) {
                    this.setCurrentState(EnemyState.PATROLLING);
                    this.getVelocity().x = getPatrolVelocity();
                    walkingTimer = WALK_DURATION;
                }
            }
            case PATROLLING -> {
                walkingTimer -= delta;
                if (walkingTimer <= 0) {
                    this.getVelocity().x = 0;
                    this.setCurrentState(EnemyState.IDLE);
                    restingTimer = REST_DURATION;
                }
            }
            case TURNING -> {
                turningTimer -= delta;
                if (turningTimer <= 0) {
                    this.setCurrentState(EnemyState.PATROLLING);
                }
            }
        }

        if (isHeadingForWall(blocks) || isHeadingForCliff(blocks)) {
            turn();
        }

        super.update(delta, blocks);
    }

    private void turn() {
        if (getFacingDirection() == FacingDirection.RIGHT) {
            setFacingDirection(FacingDirection.LEFT);
        } else {
            setFacingDirection(FacingDirection.RIGHT);
        }

        getVelocity().x = -getPatrolVelocity();
        this.setCurrentState(EnemyState.TURNING);
        turningTimer = TURN_DURATION;
    }

    private void updateFOV() {
        float fovX = (getFacingDirection() == FacingDirection.RIGHT)
            ? getHitBox().x + getHitBox().width
            : getHitBox().x - FOV_WIDTH;
        float fovY = getHitBox().y - FOV_VERTICAL_PAD;
        float fovHeight = getHitBox().height + FOV_VERTICAL_PAD * 2f;
        fov.set(fovX, fovY, FOV_WIDTH, fovHeight);
    }

    public Rectangle getFov() {
        updateFOV();
        return fov;
    }

    private void charge() {
        if (this.getCurrentState() == EnemyState.RUNNING) return;

        this.setCurrentState(EnemyState.RUNNING);
        this.getVelocity().x = getFacingDirection() == FacingDirection.RIGHT ? RUNNING_SPEED : -RUNNING_SPEED;
    }

    private float getPatrolVelocity() {
        return getFacingDirection() == FacingDirection.RIGHT ? PATROL_SPEED : -PATROL_SPEED;
    }

    @Override
    public void takeDamage(int amount) {
        super.takeDamage(amount);
    }

    @Override
    protected void resetHp() {
        this.setHp(30);
    }
}
