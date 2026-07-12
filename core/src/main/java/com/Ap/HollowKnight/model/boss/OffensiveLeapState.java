package com.Ap.HollowKnight.model.boss;

import com.Ap.HollowKnight.controller.events.GameEvent;
import com.Ap.HollowKnight.controller.events.GameEventMessenger;
import com.Ap.HollowKnight.model.game.FacingDirection;
import com.Ap.HollowKnight.model.map.Block;
import com.Ap.HollowKnight.model.Knight.Knight;

import java.util.ArrayList;

public class OffensiveLeapState implements BossState{
    private float timer=0;
    private static final float VERTICAL_VELOCITY = 600f;
    private static final float HORIZONTAL_VELOCITY = 400f;
    private final float LANDING_TIME = 0.3f;
    private boolean jumped = false;


    @Override
    public void enter(FalseKnight boss, Knight knight) {
        float dir = (knight.getPosition().x > boss.getPosition().x) ? 1f : -1f;
        boss.setFacingDirection(dir > 0 ? FacingDirection.RIGHT : FacingDirection.LEFT);

        boss.setOnGround(false);
        jumped = true;
        GameEventMessenger.getInstance().dispatch(GameEvent.BOSS_JUMP,null);

        boss.setCurrentPhase(FalseKnightPhase.JUMPING_ATTACK);
        boss.getVelocity().y = VERTICAL_VELOCITY;
        boss.getVelocity().x = HORIZONTAL_VELOCITY*dir;
    }

    @Override
    public void update(FalseKnight boss, Knight knight, ArrayList<Block> blocks, float delta) {
        boss.applyPhysics(delta, blocks);

        if(jumped&&boss.isOnGround()&&boss.getVelocity().y<=0f){
            boss.setCurrentPhase(FalseKnightPhase.LANDING);
            timer = LANDING_TIME;
            GameEventMessenger.getInstance().dispatch(GameEvent.BOSS_LAND,null);

            jumped = false;
        }
        if(timer >0){
            timer-= delta;
            if(timer <=0){
                boss.changeState(new IdleState());
                boss.setCurrentPhase(FalseKnightPhase.IDLE);
            }
        }
    }

    @Override
    public void exit(FalseKnight boss) {
        boss.getVelocity().x = 0;
        timer =0;
        jumped = false;
    }
}
