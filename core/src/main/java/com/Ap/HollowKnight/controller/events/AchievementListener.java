package com.Ap.HollowKnight.controller.events;

import com.Ap.HollowKnight.controller.AchievementManager;
import com.Ap.HollowKnight.view.AchievementsAssets;
import com.Ap.HollowKnight.view.screen.BaseScreen;
import com.Ap.HollowKnight.view.screen.toasts.AchievementToast;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.utils.Align;

import java.util.HashSet;
import java.util.Set;

public class AchievementListener implements GameEventListener {
    private final AchievementManager manager = AchievementManager.getInstance();
    private final BaseScreen currentScreen;
    private final Set<String> killedEnemyTypes = new HashSet<>();
    private final long gameStartTime;
    public AchievementListener(BaseScreen screen) {
        this.currentScreen = screen;
        this.gameStartTime = System.currentTimeMillis();
    }

    @Override
    public void onEvent(GameEvent e, Object payload) {
        switch (e) {
            case BOSS_DEFEATED -> unlock(AchievementsAssets.DEFEAT_BOSS);

            case ENEMY_KILLED -> {
                if (payload instanceof String) {
                    String enemyType = (String) payload;
                    killedEnemyTypes.add(enemyType);
                    if (killedEnemyTypes.size() >= 4) {
                        unlock(AchievementsAssets.TRUE_HUNTER);
                    }
                }
            }

            case GAME_COMPLETED -> {
                unlock(AchievementsAssets.GAME_COMPLETION);

                long timeTakenMillis = System.currentTimeMillis() - gameStartTime;
                if (timeTakenMillis <= 300000) {
                    unlock(AchievementsAssets.SPEEDRUN);
                }
            }

            case SECRET_DISCOVERED -> unlock(AchievementsAssets.SECRET_DISCOVERED);
        }
    }

    private void unlock(AchievementsAssets achievement) {
        if (!manager.isUnlocked(achievement)) {
            manager.unlock(achievement);
            showPopup(achievement);
        }
    }

    private void showPopup(AchievementsAssets achievement) {
        if (currentScreen == null || currentScreen.getToastStack() == null) return;

        AchievementToast toast = new AchievementToast(achievement, currentScreen.getLabelStyle());

        Container<AchievementToast> container = new Container<>(toast);
        container.align(Align.top | Align.right);
        container.padTop(30f).padRight(30f);

        currentScreen.getToastStack().add(container);
    }
}
