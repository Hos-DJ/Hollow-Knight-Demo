package com.Ap.HollowKnight.view;

import com.badlogic.gdx.graphics.g2d.Animation;

public enum MossflyAnimationType implements AnimationType {
    APPEAR("animation/Mossfly/Appear.png", 6, 1, 6, Animation.PlayMode.NORMAL),
    FLY("animation/Mossfly/Fly.png", 4, 1, 4, Animation.PlayMode.LOOP),
    DEATH_AIR("animation/Mossfly/Death Air.png", 4, 1, 4, Animation.PlayMode.NORMAL),
    DEATH_LAND("animation/Mossfly/Death Land.png", 2, 1, 2, Animation.PlayMode.NORMAL),
    SHAKE("animation/Mossfly/Shake.png", 3, 1, 3, Animation.PlayMode.LOOP),
    TURN_TO_FLY("animation/Mossfly/TurnToFly.png", 3, 1, 3, Animation.PlayMode.NORMAL);

    private final Spec spec;

    MossflyAnimationType(String path, int frameCount, int rowCount, int colCount, Animation.PlayMode playMode) {
        this.spec = new Spec(path, frameCount, rowCount, colCount, playMode);
    }

    @Override
    public Spec getSpec() {
        return spec;
    }
}
