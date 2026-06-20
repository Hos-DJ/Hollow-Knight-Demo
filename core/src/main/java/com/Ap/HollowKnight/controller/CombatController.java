package com.Ap.HollowKnight.controller;

import com.Ap.HollowKnight.model.AttackDirection;
import com.Ap.HollowKnight.model.enemy.EnemyModel;
import com.Ap.HollowKnight.model.player.Knight;
import com.Ap.HollowKnight.model.player.PlayerCondition;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CombatController {
    private Set<EnemyModel> hittedEnemies = new HashSet<>();
    private final int NAIL_DAMAGE =11;
    public void checkCombat(Knight knight, List<EnemyModel> enemies){
        if(enemies == null || enemies.isEmpty()){
            return;
        }
        for (EnemyModel enemy : enemies){
            if(!hittedEnemies.contains(enemy)&&knight.getNail().getHitBox().overlaps(enemy.getHitBox())){
                enemy.takeDamage(NAIL_DAMAGE);
                hittedEnemies.add(enemy);
                knight.gainSoul();
                knight.setPlayerCondition(PlayerCondition.IDLE);
                if(knight.getCurrentAttackDirection()== AttackDirection.DOWN){
                    knight.pogoBounce();
                }
            }
        }
    }
}
