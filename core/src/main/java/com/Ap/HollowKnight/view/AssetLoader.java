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
        for (KnightAnimationType type : KnightAnimationType.values()) {
            internalManager.load(type.getPath(), Texture.class);
        }
        for (EffectAnimationType type : EffectAnimationType.values()) {
            internalManager.load(type.getPath(), Texture.class);
        }
        for (CrawlidAnimationType type : CrawlidAnimationType.values()) {
            internalManager.load(type.getPath(), Texture.class);
        }
        for (HuskHornHeadAnimationType type : HuskHornHeadAnimationType.values()) {
            internalManager.load(type.getPath(), Texture.class);
        }
        for(MossflyAnimationType type : MossflyAnimationType.values()){
            internalManager.load(type.getPath(), Texture.class);
        }

        internalManager.finishLoading();
        for (CrawlidAnimationType type : CrawlidAnimationType.values()) {
            loadCrawlidAnimation(type);
        }
        for (HuskHornHeadAnimationType type : HuskHornHeadAnimationType.values()) {
            loadHuskHornHeadAnimation(type);
        }
        for(MossflyAnimationType type : MossflyAnimationType.values()){
            loadMossflyAnimation(type);
        }
        for (KnightAnimationType type : KnightAnimationType.values()) {
            loadKnightAnimation(type);
        }

        for (EffectAnimationType type : EffectAnimationType.values()) {
            loadEffectAnimation(type);
        }

    }

    public static AssetLoader getInstance() {
        if (instance == null) instance = new AssetLoader();
        return instance;
    }

    public void loadKnightAnimation(KnightAnimationType knightAnimationType) {
        Texture texture = internalManager.get(knightAnimationType.getPath(), Texture.class);
        TextureRegion[][] split = TextureRegion.split(texture, texture.getWidth() / knightAnimationType.getColCount(), texture.getHeight() / knightAnimationType.getRowCount());

        int frameCount = knightAnimationType.getFrameCount();
        TextureRegion[] frames = new TextureRegion[frameCount];
        int columnNumber = split[0].length;
        for (int i = 0; i < frameCount; i++) {
            int row = i / columnNumber;
            int col = i % columnNumber;
            frames[i] = split[row][col];
        }
        Animation<TextureRegion> animation = new Animation<>(1 / 30f, frames);

        animationMap.put(knightAnimationType, animation);
    }

    public void loadEffectAnimation(EffectAnimationType effectAnimationType) {
        Texture texture = internalManager.get(effectAnimationType.getPath(), Texture.class);
        TextureRegion[][] split = TextureRegion.split(texture, texture.getWidth() / effectAnimationType.getColCount(), texture.getHeight() / effectAnimationType.getRowCount());
        int frameCount = effectAnimationType.getFrameCount();
        TextureRegion[] frames = new TextureRegion[frameCount];
        int columnNumber = split[0].length;
        for (int i = 0; i < frameCount; i++) {
            int row = i / columnNumber;
            int col = i % columnNumber;
            frames[i] = split[row][col];
        }
        Animation<TextureRegion> animation = new Animation<>(1 / 30f, frames);

        animationMap.put(effectAnimationType, animation);
    }

    public void loadCrawlidAnimation(CrawlidAnimationType type) {
        Texture texture = internalManager.get(type.getPath(), Texture.class);
        TextureRegion[][] split = TextureRegion.split(texture,
            texture.getWidth() / type.getColCount(),
            texture.getHeight() / type.getRowCount());
        int frameCount = type.getFrameCount();
        TextureRegion[] frames = new TextureRegion[frameCount];
        int columnNumber = split[0].length;
        for (int i = 0; i < frameCount; i++) {
            int row = i / columnNumber;
            int col = i % columnNumber;
            frames[i] = split[row][col];
        }
        Animation<TextureRegion> animation = new Animation<>(1 / 30f, frames);
        animationMap.put(type, animation);
    }

    public void loadHuskHornHeadAnimation(HuskHornHeadAnimationType type) {
        Texture texture = internalManager.get(type.getPath(), Texture.class);
        TextureRegion[][] split = TextureRegion.split(texture,
            texture.getWidth() / type.getColCount(),
            texture.getHeight() / type.getRowCount());
        int frameCount = type.getFrameCount();
        TextureRegion[] frames = new TextureRegion[frameCount];
        int columnNumber = split[0].length;
        for (int i = 0; i < frameCount; i++) {
            int row = i / columnNumber;
            int col = i % columnNumber;
            frames[i] = split[row][col];
        }
        Animation<TextureRegion> animation = new Animation<>(1 / 30f, frames);
        animationMap.put(type, animation);
    }

    public void loadMossflyAnimation(MossflyAnimationType type) {
        Texture texture = internalManager.get(type.getPath(), Texture.class);
        TextureRegion[][] split = TextureRegion.split(texture,
            texture.getWidth() / type.getColCount(),
            texture.getHeight() / type.getRowCount());
        int frameCount = type.getFrameCount();
        TextureRegion[] frames = new TextureRegion[frameCount];
        int columnNumber = split[0].length;
        for (int i = 0; i < frameCount; i++) {
            int row = i / columnNumber;
            int col = i % columnNumber;
            frames[i] = split[row][col];
        }
        Animation<TextureRegion> animation = new Animation<>(1 / 30f, frames);
        animationMap.put(type, animation);
    }

    public Animation<TextureRegion> getAnimation(AnimationType animationType) {
        if (!animationMap.containsKey(animationType)) {
            if (animationType instanceof KnightAnimationType) loadKnightAnimation((KnightAnimationType) animationType);
            else if (animationType instanceof EffectAnimationType)
                loadEffectAnimation((EffectAnimationType) animationType);
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
