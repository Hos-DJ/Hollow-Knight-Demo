package com.Ap.HollowKnight.model.player;

import com.Ap.HollowKnight.controller.events.GameEvent;
import com.Ap.HollowKnight.controller.events.GameEventMessenger;
import com.Ap.HollowKnight.model.AttackDirection;
import com.Ap.HollowKnight.model.Nail;
import com.Ap.HollowKnight.model.game.FacingDirection;
import com.Ap.HollowKnight.model.game.PhysicalPart;
import com.Ap.HollowKnight.model.map.Block;
import com.Ap.HollowKnight.model.map.BlockType;
import com.Ap.HollowKnight.model.spells.SpellManager;
import com.Ap.HollowKnight.view.PlayerHUD;
import com.Ap.HollowKnight.view.animations.EffectAnimationType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

import static com.Ap.HollowKnight.model.game.FacingDirection.LEFT;
import static com.Ap.HollowKnight.model.game.FacingDirection.RIGHT;

public class Knight extends PhysicalPart {
    public final int MAX_MASKS = 5;
    private final float INVINCIBILITY_DURATION = 1.2f;
    private final float JUMP_VELOCITY = 450.0f;
    private final float ATTACK_DURATION = 0.15f;
    private final float ATTACK_COOLDOWN = 0.3f;
    private final float NAIL_LENGTH = 70f;
    private final float NAIL_THICKNESS = 90f;
    private final int MAX_SOUL = 99;
    private final int SOUL_PER_HIT = 11;
    private final float POGO_BOUNCE_SPEED = 350f;
    private final float DASH_SPEED = 600.0f;
    private final float WALL_JUMP_HORIZONTAL_SPEED = 120;
    private final float WALL_JUMP_VERTICAL_SPEED = 500;
    private final float MAX_VELOCITY = 300.0f;
    private final float DASH_DURATION = 0.35f;
    private final float DASH_COOLDOWN = 0.6f;
    private final float FOCUS_DURATION = 1.5f;
    private final int FOCUS_COST = 33;
    private final float SPAWN_DELAY = 1f;
    private final float CAST_DELAY = 0.5f;
    private final float DAMAGE_KNOCKBACK_SPEED = 500.0f;
    private final int BASE_SPELL_DAMAGE = 15;
    private  final int  NAIL_DAMAGE = 11;
    private final int DAMAGE_MULTIPLIER = 2;
    private final float DASH_COOLDOWN_MULTIPLIER = 0.5f;
    private final float SPELL_DAMAGE_MULTIPLIER = 1.5f;
    private final float KNOCKBACK_DAMAGE_MULTIPLIER = 2f;
    private final int SOUL_PER_HIT_MULTIPLIER = 2;
    private final float DASH_SPEED_MULTIPLIER = 1.2f;
    private final float ATTACK_COOLDOWN_MULTIPLIER = 0.5f;
    private final float FOCUS_DURATION_MULTIPLIER = 0.5f;

    private int currentMasks = 5;
    private int currentSoul = 99;

    GameEventMessenger messenger = GameEventMessenger.getInstance();
    // Current
    private float currentDashCooldown = DASH_COOLDOWN;
    private int currentNailDamage = NAIL_DAMAGE;
    private int currentSoulPerHit =  SOUL_PER_HIT;
    private float currentKnockBack = DAMAGE_KNOCKBACK_SPEED;
    private float currentAttackCooldown = ATTACK_COOLDOWN;
    private float currentFocusDuration = FOCUS_DURATION;
    private float currentDashSpeed = DASH_SPEED;
    private int currentSpellDamage = BASE_SPELL_DAMAGE;

    private boolean hasSharpShadow = false;
    private boolean hasVoidHeart = false;

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
    private float wallJumpTimer = 0f;

    private Vector2 lastCheckpoint;
    private Nail nail;
    private ArrayList<Vector2> safeSpots;
    private PlayerHUD hud;
    private final SpellManager spellManager = new SpellManager();
    private final CharmManager charmManager = new CharmManager();

    //cheats
    private boolean immortal = false;
    private boolean spectator = false;
    private boolean deadlyNail =false;

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
                dashCooldownTime = currentDashCooldown;
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
            if (focusTimer >= currentFocusDuration) {
                focusTimer = 0;
                if (currentSoul >= FOCUS_COST) {
                    currentSoul -= FOCUS_COST;
                    hud.refillMask();
                    currentMasks = Math.min(currentMasks + 1, MAX_MASKS);
                }
                messenger.dispatch(GameEvent.PLAYER_FOCUS_END,null);
                playerCondition = PlayerCondition.IDLE;
            }
        } else {
            focusTimer = 0.0f;
        }

        if (!isOnGround() && getVelocity().y < 0 && playerCondition != PlayerCondition.DASHING&&playerCondition!=PlayerCondition.JUMPING) {
            playerCondition = PlayerCondition.FALLING;
        }

        if (isAttacking()) {
            attackTimer -= delta;
            if (attackTimer <= 0) {
                setAttacking(false);
                attackCooldownTimer = currentAttackCooldown;
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
        if(isOnGround()&&Math.abs(getVelocity().x)>0.02f){
            messenger.dispatch(GameEvent.PLAYER_STARTED_WALKING,null);

        }else{
            messenger.dispatch(GameEvent.PLAYER_ENDED_WALKING,null);

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
//        System.out.println(getPosition().x + " , "+ getPosition().y);

        if (!isOnGround() && isTouchingWall() && getVelocity().y < 0) {
            playerCondition = PlayerCondition.WALL_SLIDING;
            messenger.dispatch(GameEvent.PLAYER_STARTED_WALL_SLIDING,null);

            if (getVelocity().y < -150.0f) {
                getVelocity().y = -150.0f;
            }
            canMonarch = true;
            setFacingDirection(getWallDirection() == 1 ? RIGHT : LEFT);
        }
        if (wallJumpTimer > 0) {
            wallJumpTimer -= delta;
        }
        if(!isTouchingWall()) {
            messenger.dispatch(GameEvent.PLAYER_ENDED_WALL_SLIDING,null);
        }
    }

    @Override
    public void takeDamage(int amount) {
        if (isInvincible()||immortal) return;
        currentMasks = Math.max(0, currentMasks - amount);
        if (playerCondition == PlayerCondition.FOCUSING) {
            cancelFocus();
        }
        if (currentMasks <= 0) {
            die();
            return;
        }
        hud.breakMask(amount);
        setInvincible(true);
        if(amount == 1 ){
            messenger.dispatch(GameEvent.PLAYER_HURT,null);
        }else if(amount == 2){
            messenger.dispatch(GameEvent.PLAYER_DOUBLE_HURT,null);
        }
        invincibleTimer = INVINCIBILITY_DURATION;
        playerCondition = PlayerCondition.TAKING_DAMAGE;
    }

    @Override
    public void hazardReact() {
        if(immortal||isInvincible)return;
        currentMasks = Math.max(0, currentMasks - 1);
        if (currentMasks == 0) {
            die();
            return;
        }
        setPlayerCondition(PlayerCondition.TAKING_DAMAGE);
        hud.breakMask(1);
        messenger.dispatch(GameEvent.PLAYER_HURT,null);
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
                messenger.dispatch(GameEvent.PLAYER_VENGEFUL,null);
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
                messenger.dispatch(GameEvent.PLAYER_HOWLING,null);
                setVelocity(Vector2.Zero);
                setGravityIncluded(false);
                castDurationTimer = CAST_DELAY;
            }
        }
    }

    private void updateLastSafeSpot(){
        for (Vector2 sp : safeSpots){
            if (sp.dst(getPosition())<200){
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
        }else if (isTouchingWall() && !isOnGround()){
            playerCondition = PlayerCondition.WALL_JUMPING;
            getVelocity().y = WALL_JUMP_VERTICAL_SPEED;
            getVelocity().x = -getWallDirection() * WALL_JUMP_HORIZONTAL_SPEED;
            setFacingDirection(getWallDirection() == 1 ? LEFT : FacingDirection.RIGHT);
            wallJumpTimer = 0.25f;
        }

        else if (canMonarch) {
            playerCondition = PlayerCondition.MONARCHING;
            getVelocity().y = JUMP_VELOCITY;
            messenger.dispatch(GameEvent.PLAYER_MONARCH_WINGS,null);
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
            messenger.dispatch(GameEvent.PLAYER_DASH,null);
            getVelocity().x = direction * currentDashSpeed;
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
            messenger.dispatch(GameEvent.PLAYER_FOCUS_START,null);
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
        messenger.dispatch(GameEvent.PLAYER_ATTACKING,null);
        return true;
    }

    public void die() {
        lastCheckpoint.set(getSpawnPoint());
        messenger.dispatch(GameEvent.PLAYER_DEATH,null);
        setPosition(new Vector2(getSpawnPoint().x, getSpawnPoint().y));
        updateHitBox();
        setCurrentMasks(MAX_MASKS);
        setCurrentSoul(0);
        getVelocity().setZero();
        getKnockBackVelocity().setZero();
        playerCondition = PlayerCondition.IDLE;
        setInvincible(false);
        hud.resetMasks();
        stop();
        dashTimer = 0;
        focusTimer = 0;
        attackTimer = 0;
        attackCooldownTimer = 0;
        dashCooldownTime = 0;
        invincibleTimer = 0;
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
        messenger.dispatch(GameEvent.POGO_SPIKE,null);
        dashCooldownTime = 0.0f;
        canMonarch = true;
    }

    public void gainSoul() {
        currentSoul = Math.min(currentSoul + currentSoulPerHit, MAX_SOUL);
        messenger.dispatch(GameEvent.PLAYER_SOUL_GAIN,null);
    }

    public void gainFullSoul(){
        currentSoul = MAX_SOUL;
        messenger.dispatch(GameEvent.PLAYER_SOUL_GAIN,null);
    }

    public void gainMask(){
        if(currentMasks != MAX_MASKS){
            currentMasks = currentMasks + 1;
            hud.refillMask();
        }
    }

    public void goToBoss(){
        getPosition().x = 17750;
        getPosition().y =6690;
    }

    public void cancelFocus() {
        if (playerCondition == PlayerCondition.FOCUSING) {
            focusTimer = 0f;
            GameEventMessenger.getInstance().dispatch(GameEvent.PLAYER_FOCUS_END,null);

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

    public void changeToSpectator(boolean toggle) {
        spectator = toggle;
        if(spectator){
            setGravityIncluded(false);
        }else{
            setGravityIncluded(true);
        }
    }

    public void oneShotNail(boolean toggle){
        deadlyNail = toggle;
        if(deadlyNail){
            currentNailDamage = 500;
        }else{
            currentNailDamage = 11;
        }
    }

    // ---------- Getters for CombatController and SpellManager ----------
    public float getCurrentKnockBack() { return currentKnockBack; }
    public int getCurrentNailDamage() { return currentNailDamage; }
    public int getCurrentSoulPerHit() { return currentSoulPerHit; }
    public float getCurrentAttackCooldown() { return currentAttackCooldown; }
    public float getCurrentFocusDuration() { return currentFocusDuration; }
    public float getCurrentDashCooldown() { return currentDashCooldown; }

    public float getCurrentDashSpeed() { return currentDashSpeed; }
    public void setCurrentDashSpeed(float currentDashSpeed) { this.currentDashSpeed = currentDashSpeed; }

    public boolean hasSharpShadow() { return hasSharpShadow; }
    public void setHasSharpShadow(boolean hasSharpShadow) { this.hasSharpShadow = hasSharpShadow; }

    public boolean hasVoidHeart() { return hasVoidHeart; }
    public void setHasVoidHeart(boolean hasVoidHeart) { this.hasVoidHeart = hasVoidHeart; }

    // ---------- Other Existing Setters/Getters ----------

    public boolean isInvincible() { return isInvincible; }
    public void setInvincible(boolean invincible) { isInvincible = invincible; }
    public boolean isDashing() { return isDashing; }
    public float getWallJumpTimer() {
        return wallJumpTimer;
    }
    public int getCurrentSoul() { return currentSoul; }
    public void setCurrentSoul(int currentSoul) { this.currentSoul = currentSoul; }
    public int getCurrentMasks() { return currentMasks; }
    public void setCurrentMasks(int currentMasks) { this.currentMasks = currentMasks; }
    public PlayerCondition getPlayerCondition() { return playerCondition; }
    public void setPlayerCondition(PlayerCondition playerCondition) { this.playerCondition = playerCondition; }
    public Nail getNail() { return nail; }
    public AttackDirection getCurrentAttackDirection() { return currentAttackDirection; }
    public SpellManager getSpellManager() { return spellManager; }

    public CharmManager getCharmManager() {
        return charmManager;
    }

    public int getCurrentSpellDamage() {
        return currentSpellDamage;
    }

    public boolean isImmortal() {
        return immortal;
    }

    public void setCurrentDashCooldown(float currentDashCooldown) { this.currentDashCooldown = currentDashCooldown; }
    public void setCurrentNailDamage(int currentNailDamage) { this.currentNailDamage = currentNailDamage; }
    public void setCurrentSoulPerHit(int currentSoulPerHit) { this.currentSoulPerHit = currentSoulPerHit; }
    public void setCurrentKnockBack(float currentKnockBack) { this.currentKnockBack = currentKnockBack; }
    public void setCurrentAttackCooldown(float currentAttackCooldown) { this.currentAttackCooldown = currentAttackCooldown; }
    public void setCurrentFocusDuration(float currentFocusDuration) { this.currentFocusDuration = currentFocusDuration; }
    public void setCurrentSpellDamage(int currentSpellDamage) {this.currentSpellDamage = currentSpellDamage;}

    public void setImmortal(boolean immortal) {
        this.immortal = immortal;
    }

    public float getATTACK_COOLDOWN() { return ATTACK_COOLDOWN; }
    public int getSOUL_PER_HIT() { return SOUL_PER_HIT; }
    public float getDASH_SPEED() { return DASH_SPEED; }
    public float getDASH_COOLDOWN() { return DASH_COOLDOWN; }
    public float getFOCUS_DURATION() { return FOCUS_DURATION; }
    public float getDAMAGE_KNOCKBACK_SPEED() { return DAMAGE_KNOCKBACK_SPEED; }
    public int getNAIL_DAMAGE() { return NAIL_DAMAGE; }
    public int getDAMAGE_MULTIPLIER() { return DAMAGE_MULTIPLIER; }
    public float getDASH_COOLDOWN_MULTIPLIER() { return DASH_COOLDOWN_MULTIPLIER; }
    public float getSPELL_DAMAGE_MULTIPLIER() { return SPELL_DAMAGE_MULTIPLIER; }
    public float getKNOCKBACK_DAMAGE_MULTIPLIER() { return KNOCKBACK_DAMAGE_MULTIPLIER; }
    public int getSOUL_PER_HIT_MULTIPLIER() { return SOUL_PER_HIT_MULTIPLIER; }
    public float getDASH_SPEED_MULTIPLIER() { return DASH_SPEED_MULTIPLIER; }
    public float getATTACK_COOLDOWN_MULTIPLIER() { return ATTACK_COOLDOWN_MULTIPLIER; }
    public float getFOCUS_DURATION_MULTIPLIER() { return FOCUS_DURATION_MULTIPLIER; }
    public int getBASE_SPELL_DAMAGE() {return BASE_SPELL_DAMAGE;}


    public boolean isDeadlyNail() {
        return deadlyNail;
    }

    public void setDeadlyNail(boolean deadlyNail) {
        this.deadlyNail = deadlyNail;
    }

    public boolean isSpectator() {
        return spectator;
    }

    public void setSpectator(boolean spectator) {
        this.spectator = spectator;
    }
}
