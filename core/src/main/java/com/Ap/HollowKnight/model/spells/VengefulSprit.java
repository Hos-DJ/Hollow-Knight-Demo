package com.Ap.HollowKnight.model.spells;

import com.Ap.HollowKnight.model.game.FacingDirection;
import com.Ap.HollowKnight.model.map.Block;
import com.Ap.HollowKnight.model.map.BlockType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class VengefulSprit extends BaseSpell {
    private static final float SPEED = 600f;
    private static final float WIDTH = 40f;
    private static final float HEIGHT = 25f;
    private static final float DURATION = 3.0f;

    private float direction;

    public VengefulSprit() {
        this.position = new Vector2();
        this.hitBox = new Rectangle(0, 0, WIDTH, HEIGHT);
    }

    @Override
    public void cast(Rectangle knightHitBox, FacingDirection facingDirection) {
        float spawnX = knightHitBox.x + knightHitBox.width / 2f;
        float spawnY = knightHitBox.y + knightHitBox.height / 2f- HEIGHT/2f;
        this.active = true;
        this.durationTimer = DURATION;
        this.direction = (facingDirection == FacingDirection.RIGHT) ? 1.0f : -1.0f;
        this.position.set(spawnX, spawnY);
        this.hitBox.setPosition(position.x, position.y);
    }

    @Override
    public void update(float delta, ArrayList<Block> blocks) {
        if (!active) {
            return;
        }

        if (durationTimer > 0) {
            durationTimer -= delta;
            if (durationTimer <= 0) {
                deactivate();
            }
        }

        position.x += direction * SPEED * delta;
        hitBox.setPosition(position.x, position.y);//works like updateHitBox

        for (Block block : blocks) {
            if (!(block.getType() == BlockType.WALL || block.getType() == BlockType.SPIKE)) return;
            if (hitBox.overlaps(block.getBound())) {
                deactivate();
            }
        }

    }

    @Override
    public float getDurationTime() {
        return DURATION;
    }

    public float getDirection() {
        return direction;
    }
}
