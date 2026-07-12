package com.Ap.HollowKnight.model.boss;

import com.Ap.HollowKnight.model.map.Block;
import com.Ap.HollowKnight.model.Knight.Knight;

import java.util.ArrayList;

public class StunState implements BossState {
    private float timer = 0f;
    private static final float FALL_TIME = 0.8f;
    private static final float WAKE_TIME = 0.6f;
    private static final float STUN_DURATION = 4.0f;
    @Override
    public void enter(FalseKnight boss, Knight knight) {
        boss.getVelocity().x = 0;
    }

    @Override
    public void update(FalseKnight boss, Knight knight,  ArrayList<Block> blocks,float delta) {
        timer += delta;

        if (timer < FALL_TIME) {
            boss.setCurrentPhase(FalseKnightPhase.GETTING_STUNNED);
        } else if (timer < STUN_DURATION - WAKE_TIME) {
            boss.setCurrentPhase(FalseKnightPhase.STUNNED);
        } else {
            boss.setCurrentPhase(FalseKnightPhase.WAKING_UP);
        }

        if (timer >= STUN_DURATION) {
            boss.setPhaseTwo(true);
            boss.changeState(new IdleState());
        }
        boss.applyPhysics(delta, blocks);
    }

    @Override
    public void exit(FalseKnight boss) {
        boss.getVelocity().x = 0;
    }
}
