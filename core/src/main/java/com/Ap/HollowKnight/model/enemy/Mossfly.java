package com.Ap.HollowKnight.model.enemy;

import com.Ap.HollowKnight.model.level.LevelModel;
import com.Ap.HollowKnight.model.map.Block;
import com.Ap.HollowKnight.model.Knight.Knight;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class Mossfly extends EnemyModel {

    private static final float PATROL_RADIUS = 200.0f;
    private static final float SPEED_SCALE = 0.7f;
    private static final float OFFSET_Y = 30.0f;
    private static final float FOLLOW_DURATION = 0.2f;

    private Circle patrolCircle = new Circle();

    private float followTimer = 0;

    public Mossfly(Vector2 position, Rectangle hitBox, Vector2 spawnPoint) {
        super(position, hitBox, spawnPoint, 25);
        patrolCircle.set(spawnPoint.x, spawnPoint.y, PATROL_RADIUS);
        this.setCurrentState(EnemyState.IDLE);
        this.setGravityIncluded(false);
    }

    @Override
    public void update(float delta, ArrayList<Block> blocks) {
        Knight knight = LevelModel.getInstance().getKnight();
        updateCircle();
        if (this.patrolCircle.contains(knight.getPosition().x, knight.getPosition().y) && this.getCurrentState() != EnemyState.RUNNING) {
            patrol(knight);
        }

        if (this.followTimer > 0 && this.getCurrentState() == EnemyState.RUNNING) {
            followTimer -= delta;
            if (followTimer <= 0) {
                patrol(knight);
            }
        }
        super.update(delta, blocks);
    }

    private void updateCircle() {
        float x = this.getPosition().x + this.getHitBox().width / 2;
        float y = this.getPosition().y + this.getHitBox().height / 2;
        patrolCircle.setPosition(x, y);
    }

    public Circle getPatrolCircle() {
        updateCircle();
        return patrolCircle;
    }

    private void patrol(Knight knight) {
        this.setCurrentState(EnemyState.RUNNING);
        float x = (knight.getPosition().x - this.getPosition().x) * SPEED_SCALE;
        float y = (knight.getPosition().y - this.getPosition().y + OFFSET_Y) * SPEED_SCALE;
        Vector2 velocity = new Vector2(x, y);
        this.setVelocity(velocity);
        this.followTimer = FOLLOW_DURATION;
    }

    @Override
    protected void resetHp() {
        this.setHp(25);
    }
}
