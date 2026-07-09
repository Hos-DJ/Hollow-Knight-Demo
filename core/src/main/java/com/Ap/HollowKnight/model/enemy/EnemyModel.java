package com.Ap.HollowKnight.model.enemy;

import com.Ap.HollowKnight.controller.events.GameEvent;
import com.Ap.HollowKnight.controller.events.GameEventMessenger;
import com.Ap.HollowKnight.model.game.FacingDirection;
import com.Ap.HollowKnight.model.game.PhysicalPart;
import com.Ap.HollowKnight.model.map.Block;
import com.Ap.HollowKnight.model.map.BlockType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public abstract class EnemyModel extends PhysicalPart {
    private int hp;
    private boolean isDead = false;
    private GameEventMessenger messenger=GameEventMessenger.getInstance();
    private EnemyState currentState = EnemyState.PATROLLING;
    private static final float CLIFF_PROBE_AHEAD = 5f;
    private static final float CLIFF_PROBE_DEPTH = 5f;
    private static final float WALL_PROBE_AHEAD = 6f;

    public EnemyModel(Vector2 position, Rectangle hitBox, Vector2 spawnPoint, int hp) {
        super(position, hitBox, spawnPoint);
        this.hp = hp;
    }

    public boolean isHeadingForCliff(ArrayList<Block> blocks) {
        if (!isOnGround()) return false;

        float checkX = (getFacingDirection() == FacingDirection.RIGHT)
            ? getHitBox().x + getHitBox().width + CLIFF_PROBE_AHEAD
            : getHitBox().x - CLIFF_PROBE_AHEAD;
        float checkY = getHitBox().y - CLIFF_PROBE_DEPTH;

        for (Block block : blocks) {
            if (block.getType() != BlockType.GROUND) continue;
            if (block.getBound().contains(checkX, checkY)) {
                return false;
            }
        }

        return true;
    }

    public boolean isHeadingForWall(ArrayList<Block> blocks) {
        float checkX = (getFacingDirection() == FacingDirection.RIGHT)
            ? getHitBox().x + getHitBox().width + WALL_PROBE_AHEAD
            : getHitBox().x - WALL_PROBE_AHEAD;


        float checkYLow = getHitBox().y;
        float checkYHigh = getHitBox().y + getHitBox().height - 4f;

        for (Block block : blocks) {
            if (block.getType() != BlockType.WALL && block.getType() != BlockType.SPIKE) continue;
            if (block.getBound().contains(checkX, checkYLow)
                || block.getBound().contains(checkX, checkYHigh)) {
                return true;
            }
        }
        return false;
    }


    @Override
    public void update(float delta, ArrayList<Block> blocks) {


        applyPhysics(delta, blocks);
    }

    @Override
    public void takeDamage(int amount) {
        this.hp = Math.max(this.hp - amount, 0);
        if (this.hp == 0) {
            this.die();

        }
        messenger.dispatch(GameEvent.ENEMY_HURT,null);

    }

    @Override
    public void hazardReact() {
        this.die();
        //set Animation to corpse
    }

    public int getHp() {
        return hp;
    }

    protected void die() {
        isDead = true;
        setGravityIncluded(true);
        messenger.dispatch(GameEvent.ENEMY_KILLED,this.getClass().getSimpleName());
    }

    public EnemyState getCurrentState() {
        return currentState;
    }

    public void setCurrentState(EnemyState currentState) {
        this.currentState = currentState;
    }

    public boolean isDead() {
        return isDead;
    }

    public void setDead(boolean dead) {
        isDead = dead;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }
}
