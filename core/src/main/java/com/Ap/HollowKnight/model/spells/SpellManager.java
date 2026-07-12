package com.Ap.HollowKnight.model.spells;

import com.Ap.HollowKnight.model.game.FacingDirection;
import com.Ap.HollowKnight.model.map.Block;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class SpellManager {
    private static final float SOUL_CAST = 33f;
    private static final float CAST_DURATION = 0.5f;

    private final VengefulSprit vengefulSprit = new VengefulSprit();
    private final HowlingWrath  howlingWrathSpell = new HowlingWrath();

    public void update(float delta, ArrayList<Block> blocks) {
        vengefulSprit.update(delta, blocks);
        howlingWrathSpell.update(delta, blocks);
    }

    public boolean castVengefulSpirit(Rectangle knightHitBox, FacingDirection facingDirection) {
        if (vengefulSprit.isActive()) return false;
        vengefulSprit.cast(knightHitBox, facingDirection);
        return true;
    }

    public boolean castHowlingWrath(Rectangle knightHitBox, FacingDirection facingDirection) {
        if (howlingWrathSpell.isActive()) return false;
        howlingWrathSpell.cast(knightHitBox, facingDirection);
        return true;
    }

    public VengefulSprit getVengefulSprit() {
        return vengefulSprit;
    }

    public HowlingWrath getHowlingWraiths() { return howlingWrathSpell; }

}
