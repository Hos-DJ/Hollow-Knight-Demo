package com.Ap.HollowKnight.controller;

import com.Ap.HollowKnight.model.enemy.CrystalGuardian;
import com.Ap.HollowKnight.model.enemy.EnemyModel;
import com.Ap.HollowKnight.model.map.Block;
import com.Ap.HollowKnight.model.player.Knight;
import com.Ap.HollowKnight.model.spells.HowlingWrath;
import com.Ap.HollowKnight.model.spells.SpellManager;
import com.Ap.HollowKnight.model.zote.Zote;
import com.Ap.HollowKnight.model.zote.ZoteState;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class GameFlowController {
    private Knight knight;
    private TiledMap map;
    private ArrayList<Block> blocks;
    private ArrayList<EnemyModel> enemies;
    private GameProcessor gameProcessor;
    private Zote zote;


    public GameFlowController(Knight knight, TiledMap map, ArrayList<Block> blocks, GameProcessor gameProcessor,
                              ArrayList<EnemyModel> enemies , Zote zote) {
        this.knight = knight;
        this.map = map;
        this.blocks = blocks;
        this.gameProcessor = gameProcessor;
        this.enemies = enemies;
        this.zote = zote ;
    }

    public void update(float delta) {
        if(zote.getStatus()!=ZoteState.TALKING)
            gameProcessor.pollMovement();
        knight.update(delta, blocks);

        zote.update(delta, blocks);
        boolean nearby = zote.getInteractionZone().overlaps(knight.getHitBox());
        zote.setPlayerNearby(nearby);
        zote.setPlayerNearby(zote.getInteractionZone().overlaps(knight.getHitBox()));
        zote.trackKnight(knight.getPosition());
        if (knight.isAttacking()
            && knight.getNail().getHitBox().overlaps(zote.getHitBox())) {
            zote.knock(knight.getPosition());
        }
        if (zote.getStatus() == ZoteState.ENRAGED
            && zote.getHitBox().overlaps(knight.getHitBox())) {
            zote.knockForAttacking();
            float dir = (knight.getPosition().x > zote.getPosition().x) ? 1f : -1f;
            knight.setKnockBackVelocity(new Vector2(dir * 300f, 200f));
        }



        SpellManager spells =knight.getSpellManager();

        CombatController.getInstance().checkKnightDamage(knight, enemies);

        for (EnemyModel enemy : enemies) {
            if (!enemy.isDead()) {
                enemy.update(delta, blocks);
                if (spells.getVengefulSprit().isActive() &&
                    spells.getVengefulSprit().getHitBox().overlaps(enemy.getHitBox())) {
                    enemy.takeDamage(15);
                }
                HowlingWrath wrath = spells.getHowlingWraiths();
                if (wrath.isActive() && wrath.hasPendingTick() &&
                    wrath.getHitBox().overlaps(enemy.getHitBox())) {
                    enemy.takeDamage(15);
                    wrath.consumeTick();
                }
                if (enemy instanceof CrystalGuardian guardian) {
                    if (guardian.getFov().overlaps(knight.getHitBox())) {
                        guardian.onPlayerSpotted(knight.getPosition());
                    }
                    if (guardian.isAttacking() && guardian.getLaser().overlaps(knight.getHitBox())) {
                        knight.takeDamage(1);
                        knight.setKnockBackVelocity(new Vector2(0, 400.0f));
                    }
                }
            }
        }
    }
}
