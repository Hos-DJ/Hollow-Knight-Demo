package com.Ap.HollowKnight.model.game;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
//todo: need to implement the collision with enemies and spikes
public abstract class PhysicalPart {
    private Vector2 position;
    private Vector2 velocity;
    private Vector2 knockBackVelocity;
    private Vector2 acceleration;
    private Rectangle hitBox;

    // positioning
    private boolean isOnGround;
    private boolean isCooldown;
    private boolean isAttacking ;
    private boolean gravityIncluded;
    private FacingDirection facingDirection;
    private float maxVelocity;

    protected static final float GRAVITY = -10.0f;

    public PhysicalPart(Vector2 position, float maxVelocity, Rectangle hitBox) {
        this.position         = position;
        this.maxVelocity      = maxVelocity;
        this.hitBox           = hitBox;
        this.velocity         = new Vector2(0f, 0f);
        this.knockBackVelocity= new Vector2(0f, 0f);
        this.acceleration     = new Vector2(0f, 0f);
        this.facingDirection  = FacingDirection.RIGHT;
        this.isOnGround       = false;
        this.isCooldown       = false;
        this.isAttacking      = false;
        this.gravityIncluded  = true;
        updateHitBox();
    }
    public void updateHitBox(){
        hitBox.setPosition(position.x, position.y);
    }
    public abstract void update(float delta,TiledMapTileLayer layer);
    public abstract void takeDamage(int amount);

    public void applyPhysics(float delta, TiledMapTileLayer layer) {
        if(!isOnGround&&gravityIncluded){
            this.velocity.y+=GRAVITY *delta;
        }
        else if (isOnGround){
            this.velocity.y=0.0f;
        }
        movingX(delta,layer);
        movingY(delta,layer);
        this.knockBackVelocity.scl(0.8f);
        if (this.knockBackVelocity.len()<0.05f){
            this.knockBackVelocity.setZero();
        }
        updateHitBox();
    }
    public void movingX(float delta, TiledMapTileLayer layer) {
        position.x += (velocity.x + knockBackVelocity.x) * delta;

        updateHitBox();

        resolveHorizontalCollisions(layer);
    }

    public void movingY(float delta, TiledMapTileLayer layer) {
        setOnGround(false);
        position.y += (velocity.y + knockBackVelocity.y) * delta;
        updateHitBox();

        resolveVerticalCollisions(layer);
    }

    private void resolveHorizontalCollisions(TiledMapTileLayer layer) {
        if (layer == null) return;
        int tileWidth = (int) layer.getTileWidth();
        int startX = (int) (hitBox.x / tileWidth);
        int endX = (int) ((hitBox.x + hitBox.width) / tileWidth);
        int startY = (int) (hitBox.y / (int) layer.getTileHeight());
        int endY = (int) ((hitBox.y + hitBox.height) / (int) layer.getTileHeight());

        for (int row = startY; row <= endY; row++) {
            for (int col = startX; col <= endX; col++) {
                TiledMapTileLayer.Cell cell = layer.getCell(col, row);
                if (cell != null && cell.getTile() != null && cell.getTile().getProperties().containsKey("solid")) {
                    if ((velocity.x + knockBackVelocity.x) > 0) {
                        position.x = col * tileWidth - hitBox.width;
                    } else if ((velocity.x + knockBackVelocity.x) < 0) {
                        position.x = (col + 1) * tileWidth;
                    }
                    velocity.x = 0;
                    updateHitBox();
                    return;
                }
            }
        }
    }

    private void resolveVerticalCollisions(TiledMapTileLayer layer) {
        if (layer == null) return;
        int tileHeight = (int) layer.getTileHeight();
        int startX = (int) (hitBox.x / (int) layer.getTileWidth());
        int endX = (int) ((hitBox.x + hitBox.width) / (int) layer.getTileWidth());
        int startY = (int) (hitBox.y / tileHeight);
        int endY = (int) ((hitBox.y + hitBox.height) / tileHeight);

        boolean foundGround = false;

        for (int row = startY; row <= endY; row++) {
            for (int col = startX; col <= endX; col++) {
                TiledMapTileLayer.Cell cell = layer.getCell(col, row);
                if (cell != null && cell.getTile() != null && cell.getTile().getProperties().containsKey("solid")) {
                    if ((velocity.y + knockBackVelocity.y) < 0) {
                        position.y = (row + 1) * tileHeight;
                        velocity.y = 0;
                        foundGround = true; // Landing on platforms sets isGrounded = true (Phase 2.2)
                    } else if ((velocity.y + knockBackVelocity.y) > 0) {
                        position.y = row * tileHeight - hitBox.height;
                        velocity.y = 0;
                    }
                    updateHitBox();
                }
            }
        }
        this.isOnGround = foundGround;
    }
    //getters



    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public Vector2 getKnockBackVelocity() {
        return knockBackVelocity;
    }

    public Vector2 getAcceleration() {
        return acceleration;
    }

    public Rectangle getHitBox() {
        return hitBox;
    }

    public boolean isOnGround() {
        return isOnGround;
    }

    public boolean isCooldown() {
        return isCooldown;
    }

    public FacingDirection getFacingDirection() {
        return facingDirection;
    }

    public boolean isAttacking() {
        return isAttacking;
    }

    public boolean isGravityIncluded() {
        return gravityIncluded;
    }

    public float getMaxVelocity() {
        return maxVelocity;
    }


    //setters



    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
    }

    public void setKnockBackVelocity(Vector2 knockBackVelocity) {
        this.knockBackVelocity = knockBackVelocity;
    }

    public void setAcceleration(Vector2 acceleration) {
        this.acceleration = acceleration;
    }

    public void setHitBox(Rectangle hitBox) {
        this.hitBox = hitBox;
    }

    public void setOnGround(boolean onGround) {
        isOnGround = onGround;
    }


    public void setCooldown(boolean cooldown) {
        isCooldown = cooldown;
    }

    public void setFacingDirection(FacingDirection facingDirection) {
        this.facingDirection = facingDirection;
    }

    public void setAttacking(boolean attacking) {
        isAttacking = attacking;
    }

    public void setGravityIncluded(boolean gravityIncluded) {
        this.gravityIncluded = gravityIncluded;
    }

    public void setMaxVelocity(float maxVelocity) {
        this.maxVelocity = maxVelocity;
    }


}
