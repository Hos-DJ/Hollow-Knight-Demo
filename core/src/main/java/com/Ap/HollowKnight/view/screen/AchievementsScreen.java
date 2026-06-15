package com.Ap.HollowKnight.view.screen;

import com.Ap.HollowKnight.HollowKnight;

public class AchievementsScreen extends BaseScreen {
    public AchievementsScreen(HollowKnight game) {
        super(game);
    }

    @Override
    public void render(float delta) {
        clearScreen(0.05f, 0.07f, 0.09f, 1f);
    }
}
