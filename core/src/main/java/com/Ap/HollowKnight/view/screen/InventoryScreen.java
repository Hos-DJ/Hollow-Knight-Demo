package com.Ap.HollowKnight.view.screen;

import com.Ap.HollowKnight.HollowKnight;

public class InventoryScreen extends BaseScreen {
    public InventoryScreen(HollowKnight game) {
        super(game);
    }

    @Override
    public void render(float delta) {
        clearScreen(0.05f, 0.06f, 0.07f, 1f);
    }
}
