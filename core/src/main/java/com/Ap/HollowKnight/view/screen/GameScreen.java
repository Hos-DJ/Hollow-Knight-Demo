package com.Ap.HollowKnight.view.screen;

import com.Ap.HollowKnight.HollowKnight;

public class GameScreen extends BaseScreen {
    public GameScreen(HollowKnight game) {
        super(game);
    }

    @Override
    public void render(float delta) {
        clearScreen(0.1f, 0.1f, 0.15f, 1f);
    }
}
