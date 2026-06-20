package com.Ap.HollowKnight.view;

import com.Ap.HollowKnight.model.player.Knight;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class PlayerHUD {
    private final ShapeRenderer shapeRenderer;
    private static final float MASK_SIZE  = 20f;
    private static final float MASK_GAP   = 6f;
    private static final float HUD_MARGIN = 16f;

    public PlayerHUD() {
        this.shapeRenderer = new ShapeRenderer();
    }

    public void render(Knight knight, OrthographicCamera hudCamera) {
        shapeRenderer.setProjectionMatrix(hudCamera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        drawMasks(knight, hudCamera);
        drawSoulVessel(knight, hudCamera);
        shapeRenderer.end();
    }

    private void drawMasks(Knight knight, OrthographicCamera hudCamera) {
        float startX = HUD_MARGIN;
        float startY = hudCamera.viewportHeight - HUD_MARGIN - MASK_SIZE;

        for (int i = 0; i < Knight.MAX_MASKS; i++) {
            float x = startX + i * (MASK_SIZE + MASK_GAP);
            boolean filled = i < knight.getCurrentMasks();
            shapeRenderer.setColor(filled ? Color.WHITE : Color.DARK_GRAY);
            shapeRenderer.rect(x, startY, MASK_SIZE, MASK_SIZE);
        }
    }

    private void drawSoulVessel(Knight knight, OrthographicCamera hudCamera) {
        float vesselX = HUD_MARGIN;
        float vesselY = hudCamera.viewportHeight - HUD_MARGIN - MASK_SIZE - 90f;
        float vesselWidth = 24f;
        float vesselHeight = 80f;

        shapeRenderer.setColor(Color.DARK_GRAY);
        shapeRenderer.rect(vesselX, vesselY, vesselWidth, vesselHeight);

        float fillHeight = vesselHeight * (knight.getCurrentSoul() / 99f);
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rect(vesselX, vesselY, vesselWidth, fillHeight);
    }

    public void dispose() {
        shapeRenderer.dispose();
    }
}
