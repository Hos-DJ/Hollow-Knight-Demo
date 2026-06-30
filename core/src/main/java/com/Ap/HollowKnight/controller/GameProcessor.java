package com.Ap.HollowKnight.controller;

import com.Ap.HollowKnight.model.AttackDirection;
import com.Ap.HollowKnight.model.enemy.EnemyModel;
import com.Ap.HollowKnight.model.game.FacingDirection;
import com.Ap.HollowKnight.model.game.GameCamera;
import com.Ap.HollowKnight.model.player.Knight;
import com.Ap.HollowKnight.model.player.PlayerCondition;
import com.Ap.HollowKnight.model.zote.Zote;
import com.Ap.HollowKnight.model.zote.ZoteState;
import com.Ap.HollowKnight.view.animations.EffectAnimationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class GameProcessor extends InputAdapter {

    private final Knight knight;
    private final GameCamera camera;
    private ArrayList<EnemyModel> enemies;
    private final Zote zote;

    public GameProcessor(Knight knight, GameCamera camera, ArrayList<EnemyModel> enemies,Zote zote) {
        this.knight = knight;
        this.camera = camera;
        this.enemies = enemies;
        this.zote = zote;

    }

    @Override
    public boolean keyDown(int keycode) {

        if(zote.getStatus()== ZoteState.TALKING){
            if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER)){
                boolean success =zote.nextDialogue();
                if(!success){
                    zote.stopTalking();
                }
            }
            return true;
        }
        else{
            GameKeypad key = GameKeypad.fromKeycode(keycode);
            if (key == null)
                return false;
            switch (key) {
                case RIGHT -> {
                    knight.setFacingDirection(FacingDirection.RIGHT);
                    knight.move();
                }
                case LEFT -> {
                    knight.setFacingDirection(FacingDirection.LEFT);
                    knight.move();
                }
                case UP -> {
                    if (knight.getPlayerCondition() == PlayerCondition.IDLE) {
                        knight.setPlayerCondition(PlayerCondition.LOOKING_UP);
                        camera.setLookingUp(true);

                    }
                }
                case DOWN -> {
                    if (knight.getPlayerCondition() == PlayerCondition.IDLE) {
                        knight.setPlayerCondition(PlayerCondition.LOOKING_DOWN);
                        camera.setLookingDown(true);
                    }
                }
                case JUMP -> knight.jump();
                case DASH -> knight.dash();
                case ATTACK -> {
                    handleAttacking();
                }
                case FOCUS -> {
                    if (zote.isPlayerNearby() && Gdx.input.isKeyJustPressed(GameKeypad.FOCUS.getKeyNumber())) {
                        zote.startTalking(knight.getPosition());
                        knight.setVelocity(Vector2.Zero);
                    }
                    else if (knight.getPlayerCondition() == PlayerCondition.IDLE && knight.getPlayerCondition() != PlayerCondition.FOCUSING) {
                        knight.focus();
                    }
                }
                case VENGEFUL_SPIRIT -> {
                    if (Gdx.input.isKeyJustPressed(GameKeypad.VENGEFUL_SPIRIT.getKeyNumber())) {
                        knight.castVengeful();
                    }
                }
                case HOWLING_WRATH -> {
                    if (Gdx.input.isKeyJustPressed(GameKeypad.HOWLING_WRATH.getKeyNumber())) {
                        knight.castHowling();
                    }
                }
            }
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        GameKeypad key = GameKeypad.fromKeycode(keycode);
        if (key == null)
            return false;
        if(zote.getStatus()== ZoteState.TALKING){
            return false;
        }
        switch (key) {
            case JUMP -> knight.cutJump();
            case DOWN -> {

                if (knight.getPlayerCondition() == PlayerCondition.LOOKING_DOWN) {
                    camera.setLookingDown(false);
                    knight.setPlayerCondition(PlayerCondition.IDLE);
                }
            }
            case UP -> {
                if (knight.getPlayerCondition() == PlayerCondition.LOOKING_UP) {
                    camera.setLookingUp(false);
                    knight.setPlayerCondition(PlayerCondition.IDLE);
                }
            }
            case RIGHT -> {
                if (Gdx.input.isKeyPressed(GameKeypad.LEFT.getKeyNumber())) {
                    knight.setFacingDirection(FacingDirection.LEFT);
                    knight.move();
                } else {
                    knight.stop();
                }
            }
            case LEFT -> {
                if (Gdx.input.isKeyPressed(GameKeypad.RIGHT.getKeyNumber())) {
                    knight.setFacingDirection(FacingDirection.RIGHT);
                    knight.move();
                } else {
                    knight.stop();
                }
            }
            case FOCUS -> {
                if (knight.getPlayerCondition() == PlayerCondition.FOCUSING) {
                    knight.cancelFocus();
                }
            }
        }
        return true;
    }

    public void pollMovement() {
        boolean rightHeld = Gdx.input.isKeyPressed(GameKeypad.RIGHT.getKeyNumber());
        boolean leftHeld = Gdx.input.isKeyPressed(GameKeypad.LEFT.getKeyNumber());

        if (rightHeld == leftHeld) {
            knight.stop();
        } else if (rightHeld) {
            knight.setFacingDirection(FacingDirection.RIGHT);
            knight.move();
        } else {
            knight.setFacingDirection(FacingDirection.LEFT);
            knight.move();
        }
    }

    public void handleAttacking() {
        AttackDirection direction;
        if (Gdx.input.isKeyPressed(GameKeypad.DOWN.getKeyNumber()) && !knight.isOnGround()) {
            direction = AttackDirection.DOWN;
        } else if (Gdx.input.isKeyPressed(GameKeypad.UP.getKeyNumber())) {
            direction = AttackDirection.UP;
        } else if (Gdx.input.isKeyPressed(GameKeypad.RIGHT.getKeyNumber())) {
            direction = AttackDirection.RIGHT;
        } else if (Gdx.input.isKeyPressed(GameKeypad.LEFT.getKeyNumber())) {
            direction = AttackDirection.LEFT;
        } else {
            direction = (knight.getFacingDirection() == FacingDirection.RIGHT) ? AttackDirection.RIGHT : AttackDirection.LEFT;
        }
        if (Gdx.input.isKeyJustPressed(GameKeypad.ATTACK.getKeyNumber())) {
            boolean attacked = knight.attack(direction);
            if(attacked)
            {
                EffectAnimationType nailAnimation = direction.toAnimationType();
                knight.getNail().setNailSlashType(nailAnimation);
                CombatController.getInstance().checkCombat(knight, enemies);
            }

        }


    }
}
