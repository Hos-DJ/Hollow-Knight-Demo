package com.Ap.HollowKnight.view;

import com.badlogic.gdx.graphics.g2d.Animation;

public enum AnimationType {
    KNIGHT_AIRBORNE("animations/Knight/Airborne.png", 12, 1, 12, Animation.PlayMode.LOOP),
    KNIGHT_DASH("animations/Knight/Dash.png", 12, 1, 12, Animation.PlayMode.LOOP),
    KNIGHT_DEATH("animations/Knight/Death.png", 18, 1, 18, Animation.PlayMode.LOOP),
    KNIGHT_DOUBLE_JUMP("animations/Knight/Double Jump.png", 8, 1, 8, Animation.PlayMode.NORMAL),
    KNIGHT_DOWN_SLASH("animations/Knight/DownSlash.png", 5, 1, 5, Animation.PlayMode.NORMAL),
    KNIGHT_FIREBALL_CAST("animations/Knight/Fireball Cast.png", 9, 1, 9, Animation.PlayMode.NORMAL),
    KNIGHT_FOCUS_END("animations/Knight/Focus End.png", 3, 1, 3, Animation.PlayMode.NORMAL),
    KNIGHT_FOCUS_GET("animations/Knight/Focus Get.png", 6, 1, 6, Animation.PlayMode.LOOP),
    KNIGHT_FOCUS("animations/Knight/Focus.png", 4, 1, 4, Animation.PlayMode.LOOP),
    KNIGHT_IDLE_HURT("animations/Knight/Idle Hurt.png", 12, 1, 12, Animation.PlayMode.NORMAL),
    KNIGHT_IDLE("animations/Knight/Idle.png", 9, 1, 9, Animation.PlayMode.LOOP),
    KNIGHT_LANDING("animations/Knight/Landing.png", 4, 1, 4, Animation.PlayMode.NORMAL),
    KNIGHT_RUN("animations/Knight/Run.png", 13, 1, 13, Animation.PlayMode.LOOP),
    KNIGHT_WALL_SLIDE("animations/Knight/Wall Slide.png", 4, 1, 4, Animation.PlayMode.NORMAL),
    KNIGHT_WALL_JUMP("animations/Knight/Walljump.png", 9, 1, 9, Animation.PlayMode.NORMAL),;
    private final String path ;
    private final int frameCount;
    private final int rowCount;
    private final int colCount;
    private final Animation.PlayMode playMode;
    AnimationType(String path, int frameCount, int rowCount, int colCount ,  Animation.PlayMode playMode) {
        this.path = path;
        this.frameCount = frameCount;
        this.rowCount = rowCount;
        this.colCount = colCount;
        this.playMode = playMode;
    }

    public String getPath() {
        return path;
    }

    public int getFrameCount() {
        return frameCount;
    }

    public int getRowCount() {
        return rowCount;
    }

    public int getColCount() {
        return colCount;
    }

    public Animation.PlayMode getPlayMode() {
        return playMode;
    }
}
