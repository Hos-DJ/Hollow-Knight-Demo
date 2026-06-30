package com.Ap.HollowKnight.view.animations;

import com.badlogic.gdx.graphics.g2d.Animation;

public enum KnightAnimationType implements AnimationType {
    KNIGHT_AIRBORNE("animation/Airborne.png", 12, 1, 12, Animation.PlayMode.LOOP),
    KNIGHT_DASH("animation/Dash.png", 12, 1, 12, Animation.PlayMode.LOOP),
    KNIGHT_DEATH("animation/Death.png", 18, 1, 18, Animation.PlayMode.LOOP),
    KNIGHT_DOUBLE_JUMP("animation/Double Jump.png", 8, 1, 8, Animation.PlayMode.NORMAL),
    KNIGHT_SLASH("animation/Slash.png", 5, 1, 5, Animation.PlayMode.NORMAL),
    KNIGHT_UP_SLASH("animation/UpSlash.png", 5, 1, 5, Animation.PlayMode.NORMAL),
    KNIGHT_DOWN_SLASH("animation/DownSlash.png", 5, 1, 5, Animation.PlayMode.NORMAL),
    KNIGHT_FIREBALL_CAST("animation/Fireball Cast.png", 9, 1, 9, Animation.PlayMode.NORMAL),
    KNIGHT_FOCUS_END("animation/Focus End.png", 3, 1, 3, Animation.PlayMode.NORMAL),
    KNIGHT_FOCUS_GET("animation/Focus Get.png", 6, 1, 6, Animation.PlayMode.LOOP),
    KNIGHT_FOCUS("animation/Focus.png", 4, 1, 4, Animation.PlayMode.LOOP),
    KNIGHT_IDLE_HURT("animation/Idle Hurt.png", 12, 1, 12, Animation.PlayMode.NORMAL),
    KNIGHT_IDLE("animation/Idle.png", 9, 1, 9, Animation.PlayMode.LOOP),
    KNIGHT_LANDING("animation/Landing.png", 4, 1, 4, Animation.PlayMode.NORMAL),
    KNIGHT_RUN("animation/Run.png", 13, 1, 13, Animation.PlayMode.LOOP_PINGPONG),
    KNIGHT_WALL_SLIDE("animation/Wall Slide.png", 4, 1, 4, Animation.PlayMode.NORMAL),
    KNIGHT_WALL_JUMP("animation/Walljump.png", 9, 1, 9, Animation.PlayMode.NORMAL);

    private final Spec spec;

    KnightAnimationType(String path, int frameCount, int rowCount, int colCount, Animation.PlayMode playMode) {
        this.spec = new Spec(path, frameCount, rowCount, colCount, playMode);
    }

    @Override
    public Spec getSpec() {
        return spec;
    }
}
