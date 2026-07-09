package com.Ap.HollowKnight.controller.events;

public class StatisticsListener implements GameEventListener {
    private static StatisticsListener instance;

    private int enemiesKilled = 0;
    private int deathCount = 0;
    private float timeAccumulator = 0f;
    private int secondsPassed = 0;

    private StatisticsListener() {}

    public static StatisticsListener getInstance() {
        if (instance == null) {
            instance = new StatisticsListener();
        }
        return instance;
    }

    public void loadStats(int enemiesKilled, int deathCount, int secondsPassed) {
        this.enemiesKilled = enemiesKilled;
        this.deathCount = deathCount;
        this.secondsPassed = secondsPassed;
        this.timeAccumulator = 0f;
    }

    public void resetStats() {
        loadStats(0, 0, 0);
    }

    @Override
    public void onEvent(GameEvent event, Object data) {
        if (event == GameEvent.ENEMY_KILLED) {
            enemiesKilled++;
        } else if (event == GameEvent.PLAYER_DEATH) {
            deathCount++;
        }
    }

    public void update(float delta) {
        timeAccumulator += delta;
        if (timeAccumulator >= 1.0f) {
            int secondsToAdd = (int) timeAccumulator;
            secondsPassed += secondsToAdd;
            timeAccumulator -= secondsToAdd;
        }
    }

    public int getEnemiesKilled() { return enemiesKilled; }
    public int getDeathCount() { return deathCount; }
    public int getSecondsPassed() { return secondsPassed; }
}
