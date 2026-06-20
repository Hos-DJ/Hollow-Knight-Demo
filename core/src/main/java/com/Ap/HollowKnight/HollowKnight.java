package com.Ap.HollowKnight;
import com.Ap.HollowKnight.controller.ScreenManager;
import com.Ap.HollowKnight.view.screen.GameScreen;

public class HollowKnight extends com.badlogic.gdx.Game {

    @Override
    public void create() {
        ScreenManager screenManager =ScreenManager.getInstance();
        screenManager.initialize(this);
        screenManager.setScreen("GameScreen");
    }
}
