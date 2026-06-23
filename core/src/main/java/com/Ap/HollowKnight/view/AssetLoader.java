package com.Ap.HollowKnight.view;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.HashMap;

public class AssetLoader {
    private static AssetLoader instance;
    private final AssetManager internalManager;
    private final HashMap<AnimationType, Animation<TextureRegion>> animationMap;

    private AssetLoader() {
        this.internalManager = new AssetManager();
        this.animationMap = new HashMap<>();
        queueTextures(KnightAnimationType.values());
        queueTextures(EffectAnimationType.values());
        queueTextures(CrawlidAnimationType.values());
        queueTextures(HuskHornHeadAnimationType.values());
        queueTextures(MossflyAnimationType.values());

        internalManager.finishLoading();

        loadAnimations(CrawlidAnimationType.values());
        loadAnimations(HuskHornHeadAnimationType.values());
        loadAnimations(MossflyAnimationType.values());
        loadAnimations(KnightAnimationType.values());
        loadAnimations(EffectAnimationType.values());
    }

    public static AssetLoader getInstance() {
        if (instance == null) instance = new AssetLoader();
        return instance;
    }

    private void queueTextures(AnimationType[] animationTypes) {
        for (AnimationType type : animationTypes) {
            type.loadTexture(internalManager);
        }
    }

    private void loadAnimations(AnimationType[] animationTypes) {
        for (AnimationType type : animationTypes) {
            loadAnimation(type);
        }
    }

    public void loadAnimation(AnimationType type) {
        Animation<TextureRegion> animation = type.createAnimation(internalManager);
        animationMap.put(type, animation);
    }

    public Animation<TextureRegion> getAnimation(AnimationType animationType) {
        if (!animationMap.containsKey(animationType)) {
            loadAnimation(animationType);
        }
        return animationMap.get(animationType);
    }

    public Texture getTexture(String path) {
        return internalManager.get(path, Texture.class);
    }


    public void dispose() {
        internalManager.dispose();
    }
}
