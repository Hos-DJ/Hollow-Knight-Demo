package com.Ap.HollowKnight.model.zote;

import com.Ap.HollowKnight.model.game.FacingDirection;
import com.Ap.HollowKnight.model.game.PhysicalPart;
import com.Ap.HollowKnight.model.map.Block;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class Zote extends PhysicalPart {
    private static final float MAX_VELOCITY = 300f;
    private static final float RAGE_DURATION = 1.5f;
    private static final float REST_DURATION = 1.5f;
    private static final float WAKING_DURATION = 1f;
    private static final float KNOCK_DURATION = 0.5f;
    private static final float KNOCKBACK_VELOCITY = 800f;
    private static final float INTERACTION_RANGE = 120f;

    private float knockingTimer = 0;
    private float ragingTimer = 0;
    private float restTimer = 0;
    private float wakingTimer = 0f;
    private boolean toRight = false;

    private float chargeDirection = 1f;

    private ZoteState status =ZoteState.IDLE ;
    private Rectangle interactionZone ;
    private boolean playerNearby = false;

    private static int dialogueCounter = 0;
    private String[] dialogues = {"Salam.","Foolish beast... you dulled your mandibles by gnawing on my indestructible body.\n No wonder you were defeated by this weakling!",
                                    "This dreary place... it reminds me of home. How ghastly...",
                                    "Have you seen them? The guards who still patrol this city, even after dying?"};
    private static int preceptsCounter = 0;
    private boolean precpectsTurn = false;
    private String[] precepts = {"Precept One: 'Always Win Your Battles'.",
                                    "Precept Two: 'Never Let Them Laugh at You'.",
                                    "Precept Three: 'Forget Your Past'.",
                                    "Precept Four: 'Strength Beats Strength'."};
    public Zote(Vector2 position, Rectangle hitBox, Vector2 spawnPoint) {
        super(position, hitBox, spawnPoint);
        interactionZone = new Rectangle(
            position.x - INTERACTION_RANGE,
            position.y,
            hitBox.width + INTERACTION_RANGE * 2f,
            hitBox.height
        );
    }

    @Override
    public void update(float delta, ArrayList<Block> blocks) {
        interactionZone.setPosition(getHitBox().x - INTERACTION_RANGE, getHitBox().y);

        if(knockingTimer > 0 ){
            knockingTimer -= delta;
            if (knockingTimer <= 0){
                knockingTimer = 0;
                enrage();
            }
        }
        if(ragingTimer > 0){
            ragingTimer -= delta;
            getVelocity().x = chargeDirection * MAX_VELOCITY;
            if (ragingTimer <= 0){
                rest();
            }
        }
        if(restTimer > 0) {
            getVelocity().x = 0;
            restTimer -= delta;
            if (restTimer <= 0) {
                wakeUp();
            }
        }
        if(wakingTimer > 0){
            wakingTimer -= delta;
            getVelocity().x = 0 ;
            if (wakingTimer <= 0) {
                returnToIDLE();
            }
        }

        System.out.println(this.status);



        applyPhysics(delta, blocks);
    }

    @Override
    public void takeDamage(int amount) {
    }

    @Override
    public void hazardReact() {
        // No damage for zote.
    }

    private void applyFacing(float dir) {
        setFacingDirection(dir > 0 ? FacingDirection.RIGHT : FacingDirection.LEFT);
    }

    public void knock(Vector2 knightPosition) {
        if(status == ZoteState.KNOCKING || status == ZoteState.ENRAGED) return;

        chargeDirection = (getPosition().x - knightPosition.x>=0) ? -1f: 1f;
        float knockDir = -chargeDirection;
        setOnGround(false);
        setKnockBackVelocity(new Vector2(knockDir * KNOCKBACK_VELOCITY, KNOCKBACK_VELOCITY ));
        getVelocity().x = 0;

        knockingTimer = KNOCK_DURATION;
        status = ZoteState.KNOCKING;
        applyFacing(chargeDirection);

    }

    public void enrage(){
        status = ZoteState.ENRAGED;
        ragingTimer = RAGE_DURATION;
    }

    public void trackKnight(Vector2 knightPosition){
        if(status != ZoteState.ENRAGED) return;
        float directionWithKnight = (knightPosition.x > getPosition().x) ? 1f : -1f;

        if (directionWithKnight != chargeDirection) {
            chargeDirection = directionWithKnight;
            applyFacing(chargeDirection);
        }
    }

    public void knockForAttacking(){
        if(status != ZoteState.ENRAGED) return;

        float bounceDir = - chargeDirection;
        setKnockBackVelocity(new Vector2(bounceDir * KNOCKBACK_VELOCITY, KNOCKBACK_VELOCITY));
        chargeDirection = bounceDir;
        applyFacing(chargeDirection);
    }

    public void rest(){
        status = ZoteState.RESTING;
        restTimer = REST_DURATION;
        getVelocity().x = 0;
    }

    public void wakeUp(){
        status =  ZoteState.WAKING;
        wakingTimer = WAKING_DURATION;
        getVelocity().x = 0;
    }

    public void returnToIDLE(){
        status = ZoteState.IDLE;
        getVelocity().x = 0;
    }

    // talking methods:
    public void startTalking(Vector2 knightPosition) {
        if (status == ZoteState.IDLE) status = ZoteState.TALKING;
        float direction = (knightPosition.x > getPosition().x) ? 1f: -1f;
        applyFacing(direction);
    }

    public void stopTalking() {
        if (status == ZoteState.TALKING) status = ZoteState.IDLE;
    }

    public String showDialogue(){
        if (!precpectsTurn) {
            return dialogues[dialogueCounter];
        } else {
            return precepts[preceptsCounter];
        }
    }

    public boolean nextDialogue(){
        boolean success;
        if(!precpectsTurn){
            dialogueCounter++;
            success = dialogueCounter < dialogues.length;
            if (!success) {
                precpectsTurn = true;
            }
        }else{
            preceptsCounter++;
            if(preceptsCounter >= precepts.length){
                preceptsCounter = 0;
            }
            success = false;
        }
        return success;
    }

    public float getKnockingTimer() {
        return knockingTimer;
    }

    public void setKnockingTimer(float knockingTimer) {
        this.knockingTimer = knockingTimer;
    }

    public float getRagingTimer() {
        return ragingTimer;
    }

    public void setRagingTimer(float ragingTimer) {
        this.ragingTimer = ragingTimer;
    }

    public float getRestTimer() {
        return restTimer;
    }

    public void setRestTimer(float restTimer) {
        this.restTimer = restTimer;
    }

    public float getWakingTimer() {
        return wakingTimer;
    }

    public void setWakingTimer(float wakingTimer) {
        this.wakingTimer = wakingTimer;
    }

    public boolean isToRight() {
        return toRight;
    }

    public void setToRight(boolean toRight) {
        this.toRight = toRight;
    }

    public float getChargeDirection() {
        return chargeDirection;
    }

    public void setChargeDirection(float chargeDirection) {
        this.chargeDirection = chargeDirection;
    }

    public ZoteState getStatus() {
        return status;
    }

    public void setStatus(ZoteState status) {
        this.status = status;
    }

    public Rectangle getInteractionZone() {
        return interactionZone;
    }

    public void setInteractionZone(Rectangle interactionZone) {
        this.interactionZone = interactionZone;
    }

    public boolean isPlayerNearby() {
        return playerNearby;
    }

    public void setPlayerNearby(boolean playerNearby) {
        this.playerNearby = playerNearby;
    }

    public static int getDialogueCounter() {
        return dialogueCounter;
    }

    public static void setDialogueCounter(int dialogueCounter) {
        Zote.dialogueCounter = dialogueCounter;
    }

    public String[] getDialogues() {
        return dialogues;
    }

    public void setDialogues(String[] dialogues) {
        this.dialogues = dialogues;
    }

    public static int getPreceptsCounter() {
        return preceptsCounter;
    }

    public static void setPreceptsCounter(int preceptsCounter) {
        Zote.preceptsCounter = preceptsCounter;
    }

    public String[] getPrecepts() {
        return precepts;
    }

    public void setPrecepts(String[] precepts) {
        this.precepts = precepts;
    }
}
