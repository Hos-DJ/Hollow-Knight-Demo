package com.Ap.HollowKnight.view;

import com.Ap.HollowKnight.model.player.Charm;
import com.Ap.HollowKnight.view.animations.*;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;

import java.util.HashMap;

public class AssetLoader {
    private static AssetLoader instance;
    private final AssetManager internalManager;
    private final HashMap<AnimationType, Animation<TextureRegion>> animationMap;
    private final HashMap<String, BitmapFont> fontMap = new HashMap<>();
    private AssetLoader() {
        this.internalManager = new AssetManager();
        this.animationMap = new HashMap<>();

        InternalFileHandleResolver resolver = new InternalFileHandleResolver();
        internalManager.setLoader(FreeTypeFontGenerator.class,
            new FreeTypeFontGeneratorLoader(resolver));
        internalManager.setLoader(BitmapFont.class, ".ttf",
            new FreetypeFontLoader(resolver));
        //for menus
        FreetypeFontLoader.FreeTypeFontLoaderParameter params = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        params.fontFileName = "font/primary_font.ttf";
        params.fontParameters.size = 24;
        params.fontParameters.color = Color.WHITE;
        params.fontParameters.borderWidth = 1.5f;
        params.fontParameters.borderColor = Color.BLACK;
        internalManager.load("font_24.ttf", BitmapFont.class, params);
        //for dialogue;
        FreetypeFontLoader.FreeTypeFontLoaderParameter params14 = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        params14.fontFileName = "font/primary_font.ttf"; // Same file path!
        params14.fontParameters.size = 14;
        params14.fontParameters.color = Color.LIGHT_GRAY;
        internalManager.load("font_14.ttf", BitmapFont.class, params14);
        queueAchievements(AchievementsAssets.values());
        queueTextures(KnightAnimationType.values());
        queueTextures(EffectAnimationType.values());
        queueTextures(CrawlidAnimationType.values());
        queueTextures(HuskHornHeadAnimationType.values());
        queueTextures(MossflyAnimationType.values());
        queueTextures(CrystalGuardianAnimationType.values());
        queueTextures(ZoteAnimationType.values());
        queueTextures(HudAnimationType.values());
        queueTextures(FalseKnightAnimationType.values());
        internalManager.load("Ui/Hollow-Knight-Logo-PNG-Pic.png", Texture.class);
        internalManager.load("Ui/Audio/button-click.wav", Sound.class);
        internalManager.load("Ui/Audio/button-hover.wav", Sound.class);
        internalManager.load("Ui/Slider/slider.atlas", TextureAtlas.class);
        internalManager.load("Ui/TableBottom.png", Texture.class);
        internalManager.load("Ui/TableTop.png",Texture.class);
        for(Charm charm : Charm.values()){
            internalManager.load("Ui/Charms/"+charm.name()+".png",Texture.class);
        }

        internalManager.finishLoading();
        fontMap.put("font_24",internalManager.get("font_24.ttf", BitmapFont.class));
        fontMap.put("font_14",internalManager.get("font_14.ttf", BitmapFont.class));

        loadAnimations(CrawlidAnimationType.values());
        loadAnimations(HuskHornHeadAnimationType.values());
        loadAnimations(MossflyAnimationType.values());
        loadAnimations(KnightAnimationType.values());
        loadAnimations(EffectAnimationType.values());
        loadAnimations(CrystalGuardianAnimationType.values());
        loadAnimations(ZoteAnimationType.values());
        loadAnimations(HudAnimationType.values());
        loadAnimations(FalseKnightAnimationType.values());
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

    private void queueAchievements(AchievementsAssets[] achievementsAssets) {
        for (AchievementsAssets asset : achievementsAssets) {
            internalManager.load(asset.getPath(), Texture.class);
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

    public Sound getSound(String path) {

        return internalManager.get(path, Sound.class);
    }

    public TextureAtlas getAtlas(String path) {
        return internalManager.get(path, TextureAtlas.class);
    }


    public void dispose() {
        internalManager.dispose();
    }

    public BitmapFont getFont(String key) {
        return fontMap.get(key);
    }

    public Texture getAchievement(AchievementsAssets achievement) {
        return internalManager.get(achievement.getPath(), Texture.class);
    }
}
