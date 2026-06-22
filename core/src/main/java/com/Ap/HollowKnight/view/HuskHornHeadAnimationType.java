package com.Ap.HollowKnight.view;

import com.badlogic.gdx.graphics.g2d.Animation;

public enum HuskHornHeadAnimationType  implements AnimationType{
    ATTACK("animation/Husk_HornHead/Attack Lunge.png",12,1,12, Animation.PlayMode.LOOP),
    IDLE("animation/Husk_HornHead/Idle.png",6,1,6, Animation.PlayMode.LOOP),
    TURN("animation/Husk_HornHead/Turn.png",2,1,2, Animation.PlayMode.NORMAL),
    WALK("animation/Husk_HornHead/Walk.png",7,1,7, Animation.PlayMode.LOOP),
    DEATH_LAND("animation/Husk_HornHead/Death Land.png",8,1,8, Animation.PlayMode.NORMAL),
    DEATH_AIR("animation/Husk_HornHead/Death Air.png",1,1,1, Animation.PlayMode.NORMAL);


    private final String path;
    private final int frameCount;
    private final int rowCount;
    private final int colCount;
    private final Animation.PlayMode playMode;

    HuskHornHeadAnimationType(String path, int frameCount, int rowCount, int colCount, Animation.PlayMode playMode) {
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
