package com.Ap.HollowKnight.model.boss;

import com.Ap.HollowKnight.controller.events.GameEvent;
import com.Ap.HollowKnight.controller.events.GameEventMessenger;
import com.Ap.HollowKnight.model.game.FacingDirection;
import com.Ap.HollowKnight.model.map.Block;
import com.Ap.HollowKnight.model.player.Knight;

import java.util.ArrayList;

public class DefensiveLeapState implements BossState {
    private static final float HORIZONTAL_VELOCITY = 350.0f;
    private static final float VERTICAL_VELOCITY = 500.0f;
    private final float LANDING_TIME = 0.3f;
    private boolean jumped = false;
    private float timer = 0f;

    @Override
    public void enter(FalseKnight boss, Knight knight) {
        float direction = (knight.getPosition().x > boss.getPosition().x) ? -1f : 1f;
        boss.setFacingDirection(direction > 0 ? FacingDirection.LEFT : FacingDirection.RIGHT);

        boss.setOnGround(false);
        jumped = true;
        GameEventMessenger.getInstance().dispatch(GameEvent.BOSS_JUMP,null);

        boss.setCurrentPhase(FalseKnightPhase.JUMPING_DEFENSE);
        boss.getVelocity().x =HORIZONTAL_VELOCITY*direction;
        boss.getVelocity().y = VERTICAL_VELOCITY;
    }

    @Override
    public void update(FalseKnight boss, Knight knight, ArrayList<Block> blocks, float delta) {
        boss.applyPhysics(delta, blocks);
        if (jumped && boss.isOnGround() && boss.getVelocity().y <= 0) {
            boss.setCurrentPhase(FalseKnightPhase.LANDING);
            GameEventMessenger.getInstance().dispatch(GameEvent.BOSS_LAND,null);

            timer= LANDING_TIME;
            jumped = false;
        }
        if(timer >0){
            timer-=delta;
            if(timer <=0){
                boss.changeState(new IdleState());
                boss.setCurrentPhase(FalseKnightPhase.IDLE);
            }
        }

    }

    @Override
    public void exit(FalseKnight boss) {
        boss.getVelocity().x = 0;
        timer = 0;
        jumped = false;

    }
}
