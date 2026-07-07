package com.Ap.HollowKnight.model.spells;

import com.Ap.HollowKnight.model.game.FacingDirection;
import com.Ap.HollowKnight.model.map.Block;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class HowlingWrath extends BaseSpell {
    private static final float WIDTH = 80f;
    private static final float HEIGHT = 120f;
    private static final float DURATION = 1.5f;
    private static final int TOTAL_TICKS = 3;
    private static final float TICK_INTERVAL = DURATION / TOTAL_TICKS;

    private int tickCounter = 0;
    private float tickTimer =0f;

    public HowlingWrath() {
        this.position = new Vector2();
        this.hitBox   = new Rectangle(0, 0, WIDTH, HEIGHT);
    }

    @Override
    public void cast(Rectangle knightHitBox, FacingDirection facingDirection) {
        this.active     = true;
        this.durationTimer  = DURATION;
        this.tickCounter = 0;
        this.tickTimer  = 0f;
        Vector2 spawnPoint = new Vector2(knightHitBox.x + knightHitBox.width/2f-WIDTH/2f,
            knightHitBox.y + knightHitBox.height);
        this.position.set(spawnPoint.x, spawnPoint.y);
        this.hitBox.setPosition(position.x, position.y);
    }

    public boolean hasNewTick() {
        return tickCounter > 0 && tickTimer < TICK_INTERVAL * 0.1f;
    }

    private int pendingDamageTicks = 0;

    public void consumeTick() { pendingDamageTicks = Math.max(0, pendingDamageTicks - 1); }
    public boolean hasPendingTick() { return pendingDamageTicks > 0; }

    @Override
    public void update(float delta, ArrayList<Block> blocks) {
        if (!active) return;
        durationTimer -= delta;
        tickTimer += delta;
        if (durationTimer <= 0) { deactivate(); return; }
        if (tickTimer >= TICK_INTERVAL && tickCounter < TOTAL_TICKS) {
            tickTimer -= TICK_INTERVAL;
            tickCounter++;
            pendingDamageTicks++;
        }
    }

    @Override
    public float getDurationTime() {
        return DURATION;
    }
}
