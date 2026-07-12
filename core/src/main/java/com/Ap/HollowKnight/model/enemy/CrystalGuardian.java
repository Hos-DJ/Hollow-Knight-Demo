package com.Ap.HollowKnight.model.enemy;

import com.Ap.HollowKnight.model.game.FacingDirection;
import com.Ap.HollowKnight.model.level.LevelModel;
import com.Ap.HollowKnight.model.map.Block;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class CrystalGuardian extends EnemyModel {

    private final Rectangle fov = new Rectangle();
    private final Rectangle laser = new Rectangle();

    private static final float LASER_WIDTH = 1200f;
    private static final float LASER_HEIGHT = 40f;
    private static final float FOV_WIDTH = 300f;
    private static final float FOV_HEIGHT = 80f;
    private static final float ENRAGED_SPEED = 400f;
    private static final float CHARGE_WIND_UP = 2.0f;
    private static final float LASER_DURATION = 2.25f;
    private static final float ENRAGE_DURATION = 4.0f;
    private static final float LASER_OFFSET_Y = 45f;
    private static final float TURN_DELAY_DURATION = 0.3f;

    private float chargeTimer = 0f;
    private float laserTimer = 0f;
    private float enragedTimer = 0f;
    private float turnDelayTimer = 0f;
    private float lastKnownKnightX;

    public CrystalGuardian(Vector2 position, Rectangle hitBox, Vector2 spawnPoint, int hp) {
        super(position, hitBox, spawnPoint, hp);
        setFacingDirection(FacingDirection.LEFT);
        setGravityIncluded(true);
        setCurrentState(EnemyState.IDLE);
    }

    @Override
    public void update(float delta, ArrayList<Block> blocks) {
        updateFov();
        updateLaser();
        lastKnownKnightX = LevelModel.getInstance().getKnight().getPosition().x;

        if (chargeTimer > 0) {
            chargeTimer -= delta;
            if (chargeTimer <= 0) {
                fireLaser();
            }
        }

        if (laserTimer > 0) {
            laserTimer -= delta;
            setAttacking(laserTimer > 0);
            if (laserTimer <= 0) {
                enrage();
            }
        }

        if (enragedTimer > 0) {
            enragedTimer -= delta;

            if (isHeadingForWall(blocks) || isHeadingForCliff(blocks)) {
                getVelocity().x *= -1;
                setFacingDirection(getFacingDirection() == FacingDirection.LEFT ? FacingDirection.RIGHT : FacingDirection.LEFT);
                turnDelayTimer = 0f;
            } else {
                boolean shouldFaceRight = lastKnownKnightX > getPosition().x;
                FacingDirection targetDirection = shouldFaceRight ? FacingDirection.RIGHT : FacingDirection.LEFT;

                if (getFacingDirection() != targetDirection) {
                    if (turnDelayTimer <= 0) {
                        turnDelayTimer = TURN_DELAY_DURATION;
                    }
                    turnDelayTimer -= delta;

                    if (turnDelayTimer <= 0) {
                        setFacingDirection(targetDirection);
                        getVelocity().x = shouldFaceRight ? ENRAGED_SPEED : -ENRAGED_SPEED;
                    }
                } else {
                    turnDelayTimer = 0f;
                    getVelocity().x = shouldFaceRight ? ENRAGED_SPEED : -ENRAGED_SPEED;
                }
            }

            if (enragedTimer <= 0) {
                returnToIdle();
            }
        }
        super.update(delta, blocks);
    }

    public void onPlayerSpotted(Vector2 knightPosition) {
        if (getCurrentState() != EnemyState.IDLE) return;
        lastKnownKnightX = knightPosition.x;
        setCurrentState(EnemyState.CHARGING);
        chargeTimer = CHARGE_WIND_UP;
        getVelocity().x = 0f;
    }

    private void fireLaser() {
        setCurrentState(EnemyState.SHOOTING);
        setAttacking(true);
        laserTimer = LASER_DURATION;
    }

    private void enrage() {
        setAttacking(false);
        boolean shouldFaceRight = lastKnownKnightX > getPosition().x;
        setFacingDirection(shouldFaceRight ? FacingDirection.RIGHT : FacingDirection.LEFT);
        getVelocity().x = shouldFaceRight ? ENRAGED_SPEED : -ENRAGED_SPEED;

        enragedTimer = ENRAGE_DURATION;
        setCurrentState(EnemyState.RUNNING);
    }

    private void returnToIdle() {
        getVelocity().x = 0f;
        turnDelayTimer = 0f;
        setCurrentState(EnemyState.IDLE);
    }

    private void updateFov() {
        float fovX = (getFacingDirection() == FacingDirection.RIGHT)
            ? getHitBox().x + getHitBox().width
            : getHitBox().x - FOV_WIDTH;
        fov.set(fovX, getHitBox().y, FOV_WIDTH, FOV_HEIGHT);
    }

    private void updateLaser() {
        float x = (getFacingDirection() == FacingDirection.RIGHT)
            ? getHitBox().x + getHitBox().width
            : getHitBox().x - LASER_WIDTH;
        float y = getHitBox().y + LASER_OFFSET_Y;
        laser.set(x, y, LASER_WIDTH, LASER_HEIGHT);
    }

    @Override
    public void resetHp() {
        this.setHp(60);
    }

    public Rectangle getFov() {
        return fov;
    }

    public Rectangle getLaser() {
        return laser;
    }
}
