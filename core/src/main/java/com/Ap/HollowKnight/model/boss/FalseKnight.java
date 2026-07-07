package com.Ap.HollowKnight.model.boss;

import com.Ap.HollowKnight.controller.events.GameEvent;
import com.Ap.HollowKnight.controller.events.GameEventMessenger;
import com.Ap.HollowKnight.model.enemy.EnemyModel;
import com.Ap.HollowKnight.model.game.FacingDirection;
import com.Ap.HollowKnight.model.level.LevelModel;
import com.Ap.HollowKnight.model.map.Block;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Random;

public class FalseKnight extends EnemyModel {
    private BossState currentState;
    private Class<? extends BossState> previousState;
    private FalseKnightPhase currentPhase;
    private Rectangle maceHitBox;
    private int spamCount = 0;
    private boolean isPhaseTwo = false;
    private int damageCounter= 0;
    private ArrayList<ShockWave> shockWaves = new ArrayList<>();

    private Random random  = new Random();
    private static final int BASE_ARMOR_HP = 200;
    private static final float MAX_SPAM = 2;
    private static final int BASE_HP = 60;
    int cnt = 1;

    private int currentArmorHp;
    private int currentHp;

    public FalseKnight(Vector2 position, Rectangle hitBox, Vector2 spawnPoint) {
        super(position, hitBox, spawnPoint, 60);
        currentState = new IdleState();
        currentArmorHp = BASE_ARMOR_HP;
        currentHp = BASE_HP;
        maceHitBox = new Rectangle(position.x, position.y,180,120);
        currentPhase = FalseKnightPhase.IDLE;
    }
    @Override
    public void update(float delta, ArrayList<Block> blocks){
        if (currentState != null) {
            currentState.update(this, LevelModel.getInstance().getKnight(), blocks, delta);
        }
        if(cnt<=30){
            cnt++;
        }
        else{
            System.out.println(currentState.toString());
            cnt = 1;
        }
    }

    @Override
    public void takeDamage(int amount) {
        if(currentState instanceof StunState){
            currentHp -= amount;
            if(currentHp <= 0){
                GameEventMessenger.getInstance().dispatch(GameEvent.BOSS_DEFEATED,this);
                die();
            }
        }else{
            currentArmorHp -= amount;
            if(currentArmorHp <= BASE_ARMOR_HP/2){
                isPhaseTwo = true;
                changeState(new StunState());
                //eventListener
            }
            damageCounter++;
        }
        if(damageCounter>=3||random.nextInt(100)>=80){
            changeState(new DefensiveLeapState());
            damageCounter = 0;
        }
    }

    public void changeState(BossState newState){
        if(currentState != null){
            previousState = currentState.getClass();
            currentState.exit(this);
        }
        currentState = newState;
        currentState.enter(this, LevelModel.getInstance().getKnight());
    }

    public void spawnShockwave() {
        float waveWidth = 60f;
        float waveHeight = 120f;

        float startX = (getFacingDirection() == FacingDirection.RIGHT)
            ? getHitBox().x + getHitBox().width + 10f
            : getHitBox().x - waveWidth - 10f;

        Vector2 spawnPos = new Vector2(startX, getHitBox().y);
        Rectangle hitBox = new Rectangle(startX, getHitBox().y, waveWidth, waveHeight);

        ShockWave wave = new ShockWave(spawnPos, hitBox, spawnPos, getFacingDirection());
        this.shockWaves.add(wave);

    }



    public BossState getBossCurrentState() {
        return currentState;
    }

    public void setCurrentState(BossState currentState) {
        this.currentState = currentState;
    }

    public Class<? extends BossState> getPreviousState() {
        return previousState;
    }

    public void setPreviousState(Class<? extends BossState> previousState) {
        this.previousState = previousState;
    }

    public int getSpamCount() {
        return spamCount;
    }

    public void setSpamCount(int spamCount) {
        this.spamCount = spamCount;
    }

    public boolean isPhaseTwo() {
        return isPhaseTwo;
    }

    public void setPhaseTwo(boolean phaseTwo) {
        isPhaseTwo = phaseTwo;
    }

    public int getDamageCounter() {
        return damageCounter;
    }

    public void setDamageCounter(int damageCounter) {
        this.damageCounter = damageCounter;
    }

    public Random getRandom() {
        return random;
    }

    public void setRandom(Random random) {
        this.random = random;
    }

    public int getCurrentArmorHp() {
        return currentArmorHp;
    }

    public void setCurrentArmorHp(int currentArmorHp) {
        this.currentArmorHp = currentArmorHp;
    }

    public int getCurrentHp() {
        return currentHp;
    }

    public void setCurrentHp(int currentHp) {
        this.currentHp = currentHp;
    }

    public Rectangle getMaceHitBox() {
        return maceHitBox;
    }

    public void setMaceHitBox(Rectangle maceHitBox) {
        this.maceHitBox = maceHitBox;
    }

    public FalseKnightPhase getCurrentPhase() {
        return currentPhase;
    }

    public void setCurrentPhase(FalseKnightPhase currentPhase) {
        this.currentPhase = currentPhase;
    }

    public ArrayList<ShockWave> getShockWaves() {
        return shockWaves;
    }
}
