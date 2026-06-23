package com.Ap.HollowKnight.model.game;

import com.Ap.HollowKnight.model.map.Block;
import com.Ap.HollowKnight.model.map.BlockType;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.min;

//todo: need to implement the collision with enemies and spikes
public abstract class PhysicalPart {
    private Vector2 position;
    private Vector2 velocity;
    private Vector2 knockBackVelocity;
    private Vector2 acceleration;
    private Vector2 spawnPoint;
    private Rectangle hitBox;

    // positioning
    private boolean isOnGround;
    private boolean isCooldown;
    private boolean isAttacking ;
    private boolean gravityIncluded;
    private FacingDirection facingDirection;
    private static final float GROUND_SNAP_EPSILON = 0.05f;
    protected static final float GRAVITY = -700.0f;

    public PhysicalPart(Vector2 position, Rectangle hitBox, Vector2 spawnPoint) {
        this.position         = position;
        this.hitBox           = hitBox;
        this.spawnPoint        = spawnPoint;
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
    public abstract void update(float delta, ArrayList<Block> blocks);
    public abstract void takeDamage(int amount);
    public abstract void hazardReact();
    public void applyPhysics(float delta,ArrayList<Block> blocks) {

        if (!isOnGround && gravityIncluded) {
            velocity.y += min(GRAVITY * delta , 300.0f);
        } else if (isOnGround) {
            velocity.y = 0.0f;
        }
        movingX(delta, blocks);
        movingY(delta, blocks);
        knockBackVelocity.scl(0.8f);
        if (knockBackVelocity.len() < 0.05f) {
            knockBackVelocity.setZero();
        }
        resolveHazardCollisions(blocks);
        updateHitBox();
    }

    public void movingX(float delta, List<Block> blocks) {
        position.x += (velocity.x + knockBackVelocity.x) * delta;
        updateHitBox();
        resolveHorizontalCollisions(blocks);
    }

    public void movingY(float delta, List<Block> blocks) {
        setOnGround(false);
        position.y += (velocity.y + knockBackVelocity.y) * delta;
        updateHitBox();
        resolveVerticalCollisions(blocks);
    }

    private void resolveHorizontalCollisions(List<Block> blocks) {
        float moveX = velocity.x + knockBackVelocity.x;
        for (Block block : blocks) {
            if (!block.getType().blocksHorizontal()) continue;
            if (!hitBox.overlaps(block.getBound())) continue;
            if (moveX > 0) {
                position.x = block.getBound().x - hitBox.width;
            } else if (moveX < 0) {
                position.x = block.getBound().x + block.getBound().width;
            }
            velocity.x = 0;
            updateHitBox();
        }
    }

    private void resolveVerticalCollisions(List<Block> blocks) {
        float moveY = velocity.y + knockBackVelocity.y;
        for (Block block : blocks) {
            if (!block.getType().blocksVertical()) continue;
            if (!hitBox.overlaps(block.getBound())) continue;
            if(block.getType()== BlockType.GROUND)
            {
                if (moveY <= 0) {
                    position.y = block.getBound().y + block.getBound().height-GROUND_SNAP_EPSILON;
                    velocity.y = 0;
                    setOnGround(true);
                } else if (moveY > 0 && block.getType()!=BlockType.GROUND) {
                    position.y = block.getBound().y - hitBox.height;
                    velocity.y = 0;
                }
            }
            else if (block.getType() == BlockType.CEIL){
                if(moveY > 0){
                    velocity.y= 0 ;
                }
            }
            updateHitBox();
        }
    }

    public void resolveHazardCollisions(ArrayList<Block> blocks) {
        for(Block block : blocks) {
            if(this.hitBox.overlaps(block.getBound())&&block.getType()== BlockType.SPIKE){
                this.hazardReact();
                break;
            }
        }
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


    public Vector2 getSpawnPoint() {
        return spawnPoint;
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

    public void setSpawnPoint(Vector2 spawnPoint) {
        this.spawnPoint = spawnPoint;
    }
}
