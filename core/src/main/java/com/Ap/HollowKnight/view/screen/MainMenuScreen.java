package com.Ap.HollowKnight.view.screen;

import com.Ap.HollowKnight.HollowKnight;
import com.Ap.HollowKnight.controller.ScreenManager;
import com.Ap.HollowKnight.view.AssetLoader;
import com.Ap.HollowKnight.view.sounds.MusicType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

public class MainMenuScreen extends BaseScreen {

    public MainMenuScreen(HollowKnight game) {
        super(game);
    }


    @Override
    public void show() {
        super.show();
        audioManager.playMusic(MusicType.MAIN_THEME);
        this.batch = new SpriteBatch();
        this.gameLogo = new Image(AssetLoader.getInstance().getTexture("Ui/Hollow-Knight-Logo-PNG-Pic.png"));

        mainMenuGlowingDots.start();


        TextButton startBtn = new TextButton("Start Game", buttonStyle);
        setupButton(startBtn,()->{
            System.out.println("game is starting...");
            audioManager.stopMusic();
            ScreenManager.getInstance().setScreen("GameScreen");
        });
        TextButton achievementBtn = new TextButton("Achievement", buttonStyle);
        setupButton(achievementBtn,()->{

        });
        TextButton guideBtn = new TextButton("Guide", buttonStyle);
        setupButton(guideBtn,()->{

        });
        TextButton settingsBtn = new TextButton("Settings", buttonStyle);
        setupButton(settingsBtn,()->{

        });
        TextButton exitBtn = new TextButton("Exit", buttonStyle);
        setupButton(exitBtn,()->{
            Gdx.app.exit();
        });

        rootTable.center();
        rootTable.add(gameLogo).width(1000).height(300).padBottom(200).row();
        rootTable.add(startBtn).pad(15).row();
        rootTable.add(achievementBtn).pad(15).row();
        rootTable.add(guideBtn).pad(15).row();
        rootTable.add(settingsBtn).pad(15).row();
        rootTable.add(exitBtn).pad(15).row();



        settingsBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ScreenManager.getInstance().setScreen("SettingsScreen");
            }
        });

        exitBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });
    }

    @Override
    public void render(float delta) {
        clearScreen(0, 0, 0, 1f);
        batch.setProjectionMatrix(stage.getCamera().combined);
        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        mainMenuGlowingDots.update(delta);
        mainMenuGlowingDots.draw(batch);

        batch.end();
        super.render(delta);
    }
}
