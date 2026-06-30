
package com.Ap.HollowKnight.controller;

import com.Ap.HollowKnight.view.AchievementsAssets;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class AchievementManager {
    private static AchievementManager instance;
    private final Preferences preferences;
    private static final String PREFS_NAME = "HollowKnight_Achievements_Json";

    private AchievementManager() {
        this.preferences = Gdx.app.getPreferences(PREFS_NAME);
    }

    public static AchievementManager getInstance() {
        if (instance == null) {
            instance = new AchievementManager();
        }
        return instance;
    }

    public boolean isUnlocked(AchievementsAssets achievement) {
        return preferences.getBoolean(achievement.name(), false);
    }


    public void unlock(AchievementsAssets achievement) {
        if (!isUnlocked(achievement)) {
            preferences.putBoolean(achievement.name(), true);
            preferences.flush();

        }
    }
}

