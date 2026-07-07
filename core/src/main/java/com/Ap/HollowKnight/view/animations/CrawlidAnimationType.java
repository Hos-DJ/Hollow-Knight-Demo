package com.Ap.HollowKnight.view.animations;

import com.badlogic.gdx.graphics.g2d.Animation;

public enum CrawlidAnimationType implements AnimationType {
    DEATH_AIR("animation/Crawlid/Death Air.png", 3, 1, 3, Animation.PlayMode.NORMAL),
    DEATH_LAND("animation/Crawlid/Death land.png", 2, 1, 2, Animation.PlayMode.NORMAL),
    WALK("animation/Crawlid/Walk.png", 4, 1, 4, Animation.PlayMode.LOOP),
    TURN("animation/Crawlid/Turn.png", 2, 1, 2, Animation.PlayMode.NORMAL);

    private final Spec spec;

    CrawlidAnimationType(String path, int frameCount, int rowCount, int colCount, Animation.PlayMode playMode) {
        this.spec = new Spec(path, frameCount, rowCount, colCount, playMode);
    }

    @Override
    public Spec getSpec() {
        return spec;
    }
}
