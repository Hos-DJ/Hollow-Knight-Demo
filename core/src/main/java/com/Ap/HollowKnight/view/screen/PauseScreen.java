package com.Ap.HollowKnight.view.screen;

import com.Ap.HollowKnight.HollowKnight;

public class PauseScreen extends BaseScreen {
    public PauseScreen(HollowKnight game) {
        super(game);
    }

    @Override
    public void render(float delta) {
        clearScreen(0.03f, 0.03f, 0.05f, 1f);
    }
}
