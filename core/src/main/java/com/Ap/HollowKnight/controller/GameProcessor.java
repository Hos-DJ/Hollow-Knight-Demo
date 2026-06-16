package com.Ap.HollowKnight.controller;

import com.Ap.HollowKnight.model.game.FacingDirection;
import com.Ap.HollowKnight.model.game.GameCamera;
import com.Ap.HollowKnight.model.player.Knight;
import com.Ap.HollowKnight.model.player.PlayerCondition;
import com.badlogic.gdx.InputAdapter;

public class GameProcessor extends InputAdapter {

    private Knight knight;
    private GameCamera camera;
    public GameProcessor(Knight knight, GameCamera camera) {
        this.knight = knight;
        this.camera = camera;
    }

    @Override
    public boolean keyDown(int keycode) {
        GameKeypad key = GameKeypad.fromKeycode(keycode);
        if(key ==null)
            return false;
        switch (key) {
            case GameKeypad.RIGHT -> {
                knight.setFacingDirection(FacingDirection.RIGHT);
                knight.move();
            }
            case GameKeypad.LEFT -> {
                knight.setFacingDirection(FacingDirection.LEFT);
                knight.move();
            }
            case GameKeypad.UP -> {
                knight.setPlayerCondition(PlayerCondition.LOOKING_UP);
                camera.setLookingUp(true);
            }
            case GameKeypad.DOWN -> {
                knight.setPlayerCondition(PlayerCondition.LOOKING_DOWN);
                camera.setLookingDown(true);
            }
            case GameKeypad.JUMP -> knight.jump();
            case GameKeypad.DASH -> knight.dash();
            case GameKeypad.ATTACK, GameKeypad.VENGEFUL_SPIRIT -> {
                //something will happen here
            }
            case GameKeypad.FOCUS -> knight.focus();
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        GameKeypad key = GameKeypad.fromKeycode(keycode);
        if(key ==null)
            return false;
        switch (key) {
            case GameKeypad.JUMP -> knight.cutJump();
            case GameKeypad.DOWN -> {
                camera.setLookingDown(false);
                knight.setPlayerCondition(PlayerCondition.IDLE);
            }
            case GameKeypad.UP -> {
                camera.setLookingUp(false);
                knight.setPlayerCondition(PlayerCondition.IDLE);
            }
        }
        return true;
    }
}
