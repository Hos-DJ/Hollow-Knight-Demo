package com.Ap.HollowKnight.model.boss;

import com.Ap.HollowKnight.controller.events.GameEvent;
import com.Ap.HollowKnight.controller.events.GameEventMessenger;
import com.Ap.HollowKnight.model.game.FacingDirection;
import com.Ap.HollowKnight.model.map.Block;
import com.Ap.HollowKnight.model.Knight.Knight;

import java.util.ArrayList;

public class PowerMaceSlamState implements BossState {
    private float timer = 0f;
    private static final float DURATION = 1.5f;
    private static final float IMPACT_TIME = 0.3f;

    private static final float VERTICAL_VELOCITY = 600f;
    private static final float HORIZONTAL_VELOCITY = 400f;

    private boolean shockwaveSpawned = false;
    private boolean audioPlayed = false;
    private boolean jumped = false;
    private boolean landed = false;

    @Override
    public void enter(FalseKnight boss, Knight knight) {
        float dir = (knight.getPosition().x > boss.getPosition().x) ? 1f : -1f;
        boss.setFacingDirection(dir > 0 ? FacingDirection.RIGHT : FacingDirection.LEFT);

        boss.setOnGround(false);
        jumped = true;
        GameEventMessenger.getInstance().dispatch(GameEvent.BOSS_JUMP, null);

        boss.setCurrentPhase(FalseKnightPhase.JUMPING_ATTACK);
        boss.getVelocity().y = VERTICAL_VELOCITY;
        boss.getVelocity().x = HORIZONTAL_VELOCITY * dir;
    }

    @Override
    public void update(FalseKnight boss, Knight knight, ArrayList<Block> blocks, float delta) {
        boss.applyPhysics(delta, blocks);


        if (jumped && boss.isOnGround() && boss.getVelocity().y <= 0f) {
            jumped = false;
            landed = true;

            boss.getVelocity().x = 0;
            boss.setCurrentPhase(FalseKnightPhase.SLAMMING);
            GameEventMessenger.getInstance().dispatch(GameEvent.BOSS_LAND, null);

            timer = 0f;
        }

        if (landed) {
            timer += delta;

            if (timer >= IMPACT_TIME && !shockwaveSpawned) {
                boss.spawnShockwave();
                shockwaveSpawned = true;

                if (!audioPlayed) {
                    GameEventMessenger.getInstance().dispatch(GameEvent.POWER_SLAM_MACE, null);
                    audioPlayed = true;
                }
            }

            if (timer >= DURATION) {
                boss.changeState(new IdleState());
            }
        }
    }

    @Override
    public void exit(FalseKnight boss) {
        boss.getVelocity().x = 0;
        timer = 0;
        jumped = false;
        landed = false;
        shockwaveSpawned = false;
        audioPlayed = false;
    }
}
