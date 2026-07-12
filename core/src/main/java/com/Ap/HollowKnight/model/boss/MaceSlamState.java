package com.Ap.HollowKnight.model.boss;

import com.Ap.HollowKnight.controller.events.GameEvent;
import com.Ap.HollowKnight.controller.events.GameEventMessenger;
import com.Ap.HollowKnight.model.game.FacingDirection;
import com.Ap.HollowKnight.model.map.Block;
import com.Ap.HollowKnight.model.Knight.Knight;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class MaceSlamState implements BossState {
    private float timer = 0f;
    private boolean hasDealtDamage = false;
    private static final float WINDUP_TIME = 0.6f;
    private static final float ACTIVE_TIME = 0.4f;
    private static final float RECOVERY_TIME = 0.4f;
    private static final float DURATION = WINDUP_TIME + ACTIVE_TIME + RECOVERY_TIME;

    @Override
    public void enter(FalseKnight boss, Knight knight) {
        boss.getVelocity().x = 0;

        FacingDirection dir = (knight.getPosition().x > boss.getPosition().x) ?
            FacingDirection.RIGHT : FacingDirection.LEFT;
        boss.setFacingDirection(dir);
    }

    @Override
    public void update(FalseKnight boss, Knight knight,  ArrayList<Block> blocks,float delta) {
        timer += delta;
        updateMaceHitBox(boss);
        if (timer < WINDUP_TIME) {
            boss.setCurrentPhase(FalseKnightPhase.WINDUP);
        } else if (timer < WINDUP_TIME + ACTIVE_TIME) {
            boss.setCurrentPhase(FalseKnightPhase.SLAMMING);
            GameEventMessenger.getInstance().dispatch(GameEvent.SLAM_MACE,null);
            checkDamage(boss, knight);
            updateMaceHitBox(boss);
        } else {
            boss.setCurrentPhase(FalseKnightPhase.RECOVERING);
        }

        if (timer >= DURATION) {
            boss.changeState(new IdleState());
        }
        boss.applyPhysics(delta, blocks);
    }

    private void updateMaceHitBox(FalseKnight boss) {
        float maceWidth = 180f;
        float maceHeight = 120f;

        float maceX = (boss.getFacingDirection() == FacingDirection.RIGHT)
            ? boss.getHitBox().x + boss.getHitBox().width
            : boss.getHitBox().x - maceWidth;

        boss.getMaceHitBox().set(maceX, boss.getHitBox().y, maceWidth, maceHeight);
    }

    private void checkDamage(FalseKnight boss, Knight knight) {
        if (!hasDealtDamage && boss.getMaceHitBox().overlaps(knight.getHitBox())) {

            if (!knight.isInvincible()) {
                float knockBackDir = (knight.getPosition().x > boss.getPosition().x) ? 1f : -1f;
                knight.setKnockBackVelocity(new Vector2(knockBackDir * 600f, 400f));
            }

            knight.takeDamage(1);

            hasDealtDamage = true;
        }
    }

    @Override
    public void exit(FalseKnight boss) {
        hasDealtDamage=false;
    }
}
