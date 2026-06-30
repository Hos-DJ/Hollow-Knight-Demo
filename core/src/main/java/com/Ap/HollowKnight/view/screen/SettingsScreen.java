package com.Ap.HollowKnight.view.screen;

import com.Ap.HollowKnight.HollowKnight;
import com.Ap.HollowKnight.controller.ScreenManager;
import com.Ap.HollowKnight.controller.SettingsController;
import com.Ap.HollowKnight.view.AssetLoader;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;

public class SettingsScreen extends BaseScreen {
    private static int DEFAULT = 8 ;
    private final SettingsController controller = SettingsController.getInstance();
    private Table keyTable ;
    public SettingsScreen(HollowKnight game) {
        super(game);
        keyTable = createKeypadTable();
        keyTable.setVisible(false);
    }

    @Override
    public void show() {
        controller.resetSounds();
        super.show();
        batch = new SpriteBatch();
        rootTable.center();
        rootTable.setFillParent(true);
        stage.addActor(keyTable);
        Image topImage = new Image(AssetLoader.getInstance().getTexture("Ui/TableTop.png"));
        rootTable.add(topImage).colspan(3).center().padBottom(20).row();


        TextButton musicButton = new TextButton("MUSIC VOLUME:", buttonStyle);
        TextButton musicValue = new TextButton("8", buttonStyle);
        Slider musicSlider = new Slider(0, 10, 1, false, sliderStyle);
        musicSlider.setValue(8);
        musicSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event , Actor actor){
                int amount = (int) musicSlider.getValue();
                controller.changeMusicVolume(amount);
                musicValue.setText(String.valueOf(amount));
                if(amount == 0){
                    musicButton.setText("MUSIC OFF");
                }
                else {
                    musicButton.setText("MUSIC VOLUME:");
                }
            }

        });
        setupButton(musicButton,()->{
            if(musicButton.getText().toString().equals("MUSIC VOLUME:")) {
                musicButton.setText("MUSIC OFF");
                controller.changeMusicVolume(0);
                musicSlider.setValue(0);
            }
            else {
                musicButton.setText("MUSIC VOLUME:");
                controller.changeMusicVolume(1);
                musicSlider.setValue(1);
            }
        });


        TextButton sfxButton = new TextButton("SFX VOLUME:", buttonStyle);
        Slider sfxSlider = new Slider(0, 10, 1, false, sliderStyle);
        sfxSlider.setValue(8);
        TextButton sfxValue = new TextButton(String.valueOf((int) sfxSlider.getValue()), buttonStyle);

        sfxSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                int amount = (int) sfxSlider.getValue();
                controller.changeSfxVolume(amount);
                sfxValue.setText(String.valueOf(amount));

                if (amount == 0) {
                    sfxButton.setText("SFX OFF");
                } else {
                    sfxButton.setText("SFX VOLUME:");
                }
            }
        });
        setupButton(sfxButton, () -> {
            if (sfxButton.getText().toString().equals("SFX VOLUME:")) {
                sfxSlider.setValue(0);
            } else {
                sfxSlider.setValue(8);
            }
        });

        TextButton brightnessButton = new TextButton("BRIGHTNESS:", buttonStyle);
        TextButton brightnessValue = new TextButton("8", buttonStyle);
        Slider brightnessSlider = new Slider(0, 10, 1, false, sliderStyle);
        brightnessSlider.setValue(8);
        brightnessSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event , Actor actor){
                int amount = (int) brightnessSlider.getValue();
                controller.changeBrightness(amount);
                brightnessValue.setText(String.valueOf(amount));
            }
        });

        TextButton resetSounds = new TextButton("RESET SOUND", buttonStyle);
        setupButton(resetSounds,()->{
            controller.resetSounds();
            musicSlider.setValue(8);
            sfxSlider.setValue(8);
            musicButton.setText("MUSIC VOLUME:");
        });

        TextButton changeGamePlayButton = new TextButton("CHANGE GAME KEYPAD", buttonStyle);
        setupButton(changeGamePlayButton,()->{
            rootTable.setVisible(false);
            keyTable = createKeypadTable();
            keyTable.setVisible(true);
        });
        TextButton backButton = new TextButton("BACK", buttonStyle);
        setupButton(backButton,()->{
            ScreenManager.getInstance().setScreen("MainMenuScreen");
        });


        rootTable.add(musicButton).align(Align.left).padRight(40).expandY();
        rootTable.add(musicSlider).width(300f).center().expandY();
        rootTable.add(musicValue).align(Align.right).padLeft(40).expandY().row();

        rootTable.add(sfxButton).align(Align.left).padRight(40);
        rootTable.add(sfxSlider).width(300f).center();
        rootTable.add(sfxValue).align(Align.right).padLeft(40).row();


        rootTable.add(brightnessButton).align(Align.left).padRight(40);
        rootTable.add(brightnessSlider).width(300f).center();
        rootTable.add(brightnessValue).align(Align.right).padLeft(40).row();

        rootTable.add(resetSounds).center().colspan(3).padBottom(20).padTop(20).expandY().row();
        rootTable.add(changeGamePlayButton).center().colspan(3).padBottom(20).padTop(20).row();
        rootTable.add(backButton).center().colspan(3).padTop(40).row();





        // ۴. ردیف سوم: ایمیج پایینی (مثل فوتر یا خط تزیینی پایین منو)
        Image bottomImage = new Image(AssetLoader.getInstance().getTexture("Ui/TableBottom.png"));
        rootTable.add(bottomImage).colspan(3).center().padTop(20).row();
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

    private Table createKeypadTable() {
        Table table = new Table();
        Label title = new Label("GAME KEYPAD", labelStyle);
        TextButton backButton = new TextButton("BACK", buttonStyle);
        setupButton(backButton,()->{
            table.setVisible(false);
            rootTable.setVisible(true);
        });
        title.setScale(2);
        table.center();
        table.add(title).align(Align.left).padRight(20).padTop(20).expandY().row();
        return table;
    }
}
