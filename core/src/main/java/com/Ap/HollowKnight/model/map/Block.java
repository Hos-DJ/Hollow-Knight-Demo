package com.Ap.HollowKnight.model.map;

import com.badlogic.gdx.math.Rectangle;

public class Block {
    private final Rectangle bound ;
    private final BlockType type;

    public Block(Rectangle bound , BlockType type){
        this.bound = bound;
        this.type = type;
    }

    public Rectangle getBound() {
        return bound;
    }
    public BlockType getType() {
        return type;
    }
}
