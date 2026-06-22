package com.Ap.HollowKnight.controller;

import com.Ap.HollowKnight.model.AttackDirection;
import com.Ap.HollowKnight.model.enemy.EnemyModel;
import com.Ap.HollowKnight.model.player.Knight;
import com.Ap.HollowKnight.model.player.PlayerCondition;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CombatController {
    private static CombatController instance;
    private CombatController(){

    }
    public static CombatController getInstance(){
        if(instance== null){
            instance = new CombatController();
        }
        return instance;
    }
    private static final float DAMAGE_KNOCKBACK_SPEED= 400f;
    private static final int NAIL_DAMAGE =11;

    public void checkCombat(Knight knight, List<EnemyModel> enemies){
        Set<EnemyModel> hittedEnemies = new HashSet<>();
        if(enemies == null || enemies.isEmpty()){
            return;
        }
        for (EnemyModel enemy : enemies){
            if(!hittedEnemies.contains(enemy)&&knight.getNail().getHitBox().overlaps(enemy.getHitBox())&&!enemy.isDead()){
                enemy.takeDamage(NAIL_DAMAGE);
                hittedEnemies.add(enemy);
                knight.gainSoul();
                float knockBackDirection = (knight.getPosition().x < enemy.getPosition().x) ? 1f : -1f;
                float knockBackVerticalSpeed = 0f;
                knight.setPlayerCondition(PlayerCondition.IDLE);
                if(knight.getCurrentAttackDirection()== AttackDirection.DOWN){
                    knight.pogoBounce();
                    knockBackVerticalSpeed = 400.0f;
                }
                enemy.setKnockBackVelocity(new Vector2(knockBackDirection * DAMAGE_KNOCKBACK_SPEED, knockBackVerticalSpeed));
            }
        }
    }

    public void checkKnightDamage(Knight knight , ArrayList <EnemyModel> enemies){
        for(EnemyModel enemy : enemies){
            if(enemy.getHitBox().overlaps(knight.getHitBox())){
                knight.takeDamage(1);
                float knockBackDirection = (knight.getPosition().x > enemy.getPosition().x) ? 1f : -1f;
                knight.setKnockBackVelocity(new Vector2(knockBackDirection * DAMAGE_KNOCKBACK_SPEED, 400.0f));
            }
        }
    }
}
