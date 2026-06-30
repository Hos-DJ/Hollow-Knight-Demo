package com.Ap.HollowKnight.model.player;

import com.Ap.HollowKnight.model.AttackDirection;
import com.Ap.HollowKnight.model.Nail;
import com.Ap.HollowKnight.model.game.PhysicalPart;
import com.Ap.HollowKnight.model.map.Block;
import com.Ap.HollowKnight.model.map.BlockType;
import com.Ap.HollowKnight.model.spells.SpellManager;
import com.Ap.HollowKnight.view.PlayerHUD;
import com.Ap.HollowKnight.view.animations.EffectAnimationType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

import static com.Ap.HollowKnight.model.game.FacingDirection.RIGHT;

public class Knight extends PhysicalPart {
    public static final int MAX_MASKS = 5;
    private static final float INVINCIBILITY_DURATION = 4.0f;
    private static final float DAMAGE_KNOCKBACK_SPEED = 250f;
    private static final float JUMP_VELOCITY = 450.0f;
    private static final float ATTACK_DURATION = 0.15f;
    private static final float ATTACK_COOLDOWN = 0.25f;
    private static final float NAIL_LENGTH = 70f;
    private static final float NAIL_THICKNESS = 90f;
    private static final int MAX_SOUL = 99;
    private static final int SOUL_PER_HIT = 11;
    private static final float POGO_BOUNCE_SPEED = 350f;
    private static final float DASH_SPEED = 600.0f;
    private static final float MAX_VELOCITY = 300.0f;
    private static final float DASH_DURATION = 0.35f;
    private static final float DASH_COOLDOWN = 0.6f;
    private static final float FOCUS_DURATION = 1.5f;
    private static final int FOCUS_COST = 33;
    private static final float SPAWN_DELAY = 1f;
    private static final float CAST_DELAY = 0.5f;

    private int currentMasks = 5;
    private int currentSoul = 99;

    private PlayerCondition playerCondition = PlayerCondition.IDLE;
    private AttackDirection currentAttackDirection = AttackDirection.RIGHT;
    private boolean isDashing = false;
    private boolean isInvincible = false;
    private boolean canMonarch = true;

    private float dashTimer = 0f;
    private float focusTimer = 0f;
    private float invincibleTimer = 0f;
    private float dashCooldownTime;
    private float attackTimer = 0f;
    private float attackCooldownTimer = 0f;
    private float respawnTimer = 0f;
    private float castDurationTimer = 0f;

    private Vector2 lastCheckpoint;
    private Nail nail;
    private ArrayList<Vector2> safeSpots;
    private PlayerHUD hud;
    private final SpellManager spellManager = new SpellManager();

    public Knight(Vector2 position, Rectangle hitBox, Vector2 spawnPoint, ArrayList<Vector2> safeSpots , PlayerHUD hud) {
        super(position, hitBox, spawnPoint);
        Rectangle nailHitBox = new Rectangle(position.x, position.y, NAIL_LENGTH, NAIL_THICKNESS);
        this.nail = new Nail(new Vector2(position), nailHitBox, new Vector2(spawnPoint), EffectAnimationType.NAIL_SLASH);
        this.safeSpots = safeSpots;
        this.lastCheckpoint = new Vector2(spawnPoint);
        this.hud = hud;

    }

    @Override
    public void update(float delta, ArrayList<Block> blocks) {
        if (isDashing) {
            dashTimer -= delta;
            if (dashTimer <= 0) {
                isDashing = false;
                dashCooldownTime = DASH_COOLDOWN;
                setGravityIncluded(true);
                playerCondition = isOnGround() ? PlayerCondition.IDLE : PlayerCondition.FALLING;
                setCooldown(true);
                getVelocity().x = 0;
            }
        }

        if (castDurationTimer > 0f) {
            castDurationTimer -= delta;
            if (castDurationTimer <= 0) {
                castDurationTimer = 0f;
                playerCondition = isOnGround() ? PlayerCondition.IDLE : PlayerCondition.FALLING;
                setGravityIncluded(true);
            }
        }

        if (isCooldown()) {
            dashCooldownTime -= delta;
            if (dashCooldownTime <= 0) {
                setCooldown(false);
            }
        }

        if (playerCondition == PlayerCondition.FOCUSING) {
            focusTimer += delta;
            if (focusTimer >= FOCUS_DURATION) {
                focusTimer = 0;
                if (currentSoul >= FOCUS_COST) {
                    currentSoul -= FOCUS_COST;
                    hud.refillMask();
                    currentMasks = Math.min(currentMasks + 1, MAX_MASKS);
                }
                playerCondition = PlayerCondition.IDLE;
            }
        } else {
            focusTimer = 0.0f;
        }

        if (!isOnGround() && getVelocity().y < 0 && playerCondition != PlayerCondition.DASHING) {
            playerCondition = PlayerCondition.FALLING;
        }

        if (isAttacking()) {
            attackTimer -= delta;
            if (attackTimer <= 0) {
                setAttacking(false);
                attackCooldownTimer = ATTACK_COOLDOWN;
                playerCondition = isOnGround() ? PlayerCondition.IDLE : PlayerCondition.FALLING;
            }
        }

        if (attackCooldownTimer >= 0) {
            attackCooldownTimer -= delta;
        }

        if (isInvincible()) {
            invincibleTimer -= delta;
            if (invincibleTimer <= 0) {
                setInvincible(false);
            }
        }

        if (playerCondition == PlayerCondition.TAKING_DAMAGE) {
            playerCondition = isOnGround() ? PlayerCondition.IDLE : PlayerCondition.FALLING;
        }
        updateLastSafeSpot();
        updateNailHitBox();
        spellManager.update(delta, blocks);

        if (!isOnGround() && getVelocity().y < 0
            && playerCondition != PlayerCondition.DASHING
            && playerCondition != PlayerCondition.ATTACKING) {
            playerCondition = PlayerCondition.FALLING;
        }

        if (isOnGround()) {
            canMonarch = true;
            if (playerCondition == PlayerCondition.FALLING
                || playerCondition == PlayerCondition.JUMPING
                || playerCondition == PlayerCondition.MONARCHING) {
                playerCondition = (Math.abs(getVelocity().x) > 0.1f) ? PlayerCondition.MOVING : PlayerCondition.IDLE;
            }
        }

        for (Block block : blocks) {
            if (block.getType() != BlockType.SPIKE) continue;
            if (nail.getHitBox().overlaps(block.getBound()) && isAttacking() && currentAttackDirection == AttackDirection.DOWN) {
                pogoBounce();
            }
        }

        applyPhysics(delta, blocks);
    }

    @Override
    public void takeDamage(int amount) {
        if (isInvincible()) return;
        currentMasks = Math.max(0, currentMasks - amount);
        if (playerCondition == PlayerCondition.FOCUSING) {
            cancelFocus();
        }
        if (currentMasks <= 0) {
            die();
            return;
        }
        hud.breakMask(1);
        setInvincible(true);
        invincibleTimer = INVINCIBILITY_DURATION;
        playerCondition = PlayerCondition.TAKING_DAMAGE;
    }

    @Override
    public void hazardReact() {
        currentMasks = Math.max(0, currentMasks - 1);
        if (currentMasks == 0) {
            die();
            return;
        }
        setPlayerCondition(PlayerCondition.TAKING_DAMAGE);
        hud.breakMask(1);
        respawn();
    }

    public void castVengeful() {
        if (castDurationTimer > 0f || playerCondition == PlayerCondition.DASHING || playerCondition == PlayerCondition.FOCUSING)
            return;

        if(currentSoul >=FOCUS_COST){
            boolean canCast = spellManager.castVengefulSpirit(getHitBox(), getFacingDirection());
            if (canCast) {
                currentSoul = Math.max(currentSoul - FOCUS_COST, 0);
                playerCondition = PlayerCondition.VENGEFUL_SPIRIT;
                setVelocity(Vector2.Zero);
                setGravityIncluded(false);
                castDurationTimer = CAST_DELAY;
            }
        }
    }

    public void castHowling(){
        if (castDurationTimer > 0f || playerCondition == PlayerCondition.DASHING || playerCondition == PlayerCondition.FOCUSING)
            return;
        if(currentSoul >= FOCUS_COST){
            boolean canCast = spellManager.castHowlingWrath(getHitBox(), getFacingDirection());
            if (canCast) {
                currentSoul = Math.max(currentSoul - FOCUS_COST, 0);
                setPlayerCondition(PlayerCondition.HOWLING_WRATH);
                setVelocity(Vector2.Zero);
                setGravityIncluded(false);
                castDurationTimer = CAST_DELAY;
            }
        }
    }

    private void updateLastSafeSpot(){
        for (Vector2 sp : safeSpots){
            if (sp.dst(getPosition())<50f){
                lastCheckpoint.set(sp);
                return;
            }
        }
    }

    public void jump() {
        if (playerCondition == PlayerCondition.FOCUSING) return;

        if (isOnGround()) {
            setOnGround(false);
            playerCondition = PlayerCondition.JUMPING;
            getVelocity().y = JUMP_VELOCITY;
        } else if (canMonarch) {
            playerCondition = PlayerCondition.MONARCHING;
            getVelocity().y = JUMP_VELOCITY;
            canMonarch = false;
        }
    }

    public void cutJump() {
        if (!isOnGround() && getVelocity().y > 0 && !isDashing) {
            getVelocity().y *= 0.5f;
        }
    }

    public void dash() {
        if (!isCooldown() && !isDashing && playerCondition != PlayerCondition.FOCUSING) {
            float direction = (getFacingDirection() == RIGHT) ? 1.0f : -1.0f;
            playerCondition = PlayerCondition.DASHING;
            getVelocity().x = direction * DASH_SPEED;
            getVelocity().y = 0.0f;
            setGravityIncluded(false);
            isDashing = true;
            dashTimer = DASH_DURATION;
        }
    }

    public void move() {
        if (!isDashing && playerCondition != PlayerCondition.FOCUSING && !(castDurationTimer > 0f)) {
            float direction = (getFacingDirection() == RIGHT) ? 1.0f : -1.0f;
            getVelocity().x = direction * MAX_VELOCITY;
            if (isOnGround()) {
                playerCondition = PlayerCondition.MOVING;
            }
        }
    }

    public void stop() {
        if (!isDashing && playerCondition != PlayerCondition.FOCUSING && !isAttacking()) {
            getVelocity().x = 0.0f;
            if (isOnGround()) {
                playerCondition = PlayerCondition.IDLE;
            }
        }
    }

    public void focus() {
        if (isOnGround()) {
            getVelocity().x = 0.0f;
            playerCondition = PlayerCondition.FOCUSING;
            focusTimer = 0;
        }
    }

    public boolean attack(AttackDirection direction) {
        if (isAttacking() || attackCooldownTimer > 0 || isDashing || playerCondition == PlayerCondition.FOCUSING) {
            return false ;
        }
        currentAttackDirection = direction;
        setAttacking(true);
        attackTimer = ATTACK_DURATION;
        playerCondition = PlayerCondition.ATTACKING;
        return true;
    }

    public void die() {
        lastCheckpoint.set(getSpawnPoint());
        setPosition(new Vector2(getSpawnPoint().x, getSpawnPoint().y));
        setCurrentMasks(MAX_MASKS);
        setCurrentSoul(0);
        getVelocity().setZero();
        getKnockBackVelocity().setZero();
        playerCondition = PlayerCondition.IDLE;
        setInvincible(false);
        hud.resetMasks();
        dashTimer = 0;
        focusTimer = 0;
        attackTimer = 0;
        attackCooldownTimer = 0;
        dashCooldownTime = 0;
        invincibleTimer = 0;
//        wallJumpCooldownTimer = 0
    }

    private void updateNailHitBox() {
        float nailX, nailY, width, height;
        switch (currentAttackDirection) {
            case UP -> {
                width = NAIL_THICKNESS;
                height = NAIL_LENGTH;
                nailX = getHitBox().x + getHitBox().width / 2f - width / 2f;
                nailY = getHitBox().y + getHitBox().height;
            }
            case DOWN -> {
                width = NAIL_THICKNESS;
                height = NAIL_LENGTH;
                nailX = getHitBox().x + getHitBox().width / 2f - width / 2f;
                nailY = getHitBox().y - height;
            }
            case LEFT -> {
                width = NAIL_LENGTH;
                height = NAIL_THICKNESS;
                nailX = getHitBox().x - width;
                nailY = getHitBox().y + getHitBox().height / 2f - height / 2f;
            }
            default -> {
                width = NAIL_LENGTH;
                height = NAIL_THICKNESS;
                nailX = getHitBox().x + getHitBox().width;
                nailY = getHitBox().y + getHitBox().height / 2f - height / 2f;
            }
        }
        nail.getHitBox().setSize(width, height);
        nail.setPosition(new Vector2(nailX, nailY));
        nail.updateHitBox();
        nail.setAttacking(isAttacking());
    }

    public void pogoBounce() {
        getVelocity().y = POGO_BOUNCE_SPEED;
        setCooldown(false);
        setPlayerCondition(PlayerCondition.JUMPING);
        dashCooldownTime = 0.0f;
        canMonarch = true;
    }

    public void gainSoul() {
        currentSoul = Math.min(currentSoul + SOUL_PER_HIT, MAX_SOUL);
    }

    public void cancelFocus() {
        if (playerCondition == PlayerCondition.FOCUSING) {
            focusTimer = 0f;
            playerCondition = isOnGround() ? PlayerCondition.IDLE : PlayerCondition.FALLING;
        }
    }

    public void respawn() {
        setPosition(new Vector2(lastCheckpoint.x, lastCheckpoint.y));
        getVelocity().setZero();
        getKnockBackVelocity().setZero();
        updateHitBox();
        setInvincible(true);
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

    public Nail getNail() {
        return nail;
    }

    public AttackDirection getCurrentAttackDirection() {
        return currentAttackDirection;
    }

    public SpellManager getSpellManager() {
        return spellManager;
    }
}
