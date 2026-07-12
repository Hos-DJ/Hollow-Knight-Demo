package com.Ap.HollowKnight.view;

import com.Ap.HollowKnight.model.Knight.Knight;
import com.Ap.HollowKnight.view.animations.HudAnimationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class PlayerHUD {

    private enum MaskState {FULL, EMPTY, BREAKING, FILLING}

    private static class MaskData {
        MaskState state = MaskState.FULL;
        float timer = 0f;
    }

    private static final float HUD_X_OFFSET = 25f;
    private static final float HUD_Y_TOP_OFFSET = 20f;

    private static final float ORB_SIZE_HORIZONTAL = 120f;
    private static  final float ORB_SIZE_VERTICAL = 120f;

    private static final float HEALTH_BAR_WIDTH = 260f;
    private static final float HEALTH_BAR_HEIGHT = 90f;

    private static final float MASK_START_X_OFFSET = 130.0f;
    private static final float MASK_START_Y_OFFSET = 22f;
    private static final float MASK_SIZE = 90f;
    private static final float MASK_GAP = -5f;

    private static final int MAX_MASKS = 5;
    private static final float MAX_SOUL = 99f;
    private static final float SOUL_LERP_SPEED = 5f;

    private final MaskData[] maskData = new MaskData[MAX_MASKS];
    private float displayedSoul = 0f;
    private final SpriteBatch batch;
    private final TextureRegion fillSoul = new TextureRegion();

    public PlayerHUD() {
        batch = new SpriteBatch();
        for (int i = 0; i < maskData.length; i++) {
            maskData[i] = new MaskData();
        }
    }


    public void render(Knight knight, OrthographicCamera hudCamera, float delta) {
        displayedSoul += (knight.getCurrentSoul() - displayedSoul) * SOUL_LERP_SPEED * delta;

        batch.setProjectionMatrix(hudCamera.combined);
        batch.begin();
        drawMasks(delta);
        drawSoulOrb();
        batch.end();

    }

    public void breakMask(int amount) {
        int counter = 0;
        for (int i = maskData.length - 1; i >= 0; i--) {
            if (maskData[i].state == MaskState.FULL) {
                maskData[i].state = MaskState.BREAKING;
                maskData[i].timer = 0f;
                counter++;
                if (counter == amount) {
                    return;
                }
            }
        }
    }

    public void refillMask() {
        for (MaskData md : maskData) {
            if (md.state == MaskState.EMPTY) {
                md.state = MaskState.FILLING;
                md.timer = 0f;
                break;
            }
        }
    }

    public void resetMasks() {
        for (MaskData md : maskData) {
            md.state = MaskState.FULL;
            md.timer = 0f;
        }
    }

    public void masksAndSoulAfterLoad(float soulAmount, int masks) {
        for (int i = 0; i < maskData.length; i++) {
            if (i < masks) {
                maskData[i].state = MaskState.FULL;
            } else {
                maskData[i].state = MaskState.EMPTY;
            }
            maskData[i].timer = 0f;
        }

        displayedSoul = soulAmount;
    }


    private void drawMasks(float delta) {
        float startX = HUD_X_OFFSET +MASK_START_X_OFFSET;
        float startY = Gdx.graphics.getHeight() - HUD_Y_TOP_OFFSET -MASK_START_Y_OFFSET -MASK_SIZE;

        for (int i = 0; i < MAX_MASKS; i++) {
            MaskData md = maskData[i];
            md.timer += delta;

            HudAnimationType animType = getHudAnimationType(md);
            Animation<TextureRegion> anim = AssetLoader.getInstance().getAnimation(animType);

            if (md.state == MaskState.BREAKING && anim.isAnimationFinished(md.timer)) {
                md.state = MaskState.EMPTY;
                md.timer = 0f;
            } else if (md.state == MaskState.FILLING && anim.isAnimationFinished(md.timer)) {
                md.state = MaskState.FULL;
                md.timer = 0f;
            }

            TextureRegion frame = anim.getKeyFrame(md.timer);
            float x = startX + i * (MASK_SIZE + MASK_GAP);
            batch.draw(frame, x, startY, MASK_SIZE, MASK_SIZE);
        }
    }

    private HudAnimationType getHudAnimationType(MaskData md) {
        return switch (md.state) {
            case BREAKING -> HudAnimationType.BREAK_MASK;
            case FILLING -> HudAnimationType.HEALTH_REFILL;
            case FULL -> HudAnimationType.FULL_MASK;
            case EMPTY -> HudAnimationType.EMPTY_MASK;
        };
    }


    private void drawSoulOrb() {
        Animation<TextureRegion> soulOrbAnimation = AssetLoader.getInstance().getAnimation(HudAnimationType.SOUL_ORB);
        TextureRegion fullOrbTex = soulOrbAnimation.getKeyFrame(0);

        float x = HUD_X_OFFSET;
        float y =Gdx.graphics.getHeight() - HUD_Y_TOP_OFFSET - ORB_SIZE_VERTICAL;
        batch.setColor(0.12f, 0.12f, 0.16f, 0.95f);
        batch.draw(fullOrbTex, x, y, ORB_SIZE_HORIZONTAL, ORB_SIZE_VERTICAL);
        batch.setColor(Color.WHITE);

        float percent = Math.clamp(displayedSoul / MAX_SOUL,0f,1f);
        if(percent>0.005f){
            int originalHeight = fullOrbTex.getRegionHeight();
            int finalHeight = Math.round(originalHeight * percent);
            int offset = originalHeight - finalHeight;
            fillSoul.setRegion(fullOrbTex);
            fillSoul.setRegionY(fullOrbTex.getRegionY() + offset);
            fillSoul.setRegionHeight(finalHeight);

            float renderHeight = ORB_SIZE_HORIZONTAL * percent;
            batch.draw(fillSoul,x,y,ORB_SIZE_HORIZONTAL,renderHeight);
        }

    }


    public void dispose() {
        batch.dispose();
    }
}
