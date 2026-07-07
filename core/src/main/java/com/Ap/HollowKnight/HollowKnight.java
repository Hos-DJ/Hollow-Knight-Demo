package com.Ap.HollowKnight;

import com.Ap.HollowKnight.controller.ScreenManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Pixmap;

public class HollowKnight extends com.badlogic.gdx.Game {

    @Override
    public void create() {
        ScreenManager screenManager = ScreenManager.getInstance();
        screenManager.initialize(this);
        screenManager.setScreen("MainMenuScreen");

        Pixmap pixmap = new Pixmap(Gdx.files.internal("Ui/cursor.png"));
        int hotspotX = 0;
        int hotspotY = 0;
        Cursor customCursor = Gdx.graphics.newCursor(pixmap, hotspotX, hotspotY);
        Gdx.graphics.setCursor(customCursor);
        pixmap.dispose();
    }
}
