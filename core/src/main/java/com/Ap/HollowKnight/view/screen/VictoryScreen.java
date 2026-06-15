package com.Ap.HollowKnight.view.screen;

import com.Ap.HollowKnight.HollowKnight;

public class VictoryScreen extends BaseScreen {
    public VictoryScreen(HollowKnight game) {
        super(game);
    }

    @Override
    public void render(float delta) {
        clearScreen(0.08f, 0.08f, 0.1f, 1f);
    }
}
