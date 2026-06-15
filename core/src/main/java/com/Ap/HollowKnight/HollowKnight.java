package com.Ap.HollowKnight;
import com.Ap.HollowKnight.view.screen.GameScreen;

// Game (not ApplicationAdapter) lets us switch between Screens
public class HollowKnight extends com.badlogic.gdx.Game {

    @Override
    public void create() {
        setScreen(new GameScreen(this));
    }
}
