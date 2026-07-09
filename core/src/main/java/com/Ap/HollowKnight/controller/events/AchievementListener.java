package com.Ap.HollowKnight.controller.events;

import com.Ap.HollowKnight.controller.AchievementManager;
import com.Ap.HollowKnight.view.AchievementsAssets;
import com.Ap.HollowKnight.view.screen.BaseScreen;
import com.Ap.HollowKnight.view.screen.toasts.AchievementToast;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.FloatAction;
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
            case SPEED_RUN -> unlock(AchievementsAssets.SPEEDRUN);
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

        for (Actor actor : currentScreen.getToastStack().getChildren()) {
            if (actor instanceof Container) {
                @SuppressWarnings("unchecked")
                Container<AchievementToast> existingContainer = (Container<AchievementToast>) actor;

                float currentPad = existingContainer.getPadTop();

                FloatAction slideAction = new FloatAction() {
                    @Override
                    protected void update(float percent) {
                        super.update(percent);
                        existingContainer.padTop(getValue());
                        existingContainer.invalidateHierarchy();
                    }
                };
                slideAction.setStart(currentPad);
                slideAction.setEnd(currentPad + 120f);
                slideAction.setDuration(0.4f);
                slideAction.setInterpolation(Interpolation.swingOut);

                existingContainer.addAction(slideAction);
            }
        }

        AchievementToast toast = new AchievementToast(achievement, currentScreen.getLabelStyle());

        Container<AchievementToast> container = new Container<>(toast);
        container.align(Align.top | Align.right);
        container.padTop(30f).padRight(30f);

        container.getColor().a = 0f;
        container.addAction(Actions.sequence(
            Actions.fadeIn(0.5f, Interpolation.fade),
            Actions.delay(3.0f),
            Actions.fadeOut(0.5f, Interpolation.fade),
            Actions.removeActor()
        ));

        currentScreen.getToastStack().addActor(container);
    }
}

