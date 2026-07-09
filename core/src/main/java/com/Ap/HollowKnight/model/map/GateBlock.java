package com.Ap.HollowKnight.model.map;

import com.badlogic.gdx.math.Rectangle;

public class GateBlock extends Block{
    boolean isPassed = false;
    public GateBlock(Rectangle bound, BlockType type) {
        super(bound, type);
    }
}
