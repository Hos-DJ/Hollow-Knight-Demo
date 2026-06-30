package com.Ap.HollowKnight.model.map;

public enum BlockType {
    SPIKE("spike"),
    CEIL("ceil"),
    GROUND("ground"),
    WALL("wall");

    private final String name;

    BlockType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static BlockType fromName(String name) {
        for (BlockType blockType : BlockType.values()) {
            if (blockType.getName().equals(name)) {
                return blockType;
            }
        }
        return null;
    }

    public boolean blocksVertical() {
        return this == GROUND || this == CEIL;
    }

    public boolean blocksHorizontal() {
        return this == WALL;
    }

    public boolean isHazard() {
        return this == SPIKE;
    }
}
