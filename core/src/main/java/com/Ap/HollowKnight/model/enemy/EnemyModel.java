package com.Ap.HollowKnight.model.enemy;

import com.Ap.HollowKnight.model.game.FacingDirection;
import com.Ap.HollowKnight.model.game.PhysicalPart;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public abstract class EnemyModel extends PhysicalPart {

    public EnemyModel(Vector2 position, float maxVelocity, Rectangle hitBox) {
        super(position, maxVelocity, hitBox);
    }

    public boolean isHeadingForCliff(TiledMapTileLayer layer) {
        if (layer == null || !this.isOnGround()) return false;
        float checkX = (this.getFacingDirection() == FacingDirection.RIGHT) ? this.getHitBox().x + this.getHitBox().width + 5 : this.getHitBox().x - 5;
        float checkY = this.getHitBox().y - 5;
        int col = (int) (checkX / layer.getTileWidth());
        int row = (int) (checkY / layer.getTileHeight());
        TiledMapTileLayer.Cell cell = layer.getCell(col, row);
        return cell == null || cell.getTile() == null || !cell.getTile().getProperties().containsKey("solid");
    }

    @Override
    public void update(float delta, TiledMapTileLayer layer) {

    }

    @Override
    public void takeDamage(int amount) {

    }
}
