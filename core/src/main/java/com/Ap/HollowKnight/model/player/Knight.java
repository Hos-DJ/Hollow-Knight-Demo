package com.Ap.HollowKnight.model.player;

import com.Ap.HollowKnight.model.AttackDirection;
import com.Ap.HollowKnight.model.Nail;
import com.Ap.HollowKnight.model.game.PhysicalPart;
import com.Ap.HollowKnight.model.map.Block;
import com.Ap.HollowKnight.model.map.BlockType;
import com.Ap.HollowKnight.view.EffectAnimationType;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import java.util.ArrayList;

import static com.Ap.HollowKnight.model.game.FacingDirection.RIGHT;

public class Knight extends PhysicalPart {
    private Skin skin;
    private PlayerCondition playerCondition = PlayerCondition.IDLE;
    private int currentMasks= 5;
    private int currentSoul = 99;
    private float dashTimer;
    private float focusTimer;
    private boolean isDashing=false;
    private boolean isInvincible=false;
    private float invincibleTimer =0 ;
    private ArrayList <Vector2> safeSpots;
    private boolean canMonarch = true;
    private float dashCooldownTime;
    private Nail nail;
    private AttackDirection currentAttackDirection=AttackDirection.RIGHT;
    private float attackTimer = 0f;
    private float attackCooldownTimer = 0f;
    private float respawnTimer = 0f;

    public static final int MAX_MASKS = 5 ;
    private static final float INVINCIBILITY_DURATION = 4.0f;
    private static final float DAMAGE_KNOCKBACK_SPEED  = 250f;
    private static final float ATTACK_DURATION   = 0.15f;
    private static final float ATTACK_COOLDOWN   = 0.25f;
    private static final float NAIL_LENGTH       = 70f;
    private static final float NAIL_THICKNESS    =90f;
    private static final int   MAX_SOUL          = 99;
    private static final int   SOUL_PER_HIT      = 11;
    private static final float POGO_BOUNCE_SPEED = 350f;
    private static final float DASH_SPEED = 600.0f;
    private static final float MAX_VELOCITY = 300.0f;
    private static final float DASH_DURATION  = 0.35f;  // seconds
    private static final float DASH_COOLDOWN  = 0.6f;
    private static final float FOCUS_DURATION = 1.5f;
    private static final int   FOCUS_COST     = 33;
    private static final float SPAWN_DELAY = 1f;
    public Knight(Vector2 position,Rectangle hitBox,Vector2 spawnPoint , ArrayList <Vector2> safeSpots) {

        super(position, hitBox,spawnPoint);
        Rectangle nailHitBox = new Rectangle (position.x, position.y, NAIL_LENGTH, NAIL_THICKNESS);
        this.nail = new Nail(new Vector2(this.getPosition().x,this.getPosition().y),nailHitBox,new Vector2(spawnPoint.x,spawnPoint.y), EffectAnimationType.NAIL_SLASH);
        this.safeSpots = safeSpots;
    }

    @Override
    public void update(float delta, ArrayList<Block> blocks) {
        if(isDashing) {
            dashTimer -= delta;
            if(dashTimer <= 0) {
                this.isDashing = false;
                dashCooldownTime =DASH_COOLDOWN;
                setGravityIncluded(true);
                this.playerCondition = (this.isOnGround())?PlayerCondition.IDLE : PlayerCondition.FALLING;
                this.setCooldown(true);
                getVelocity().x=0;
            }
        }
        if(isCooldown()){
            dashCooldownTime -=delta;
            if(this.dashCooldownTime<=0) {
                this.setCooldown(false);
            }
        }
        if (this.playerCondition == PlayerCondition.FOCUSING) {
            focusTimer += delta;
            if (focusTimer >= FOCUS_DURATION) {
                focusTimer = 0;
                if (currentSoul >= FOCUS_COST) {
                    currentSoul -= FOCUS_COST;
                    currentMasks = Math.min(currentMasks + 1,MAX_MASKS);
                }
                playerCondition = PlayerCondition.IDLE;
            }
        }
        else focusTimer = 0.0f;
        if (!isOnGround() && getVelocity().y < 0 && playerCondition != PlayerCondition.DASHING) {
            playerCondition = PlayerCondition.FALLING;
        }
        if (this.isAttacking()){
            attackTimer-=delta;
            if(attackTimer<=0){
                setAttacking(false);
                attackCooldownTimer = ATTACK_COOLDOWN;
                playerCondition = isOnGround() ? PlayerCondition.IDLE : PlayerCondition.FALLING;
            }
        }
        if(attackCooldownTimer>=0){
            attackCooldownTimer-=delta;
        }

        if(this.isInvincible()){
            invincibleTimer -= delta;
            if(invincibleTimer<=0){
                setInvincible(false);
            }
        }

        if(this.playerCondition == PlayerCondition.TAKING_DAMAGE){
            playerCondition = isOnGround() ? PlayerCondition.IDLE : PlayerCondition.FALLING;
        }

        updateNailHitBox();

        if (!isOnGround() && getVelocity().y < 0
            && playerCondition != PlayerCondition.DASHING
            && playerCondition != PlayerCondition.ATTACKING) {
            playerCondition = PlayerCondition.FALLING;
        }

        if(isOnGround()){
            canMonarch = true;
            if (playerCondition == PlayerCondition.FALLING
                || playerCondition == PlayerCondition.JUMPING
                || playerCondition == PlayerCondition.MONARCHING) {
                playerCondition = (Math.abs(getVelocity().x) > 0.1f)
                    ? PlayerCondition.MOVING
                    : PlayerCondition.IDLE;
            }

        }

        for(Block block : blocks){
            if(block.getType()!= BlockType.SPIKE) continue;
            if(nail.getHitBox().overlaps(block.getBound())&& isAttacking()&&currentAttackDirection==AttackDirection.DOWN){
                pogoBounce();
            }
        }



        applyPhysics(delta,blocks);
    }

    @Override
    public void takeDamage(int amount) {
        if(isInvincible())return ;
        currentMasks = Math.max(0,currentMasks - amount);
        if (playerCondition == PlayerCondition.FOCUSING) {
            cancelFocus();
        }
        if (this.currentMasks<=0){
            die();
            return;
        }

        setInvincible(true);
        this.invincibleTimer=INVINCIBILITY_DURATION;
        this.playerCondition = PlayerCondition.TAKING_DAMAGE;
    }

    @Override
    public void hazardReact(){
        currentMasks = Math.max(0,currentMasks - 1);
        if(currentMasks<=0){
            die();
            return;
        }
        this.setPlayerCondition(PlayerCondition.TAKING_DAMAGE);
        respawn();
    }

    public void jump(){
        if(playerCondition==PlayerCondition.FOCUSING)return ;

        if(this.isOnGround()){
            setOnGround(false);
            playerCondition = PlayerCondition.JUMPING;
            getVelocity().y = 400.0f;
        }
        else if (this.canMonarch){
            playerCondition = PlayerCondition.MONARCHING;
            getVelocity().y = 400.0f;
            canMonarch = false;
        }
    }

    public void cutJump(){
        if (!isOnGround() && getVelocity().y > 0 && !isDashing) {
            getVelocity().y *= 0.5f;
        }
    }
    public void dash(){
        if(!this.isCooldown()&&!this.isDashing&& playerCondition!=PlayerCondition.FOCUSING){
            float direction = (this.getFacingDirection()== RIGHT)? 1.0f:-1.0f;
            playerCondition = PlayerCondition.DASHING;
            this.getVelocity().x = direction * DASH_SPEED;
            this.getVelocity().y=0.0f;
            this.setGravityIncluded(false);
            this.isDashing=true;
            this.dashTimer =DASH_DURATION;

        }
    }
    public void move(){
        if(!this.isDashing&&playerCondition!=PlayerCondition.FOCUSING){
            float direction =  (this.getFacingDirection()== RIGHT)? 1.0f:-1.0f;
            getVelocity().x = direction * MAX_VELOCITY;
            if(this.isOnGround())
               playerCondition = PlayerCondition.MOVING;

        }
    }
    public void stop(){
        if(!this.isDashing&&this.getPlayerCondition()!=PlayerCondition.FOCUSING&&!this.isAttacking()){
            this.getVelocity().x = 0.0f;
            if(this.isOnGround())
                playerCondition = PlayerCondition.IDLE;
        }

    }

    public void focus(){
        if(this.isOnGround()){
            this.getVelocity().x = 0.0f;
            playerCondition = PlayerCondition.FOCUSING;
            focusTimer = 0;
        }
    }

    public void attack(AttackDirection direction){
        if(isAttacking()||attackCooldownTimer>0||isDashing||playerCondition==PlayerCondition.FOCUSING){
            return;
        }
        currentAttackDirection = direction;
        this.setAttacking(true);
        attackTimer= ATTACK_DURATION;
        playerCondition = PlayerCondition.ATTACKING;
    }

    public void die(){
        this.setPosition(new Vector2(getSpawnPoint().x,getSpawnPoint().y));
        this.setCurrentMasks(MAX_MASKS);
        this.setCurrentSoul(0);
        this.getVelocity().setZero();
        this.getKnockBackVelocity().setZero();
        this.playerCondition = PlayerCondition.IDLE;
        setInvincible(false);
        this.dashTimer = 0 ;
        this.focusTimer=0;
        this.attackTimer = 0;
        this.attackCooldownTimer = 0;
        this.dashCooldownTime = 0;
        this.invincibleTimer = 0;

    }

    private void updateNailHitBox(){
        float nailX , nailY ,width , height;// for setting the rectangle
        switch (currentAttackDirection){
            case UP -> {
                width  = NAIL_THICKNESS;
                height = NAIL_LENGTH;
                nailX = this.getHitBox().x+this.getHitBox().width/2f - width/2f;
                nailY = this.getHitBox().y + this.getHitBox().height;
            }
            case DOWN -> {
                width  = NAIL_THICKNESS;
                height = NAIL_LENGTH;
                nailX =  this.getHitBox().x+this.getHitBox().width/2f - width/2f;
                nailY = this.getHitBox().y - height;
            }
            case LEFT -> {
                width  = NAIL_LENGTH;
                height = NAIL_THICKNESS;
                nailX = this.getHitBox().x - width;
                nailY = this.getHitBox().y +  this.getHitBox().height/2f -  height/2f;
            }
            default -> {
                width  = NAIL_LENGTH;
                height = NAIL_THICKNESS;
                nailX = this.getHitBox().x+this.getHitBox().width;
                nailY = this.getHitBox().y +  this.getHitBox().height/2f -  height/2f;
            }

        }
        nail.getHitBox().setSize(width,height);
        nail.setPosition(new Vector2(nailX,nailY));
        nail.updateHitBox();
        nail.setAttacking(isAttacking());
    }

    public void pogoBounce(){
        this.getVelocity().y = POGO_BOUNCE_SPEED;
        setCooldown(false);
        this.setPlayerCondition(PlayerCondition.JUMPING);
        dashCooldownTime = 0.0f;
        canMonarch = true;

    }

    public void gainSoul(){
        currentSoul = Math.min(currentSoul + SOUL_PER_HIT, MAX_SOUL);

    }

    public void cancelFocus(){
        if (playerCondition == PlayerCondition.FOCUSING) {
            focusTimer = 0f;
            playerCondition = isOnGround() ? PlayerCondition.IDLE : PlayerCondition.FALLING;
        }
    }

    public void respawn(){
        float distance = 1e10f;
        Vector2 respawnPoint=null;
        for(Vector2 safeSpot : safeSpots){
            float dist = distanceBetweenTwoBorders(safeSpot,this.getPosition());
            if(dist<distance){
                respawnPoint = new Vector2(safeSpot.x,safeSpot.y);
                distance = dist ;
            }
        }
        this.getPosition().x= respawnPoint.x;
        this.getPosition().y= respawnPoint.y;

    }

    public float distanceBetweenTwoBorders(Vector2 safeSpot, Vector2 target){
        float y = safeSpot.y - target.y;
        float x = safeSpot.x - target.x;
        return (float) Math.sqrt(x * x + y * y);
    }

    public float getDashCooldownTime() {
        return dashCooldownTime;
    }

    public void setDashCooldownTime(float dashCooldownTime) {
        this.dashCooldownTime = dashCooldownTime;
    }

    public boolean isInvincible() {
        return isInvincible;
    }

    public void setInvincible(boolean invincible) {
        isInvincible = invincible;
    }

    public boolean isDashing() {
        return isDashing;
    }

    public void setDashing(boolean dashing) {
        isDashing = dashing;
    }

    public float getFocusTimer() {
        return focusTimer;
    }

    public void setFocusTimer(float focusTimer) {
        this.focusTimer = focusTimer;
    }

    public float getDashTimer() {
        return dashTimer;
    }

    public void setDashTimer(float dashTimer) {
        this.dashTimer = dashTimer;
    }

    public int getCurrentSoul() {
        return currentSoul;
    }

    public void setCurrentSoul(int currentSoul) {
        this.currentSoul = currentSoul;
    }

    public int getCurrentMasks() {
        return currentMasks;
    }

    public void setCurrentMasks(int currentMasks) {
        this.currentMasks = currentMasks;
    }

    public PlayerCondition getPlayerCondition() {
        return playerCondition;
    }

    public void setPlayerCondition(PlayerCondition playerCondition) {
        this.playerCondition = playerCondition;
    }

    public Skin getSkin() {
        return skin;
    }

    public void setSkin(Skin skin) {
        this.skin = skin;
    }

    public Nail getNail() {
        return nail;
    }

    public AttackDirection getCurrentAttackDirection() {
        return currentAttackDirection;
    }
}
