package com.Ap.HollowKnight.view;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import java.util.HashMap;

public class AssetLoader {
    private static AssetLoader instance;
    private final AssetManager internalManager;
    private final HashMap<AnimationType, Animation<TextureRegion>> animationMap;

    private AssetLoader() {
        this.internalManager = new AssetManager();
        this.animationMap = new HashMap<>();
        for (AnimationType type : AnimationType.values()) {
            loadAnimation(type);
        }
        for (AnimationType type : AnimationType.values()) {
            internalManager.load(type.getPath(), Texture.class);
        }

        internalManager.finishLoading();

        for (AnimationType type : AnimationType.values()) {
            loadAnimation(type);
        }
    }
    public static AssetLoader getInstance() {
        if(instance == null) instance = new AssetLoader();
        return instance;
    }
    public  void loadAnimation(AnimationType animationType){
        Texture texture = internalManager.get(animationType.getPath(),Texture.class);
        TextureRegion[][] split =TextureRegion.split(texture,
            texture.getWidth()/animationType.getColCount(),
            texture.getHeight()/animationType.getRowCount());

        int frameCount = animationType.getFrameCount();
        TextureRegion[] frames = new TextureRegion[frameCount];
        int columnNumber =split [0].length;
        for (int i = 0;i<frameCount;i++){
            int row = i/columnNumber;
            int col = i%columnNumber;
            frames[i] = split[row][col];
        }
        Animation<TextureRegion> animation = new Animation<>(1/30f, frames);
        animation.setPlayMode(Animation.PlayMode.LOOP);

        animationMap.put(animationType, animation);
    }


    public Animation<TextureRegion> getAnimation(AnimationType animationType) {
        if (!animationMap.containsKey(animationType)) {
            loadAnimation(animationType);
        }
        return animationMap.get(animationType);
    }

    public Texture getTexture(String path) {
        return internalManager.get(path,Texture.class);
    }

    public void dispose(){
        internalManager.dispose();
    }
}
