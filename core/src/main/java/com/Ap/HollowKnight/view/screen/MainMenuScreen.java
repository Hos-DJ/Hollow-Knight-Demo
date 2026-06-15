package com.Ap.HollowKnight.view.screen;

import com.Ap.HollowKnight.HollowKnight;

public class MainMenuScreen extends BaseScreen {
    public MainMenuScreen(HollowKnight game) {
        super(game);
    }

    @Override
    public void render(float delta) {
        clearScreen(0.04f, 0.05f, 0.08f, 1f);
    }
}
