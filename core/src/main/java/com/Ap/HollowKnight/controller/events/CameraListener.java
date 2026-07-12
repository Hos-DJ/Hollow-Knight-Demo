package com.Ap.HollowKnight.controller.events;

import com.Ap.HollowKnight.model.game.GameCamera;
import com.badlogic.gdx.math.Rectangle;

public class CameraListener implements GameEventListener {
    private final GameCamera camera;

    public CameraListener(GameCamera camera) {
        this.camera = camera;
    }

    @Override
    public void onEvent(GameEvent e , Object payload) {
        switch(e){
            case PLAYER_HURT -> onPlayerHurt(1);
            case PLAYER_DOUBLE_HURT -> onPlayerHurt(1.5f);
            case SLAM_MACE, POWER_SLAM_MACE -> onBossMace();
            case PLAYER_HOWLING, PLAYER_VENGEFUL -> onSpellCast();
            case BOSS_JUMP,BOSS_LAND -> onBossMove();
        }
    }

    public void onPlayerHurt(float multiplier) {
        if (camera != null) camera.initializeShake(15f, 0.25f);
    }

    public void onSpellCast() {
        if (camera != null) camera.initializeShake(8f, 0.2f);
    }
    public void onBossMove(){
        if(camera != null){
            camera.initializeShake(15,0.3f);
        }
    }

    public void onBossMace(){
        if(camera != null){
            camera.initializeShake(22f,0.4f);
        }
    }
}
