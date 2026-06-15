package com.Ap.HollowKnight.view.screen;

import com.Ap.HollowKnight.HollowKnight;

public class SettingsScreen extends BaseScreen {
    public SettingsScreen(HollowKnight game) {
        super(game);
    }

    @Override
    public void render(float delta) {
        clearScreen(0.06f, 0.06f, 0.09f, 1f);
    }
}
