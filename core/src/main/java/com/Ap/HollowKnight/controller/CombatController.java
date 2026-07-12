package com.Ap.HollowKnight.controller;

import com.Ap.HollowKnight.model.AttackDirection;
import com.Ap.HollowKnight.model.boss.FalseKnight;
import com.Ap.HollowKnight.model.enemy.EnemyModel;
import com.Ap.HollowKnight.model.map.DestructibleWall;
import com.Ap.HollowKnight.model.Knight.Knight;
import com.Ap.HollowKnight.model.Knight.PlayerCondition;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.Ap.HollowKnight.model.Knight.PlayerCondition.DASHING;

public class CombatController {
    private static CombatController instance;
    private Set<EnemyModel> dashedEnemies;

    private CombatController() {
        dashedEnemies = new HashSet<>();
    }

    public static CombatController getInstance() {
        if (instance == null) {
            instance = new CombatController();
        }
        return instance;
    }


    public void checkCombat(Knight knight, List<EnemyModel> enemies, DestructibleWall wall) {
        float damageKnockBack = knight.getCurrentKnockBack();
        int currentNailDamage = knight.getCurrentNailDamage();
        Set<EnemyModel> hittedEnemies = new HashSet<>();
        boolean isWallDamaged = false;
        if (enemies == null || enemies.isEmpty()) {
            return;
        }
        for (EnemyModel enemy : enemies) {
            if (!enemy.isDead() && !hittedEnemies.contains(enemy) && knight.getNail().getHitBox().overlaps(enemy.getHitBox())) {
                enemy.takeDamage(currentNailDamage);
                hittedEnemies.add(enemy);
                knight.gainSoul();
                float knockBackDirection = (knight.getPosition().x < enemy.getPosition().x) ? 1f : -1f;
                float knockBackVerticalSpeed = 600.0f;
                knight.setPlayerCondition(PlayerCondition.IDLE);
                if (knight.getCurrentAttackDirection() == AttackDirection.DOWN) {
                    knight.pogoBounce();
                    knockBackVerticalSpeed = 0f;
                }
                if (enemy instanceof FalseKnight) {
                    enemy.setKnockBackVelocity(new Vector2(knockBackDirection * damageKnockBack, 0));
                } else
                    enemy.setKnockBackVelocity(new Vector2(knockBackDirection * damageKnockBack, knockBackVerticalSpeed));
            }
        }

        if (!isWallDamaged && wall.getBound().overlaps(knight.getNail().getHitBox()) && !wall.isDestroyed()) {
            wall.takeDamage();
        }
    }

    public void checkKnightDamage(Knight knight, ArrayList<EnemyModel> enemies) {
        float damageKnockBack = knight.getCurrentKnockBack();
        int currentNailDamage = knight.getCurrentNailDamage();
        if (knight.getPlayerCondition() != DASHING) {
            dashedEnemies.clear();
        }
        for (EnemyModel enemy : enemies) {
            if (enemy.getHitBox().overlaps(knight.getHitBox()) && !enemy.isDead()) {

                if (knight.hasSharpShadow() && knight.getPlayerCondition() == DASHING) {
                    if(!dashedEnemies.contains(enemy)) {
                        enemy.takeDamage(currentNailDamage);
                        dashedEnemies.add(enemy);
                    }
                } else if (!knight.isInvincible()) {
                    float knockBackDirection = (knight.getPosition().x > enemy.getPosition().x) ? 1f : -1f;
                    knight.setKnockBackVelocity(new Vector2(knockBackDirection * damageKnockBack, 400.0f));
                    knight.takeDamage(1);
                }

            }
        }
    }
}
