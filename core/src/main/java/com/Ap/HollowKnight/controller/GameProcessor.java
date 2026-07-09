package com.Ap.HollowKnight.controller;

import com.Ap.HollowKnight.model.AttackDirection;
import com.Ap.HollowKnight.model.enemy.EnemyModel;
import com.Ap.HollowKnight.model.game.FacingDirection;
import com.Ap.HollowKnight.model.game.GameCamera;
import com.Ap.HollowKnight.model.level.LevelModel;
import com.Ap.HollowKnight.model.map.DestructibleWall;
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
    private boolean paused =false;
    private boolean isInventory = false ;
    private boolean inventoryTriggered = false;
    private boolean isWallDestroyed = false;
    private boolean isActivatorPressed = false;
    public GameProcessor(Knight knight, GameCamera camera, ArrayList<EnemyModel> enemies,Zote zote) {
        this.knight = knight;
        this.camera = camera;
        this.enemies = enemies;
        this.zote = zote;

    }

    @Override
    public boolean keyDown(int keycode) {
        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            paused = true;
        }
        GameKeypad key = GameKeypad.fromKeycode(keycode);
        if (key == GameKeypad.INVENTORY) {
            if (!paused) {
                inventoryTriggered = true;
            }
            return true;
        }
        if (isInventory) {
            return true;
        }
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
            if (key == null)
                return false;
            if (keycode == GameKeypad.CHEAT_ACTIVATOR.getKeyNumber()) {
                isActivatorPressed = true;
                return true;
            }
            if (isActivatorPressed) {
                if (keycode == GameKeypad.CHEAT_GOD.getKeyNumber()) {
                    boolean toggle = knight.isImmortal();
                    knight.setImmortal(!toggle);
                    return true;
                }
                else if (keycode == GameKeypad.CHEAT_SOUL.getKeyNumber()) {
                    knight.gainFullSoul();
                    return true;
                }
                else if (keycode == GameKeypad.CHEAT_BOSS.getKeyNumber()) {
                    knight.goToBoss();
                    return true;
                }
                else if (keycode == GameKeypad.CHEAT_EMERGENCY_HEALTH.getKeyNumber()) {
                    knight.gainMask();
                    return true;
                }
                else if (keycode == GameKeypad.CHEAT_SPECTATOR.getKeyNumber()) {
                    boolean toggle = knight.isSpectator();
                    knight.changeToSpectator(!toggle);
                    return true;
                }
                else if (keycode == GameKeypad.CHEAT_DEADLY_NAIL.getKeyNumber()) {
                    boolean toggle =  knight.isDeadlyNail();
                    knight.oneShotNail(!toggle);
                    return true;
                }
            }
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
        if(isInventory){
            return true;
        }
        if (key == null) {
            return false;
        }
        if(zote.getStatus()== ZoteState.TALKING){
            return false;
        }
        switch (key) {
            case JUMP -> knight.cutJump();
            case DOWN -> {

                camera.setLookingDown(false);

                if (knight.getPlayerCondition() == PlayerCondition.LOOKING_DOWN) {
                    knight.setPlayerCondition(PlayerCondition.IDLE);
                }
            }
            case UP -> {
                camera.setLookingUp(false);

                if (knight.getPlayerCondition() == PlayerCondition.LOOKING_UP) {
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
        if(isInventory||zote.getStatus()== ZoteState.TALKING){
            knight.stop();
            return;
        }
        if (knight.getWallJumpTimer() > 0) {
            return;
        }
        boolean rightHeld = Gdx.input.isKeyPressed(GameKeypad.RIGHT.getKeyNumber());
        boolean leftHeld = Gdx.input.isKeyPressed(GameKeypad.LEFT.getKeyNumber());

        if (rightHeld == leftHeld) {
            knight.stop();
        } else if (rightHeld) {
            camera.setLookingDown(false);
            camera.setLookingUp(false);
            knight.setFacingDirection(FacingDirection.RIGHT);
            knight.move();
        } else {
            camera.setLookingDown(false);
            camera.setLookingUp(false);
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
                DestructibleWall wall = LevelModel.getInstance().getDestructibleWall();
                CombatController.getInstance().checkCombat(knight, enemies, wall);
                if(wall.isDestroyed())
                    isWallDestroyed = true;

            }

        }
    }

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public boolean isInventory() {
        return isInventory;
    }

    public void setInventory(boolean inventory) {
        isInventory = inventory;
    }

    public boolean isInventoryTriggered() {
        return inventoryTriggered;
    }

    public void setInventoryTriggered(boolean inventoryTriggered) {
        this.inventoryTriggered = inventoryTriggered;
    }

    public boolean isWallDestroyed() {
        return isWallDestroyed;
    }

    public void setWallDestroyed(boolean wallDestroyed) {
        isWallDestroyed = wallDestroyed;
    }
}
