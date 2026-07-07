package com.Ap.HollowKnight.view.animations;

import com.badlogic.gdx.graphics.g2d.Animation;

public enum CrystalGuardianAnimationType implements AnimationType {
    DEATH_AIR("animation/Crystallized/Death Air.png", 3, 1, 3, Animation.PlayMode.NORMAL),
    DEATH_LAND("animation/Crystallized/Death Land.png", 3, 1, 3, Animation.PlayMode.NORMAL),
    IDLE("animation/Crystallized/Idle.png", 5, 1, 5, Animation.PlayMode.LOOP),
    RUN("animation/Crystallized/Run.png", 6, 1, 6, Animation.PlayMode.LOOP),
    SHOOT("animation/Crystallized/Shoot.png", 7, 1, 7, Animation.PlayMode.NORMAL),
    TURN("animation/Crystallized/Turn.png", 3, 1, 3, Animation.PlayMode.NORMAL);

    private final Spec spec;

    CrystalGuardianAnimationType(String path, int frameCount, int rowCount, int colCount, Animation.PlayMode playMode) {
        this.spec = new Spec(path, frameCount, rowCount, colCount, playMode);
    }


    @Override
    public Spec getSpec() {
        return this.spec;
    }
}
