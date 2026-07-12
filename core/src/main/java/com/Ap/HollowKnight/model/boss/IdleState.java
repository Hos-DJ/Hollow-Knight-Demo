package com.Ap.HollowKnight.model.boss;

import com.Ap.HollowKnight.model.map.Block;
import com.Ap.HollowKnight.model.Knight.Knight;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Random;

public class IdleState implements BossState{
    private float timer  = 0f;
    private static float IDLE_DURATION = 0.75f;
    private Random random = new Random();
    boolean knightIsInArea = false;

    @Override
    public void exit(FalseKnight boss) {

    }

    @Override
    public void update(FalseKnight boss, Knight knight, ArrayList<Block> blocks, float delta) {
        timer+= delta;
        if(timer >= IDLE_DURATION){
            nextMove(boss, knight);
        }
        boss.applyPhysics(delta,blocks);
    }

    @Override
    public void enter(FalseKnight boss, Knight knight) {
        boss.getVelocity().x = 0f;
        if(boss.isPhaseTwo()){
            IDLE_DURATION  = 0.375f;
        }
    }

    private void nextMove(FalseKnight boss, Knight knight){
        float distance =Math.abs(boss.getPosition().x - knight.getPosition().x);
        float fisa_distance = Vector2.dst(boss.getPosition().x,boss.getPosition().y,knight.getPosition().x, knight.getPosition().y);
        BossState nextState;
        if(distance <250f){
            knightIsInArea = true;
            nextState= (random.nextInt(100)>=30)? new MaceSlamState()
                : new DefensiveLeapState();
        }else if (fisa_distance>1700){
            nextState = new IdleState();
        }
        else{
            knightIsInArea = true;
            nextState =(random.nextInt(100)>=50)? new ChargeRunState() :
                (boss.isPhaseTwo())? new PowerMaceSlamState(): new OffensiveLeapState();
        }
        if(boss.getPreviousState()!=null&& boss.getPreviousState().equals(nextState.getClass())&&knightIsInArea){
            boss.setSpamCount(boss.getSpamCount()+1);
            if(boss.getSpamCount() >= 3 ){
                nextMove(boss, knight);
                return;
            }
        }else{
            boss.setSpamCount(0);
        }
        boss.changeState(nextState);
    }
}
