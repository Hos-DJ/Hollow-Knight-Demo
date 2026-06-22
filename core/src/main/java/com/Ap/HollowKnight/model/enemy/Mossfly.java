package com.Ap.HollowKnight.model.enemy;

import com.Ap.HollowKnight.model.level.LevelModel;
import com.Ap.HollowKnight.model.map.Block;
import com.Ap.HollowKnight.model.player.Knight;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class Mossfly extends EnemyModel{

    private static final float PATROL_RADIUS = 100.0f;
    private static final float SPEED_SCALE = 0.7f;
    private static final float OFFSET_Y = 30.0f;
    private Circle patrolCircle = new Circle();
    public Mossfly(Vector2 position, Rectangle hitBox, Vector2 spawnPoint) {
        super(position, hitBox, spawnPoint, 25);
        patrolCircle.set(spawnPoint.x, spawnPoint.y, PATROL_RADIUS);
        this.setCurrentState(EnemyState.IDLE);
        this.setGravityIncluded(false);
    }

    @Override
    public void update(float delta, ArrayList<Block> blocks) {
        Knight knight = LevelModel.getInstance().getKnight();
        updateCircle();
        if(this.patrolCircle.contains(knight.getPosition().x, knight.getPosition().y)&&this.getCurrentState()!= EnemyState.RUNNING){
            patrol(knight);
        }
        super.update(delta, blocks);
    }

    private void updateCircle(){
        patrolCircle.setPosition(this.getPosition().x, this.getPosition().y);
    }

    private void patrol(Knight knight){
        this.setCurrentState(EnemyState.RUNNING);
        float x =  (knight.getPosition().x - this.getPosition().x) * SPEED_SCALE;
        float y = (knight.getPosition().y - this.getPosition().y + OFFSET_Y) *  SPEED_SCALE;
        Vector2 velocity = new Vector2(x,y);
        this.setVelocity(velocity);
    }
}
