package com.Ap.HollowKnight.view.animations;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public interface AnimationType {
    record Spec(String path, int frameCount, int rowCount, int colCount, Animation.PlayMode playMode) {
    }

    Spec getSpec();

    default String getPath() {
        return getSpec().path();
    }

    default int getFrameCount() {
        return getSpec().frameCount();
    }

    default int getRowCount() {
        return getSpec().rowCount();
    }

    default int getColCount() {
        return getSpec().colCount();
    }

    default Animation.PlayMode getPlayMode() {
        return getSpec().playMode();
    }

    default void loadTexture(AssetManager assetManager) {
        assetManager.load(getPath(), Texture.class);
    }

    default Animation<TextureRegion> createAnimation(AssetManager assetManager) {
        Texture texture = assetManager.get(getPath(), Texture.class);
        TextureRegion[][] split = TextureRegion.split(
            texture,
            texture.getWidth() / getColCount(),
            texture.getHeight() / getRowCount()
        );

        TextureRegion[] frames = new TextureRegion[getFrameCount()];
        int columnCount = split[0].length;
        for (int i = 0; i < frames.length; i++) {
            int row = i / columnCount;
            int col = i % columnCount;
            frames[i] = split[row][col];
        }

        Animation<TextureRegion> animation = new Animation<>(1 / 30f, frames);
        animation.setPlayMode(getPlayMode());
        return animation;
    }
}
