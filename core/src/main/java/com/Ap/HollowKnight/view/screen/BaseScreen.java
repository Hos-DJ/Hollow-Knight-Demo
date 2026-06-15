package com.Ap.HollowKnight.view.screen;

import com.Ap.HollowKnight.HollowKnight;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;

public abstract class BaseScreen implements Screen {
    protected final HollowKnight game;

    protected BaseScreen(HollowKnight game) {
        this.game = game;
    }

    protected void clearScreen(float red, float green, float blue, float alpha) {
        Gdx.gl.glClearColor(red, green, blue, alpha);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void show() {
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
    }
}
