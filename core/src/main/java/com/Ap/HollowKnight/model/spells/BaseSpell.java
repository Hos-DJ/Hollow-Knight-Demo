package com.Ap.HollowKnight.model.spells;

import com.Ap.HollowKnight.model.game.FacingDirection;
import com.Ap.HollowKnight.model.map.Block;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public abstract class BaseSpell {
    protected Vector2 position;
    protected Rectangle hitBox;
    protected boolean active = false;
    protected float durationTimer = 0f;

    public abstract void cast(Rectangle knightHitBox, FacingDirection facingDirection);

    public abstract void update(float delta, ArrayList<Block> blocks);

    public abstract float getDurationTime();

    public Vector2 getPosition() {
        return position;
    }

    public Rectangle getHitBox() {
        return hitBox;
    }

    public boolean isActive() {
        return active;
    }

    public void deactivate() {
        active = false;
        durationTimer = 0f;
    }
}
