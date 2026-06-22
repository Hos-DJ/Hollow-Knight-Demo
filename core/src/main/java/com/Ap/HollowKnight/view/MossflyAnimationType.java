package com.Ap.HollowKnight.view;

import com.badlogic.gdx.graphics.g2d.Animation;

public enum MossflyAnimationType implements AnimationType {
    APPEAR("aniamtion/Mossfly/Appear.png",6,1,6, Animation.PlayMode.NORMAL),
    FLY("aniamtion/Mossfly/Fly.png",4,1,4, Animation.PlayMode.LOOP),
    DEATH_AIR("aniamtion/Mossfly/Death Air.png",4,1,4, Animation.PlayMode.NORMAL),
    DEATH_LAND("aniamtion/Mossfly/Death Land.png",2,1,2, Animation.PlayMode.NORMAL),
    SHAKE("aniamtion/Mossfly/Shake.png",3,1,3, Animation.PlayMode.LOOP),
    TURN_TO_FLY("aniamtion/Mossfly/TurnToFly.png",3,1,3, Animation.PlayMode.NORMAL);

    private final String path;
    private final int frameCount;
    private final int rowCount;
    private final int colCount;
    private final Animation.PlayMode playMode;
    MossflyAnimationType(String path, int frameCount, int rowCount, int colCount, Animation.PlayMode playMode) {
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
