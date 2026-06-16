package com.Ap.HollowKnight.model.player;

import com.Ap.HollowKnight.model.game.FacingDirection;
import com.Ap.HollowKnight.model.game.PhysicalPart;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import java.util.Map;

public class Knight extends PhysicalPart {

    private Skin skin;
    private PlayerCondition playerCondition = PlayerCondition.IDLE;
    Map<PlayerCondition, Animation<TextureRegion>> animations;
    private int currentMasks= 5;
    private int currentSoul = 99;
    private float dashTimer;
    private float focusTimer;
    private boolean isDashing=false;
    private boolean isInvincible=false;
    private float dashCooldownTime;
    private float focusTime;

    private static final float DASH_SPPED = 200.0f;
    private static final float MAX_VELOCITY = 500.0f;
    private static final float DASH_DURATION  = 0.35f;  // seconds
    private static final float DASH_COOLDOWN  = 0.6f;
    private static final float FOCUS_DURATION = 1.5f;
    private static final int   FOCUS_COST     = 33;
    public Knight(Vector2 position,Rectangle hitBox) {
        super(position,MAX_VELOCITY,hitBox);
    }

    @Override
    public void update(float delta, TiledMapTileLayer layer) {
        if(isDashing) {
            dashTimer -= delta;
            if(dashTimer <= 0) {
                this.isDashing = false;
                dashCooldownTime =DASH_COOLDOWN;
                setGravityIncluded(true);
                this.playerCondition = PlayerCondition.IDLE;
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
            focusTimer -= delta;
            if (focusTimer <= 0) {
                focusTime = 0.0f;
                if (currentSoul >= FOCUS_COST) {
                    currentSoul -= FOCUS_COST;
                    currentMasks = Math.min(currentMasks + 1, 5);
                }
                playerCondition = PlayerCondition.IDLE;
            }
        }
        else focusTime = 0.0f;
        if (!isOnGround() && getVelocity().y < 0 && playerCondition != PlayerCondition.DASHING) {
            playerCondition = PlayerCondition.FALLING;
        }
        applyPhysics(delta,layer);


    }

    @Override
    public void takeDamage(int amount) {
        currentMasks = Math.max(0,currentMasks - amount);
    }
    public void jump(){
        if(!this.isOnGround()&& playerCondition==PlayerCondition.MONARCHING){
            return;
        }
        if(!this.isOnGround()){
            playerCondition = PlayerCondition.MONARCHING;
        }
        if(this.isOnGround()){
            setOnGround(false);
        }
        playerCondition = PlayerCondition.JUMPING;
        this.getVelocity().y = 400.0f;
    }

    public void cutJump(){
        if (!isOnGround() && getVelocity().y > 0 && !isDashing) {
            getVelocity().y *= 0.5f;
        }
    }
    public void dash(){
        if(!this.isCooldown()&&!this.isDashing){
            float direction = (this.getFacingDirection()== FacingDirection.RIGHT)? 1.0f:-1.0f;
            playerCondition = PlayerCondition.DASHING;
            this.getVelocity().x = direction * DASH_SPPED ;
            this.getVelocity().y=0.0f;
            this.setGravityIncluded(false);
            this.isDashing=true;
            this.dashTimer =DASH_DURATION;

        }
    }
    public void move(){
        if(!this.isDashing){
            float direction =  (this.getFacingDirection()== FacingDirection.RIGHT)? 1.0f:-1.0f;
            getVelocity().x = direction * MAX_VELOCITY;
            if(this.isOnGround())
               playerCondition = PlayerCondition.MOVING;

        }
    }
    public void stop(){
        if(!this.isDashing){
            this.getVelocity().x = 0.0f;
            if(this.isOnGround())
                playerCondition = PlayerCondition.IDLE;
        }

    }

    public void focus(){
        if(this.isOnGround()){
            this.getVelocity().x = 0.0f;
            playerCondition = PlayerCondition.FOCUSING;
        }
    }

    public float getFocusTime() {
        return focusTime;
    }

    public void setFocusTime(float focusTime) {
        this.focusTime = focusTime;
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

    public Map<PlayerCondition, Animation<TextureRegion>> getAnimations() {
        return animations;
    }

    public void setAnimations(Map<PlayerCondition, Animation<TextureRegion>> animations) {
        this.animations = animations;
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
}
