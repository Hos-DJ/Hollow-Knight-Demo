package com.Ap.HollowKnight.model.boss;

import com.Ap.HollowKnight.model.game.FacingDirection;
import com.Ap.HollowKnight.model.map.Block;
import com.Ap.HollowKnight.model.player.Knight;

import java.util.ArrayList;

public class ChargeRunState implements BossState {
    private float direction;
    private static final float BASE_SPEED = 400f;
    private float timer = 0f;
    private static final float MAX_DURATION = 2.5f;

    @Override
    public void enter(FalseKnight boss, Knight knight) {
        direction = (knight.getPosition().x > boss.getPosition().x) ? 1f : -1f;
        boss.setFacingDirection(direction > 0 ? FacingDirection.RIGHT : FacingDirection.LEFT);
        boss.setCurrentPhase(FalseKnightPhase.RUNNING);
        float speed = boss.isPhaseTwo() ? BASE_SPEED * 1.35f : BASE_SPEED;
        boss.getVelocity().x = direction * speed;
    }

    @Override
    public void update(FalseKnight boss, Knight knight, ArrayList<Block> blocks,float delta) {
        timer += delta;

        if (boss.isHeadingForWall(blocks) || boss.isHeadingForCliff(blocks) || timer > MAX_DURATION) {
            boss.changeState(new IdleState());
            return;
        }
        boss.applyPhysics(delta, blocks);
    }

    @Override
    public void exit(FalseKnight boss) {
        boss.getVelocity().x = 0;
    }
}
