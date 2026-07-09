package com.Ap.HollowKnight;

import com.Ap.HollowKnight.controller.ScreenManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.utils.I18NBundle;

import java.util.Locale;

public class HollowKnight extends com.badlogic.gdx.Game {
    private I18NBundle myBundle;
    @Override
    public void create() {
        FileHandle baseFileHandle = Gdx.files.internal("language/strings");
        myBundle = I18NBundle.createBundle(baseFileHandle, Locale.ENGLISH);
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
    public I18NBundle getBundle() {
        return myBundle;
    }

    public void setMyBundle(I18NBundle myBundle) {
        this.myBundle = myBundle;
    }
}
