package com.Ap.HollowKnight.model;

import com.Ap.HollowKnight.model.game.PhysicalPart;
import com.Ap.HollowKnight.model.map.Block;
import com.Ap.HollowKnight.view.EffectAnimationType;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class Nail extends PhysicalPart {
    EffectAnimationType nailSlashType;
    public Nail(Vector2 position,Rectangle hitBox,Vector2 spawnPoint,EffectAnimationType nailSlashType) {
        super(position, hitBox , spawnPoint);
        this.nailSlashType = nailSlashType;
    }

    @Override
    public void update(float delta ,ArrayList<Block> blocks) {

    }

    @Override
    public void takeDamage(int amount) {
        // no damage for nail
    }

    @Override
    public void hazardReact() {

    }

    public EffectAnimationType getNailSlashType() {
        return nailSlashType;
    }

    public void setNailSlashType(EffectAnimationType nailSlashType) {
        this.nailSlashType = nailSlashType;
    }
}
