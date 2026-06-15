package com.Ap.HollowKnight.view;

public enum AnimationType {
    KNIGHT_MOVE_RIGHT("path",60,8,8);

    private final String path ;
    private final int frameCount;
    private final int rowCount;
    private final int colCount;
    AnimationType(String path, int frameCount, int rowCount, int colCount) {
        this.path = path;
        this.frameCount = frameCount;
        this.rowCount = rowCount;
        this.colCount = colCount;
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
}
