package com.Ap.HollowKnight.view;

import com.badlogic.gdx.graphics.g2d.Animation;

public enum CrawlidAnimationType implements AnimationType{
    DEATH_AIR("animation/Crawlid/Death Air.png",3,1,3, Animation.PlayMode.NORMAL),
    DEATH_LAND("animation/Crawlid/Death land.png",2,1,2 , Animation.PlayMode.NORMAL),
    WALK("animation/Crawlid/Walk.png",4,1,4, Animation.PlayMode.LOOP),
    TURN("animation/Crawlid/Turn.png" , 2,1,2, Animation.PlayMode.NORMAL),;

    private final String path;
    private final int frameCount;
    private final int rowCount;
    private final int colCount;
    private final Animation.PlayMode playMode;

    CrawlidAnimationType(String path, int frameCount, int rowCount, int colCount, Animation.PlayMode playMode) {
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
