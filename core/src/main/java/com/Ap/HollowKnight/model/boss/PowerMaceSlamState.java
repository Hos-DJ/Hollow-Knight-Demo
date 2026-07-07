package com.Ap.HollowKnight.model.boss;

import com.Ap.HollowKnight.model.game.FacingDirection;
import com.Ap.HollowKnight.model.map.Block;
import com.Ap.HollowKnight.model.player.Knight;

import java.util.ArrayList;

public class PowerMaceSlamState implements BossState {
    private float timer = 0f;
    private static final float DURATION = 1.5f;
    private static final float IMPACT_TIME = 0.6f;
    private boolean shockwaveSpawned = false;

    @Override
    public void enter(FalseKnight boss, Knight knight) {
        boss.getVelocity().x = 0;

        FacingDirection dir = (knight.getPosition().x > boss.getPosition().x) ?
            FacingDirection.RIGHT : FacingDirection.LEFT;
        boss.setFacingDirection(dir);
    }

    @Override
    public void update(FalseKnight boss, Knight knight, ArrayList<Block> blocks,float delta) {
        timer += delta;

        // Spawn the shockwave at the exact moment of impact
        if (timer >= IMPACT_TIME && !shockwaveSpawned) {
            boss.spawnShockwave();
            // Optional: Trigger camera shake via the event listener here
            shockwaveSpawned = true;
        }

        if (timer >= DURATION) {
            boss.changeState(new IdleState());
        }
        boss.applyPhysics(delta, blocks);
    }

    @Override
    public void exit(FalseKnight boss) {}
}
