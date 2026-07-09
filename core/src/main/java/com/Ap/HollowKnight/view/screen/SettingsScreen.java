package com.Ap.HollowKnight.view.screen;

import com.Ap.HollowKnight.HollowKnight;
import com.Ap.HollowKnight.controller.GameKeypad;
import com.Ap.HollowKnight.controller.ScreenManager;
import com.Ap.HollowKnight.controller.SettingsController;
import com.Ap.HollowKnight.view.AssetLoader;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.I18NBundle;

import java.util.Locale;

public class SettingsScreen extends BaseScreen {
    private static int DEFAULT = 8 ;
    private final SettingsController controller = SettingsController.getInstance();
    private Table keyTable ;
    private Image topImage;
    private Image bottomImage;
    private GameKeypad waitingForKeypad = null;
    private TextButton waitingButton = null;
    private InputAdapter keyRebinder;
    public SettingsScreen(HollowKnight game) {
        super(game);
        keyTable = createKeypadTable();
        keyTable.setVisible(false);
    }

    @Override
    public void show() {
        super.show();
        keyRebinder = new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                if (waitingForKeypad != null && waitingButton != null) {
                    if (keycode != Input.Keys.ESCAPE) {
                        waitingForKeypad.setKeyNumber(keycode);
                        waitingButton.setText(Input.Keys.toString(keycode));
                    } else {
                        waitingButton.setText(Input.Keys.toString(waitingForKeypad.getKeyNumber()));
                    }
                    waitingForKeypad = null;
                    waitingButton = null;
                    return true;
                }
                return false;
            }
        };
        inputMultiplexer.addProcessor(0,keyRebinder);
        topImage = new Image(AssetLoader.getInstance().getTexture("Ui/TableTop.png"));
        bottomImage = new Image(AssetLoader.getInstance().getTexture("Ui/TableBottom.png"));
        keyTable = createKeypadTable();
        keyTable.setVisible(false);
        batch = new SpriteBatch();
        rootTable.center();
        rootTable.setFillParent(true);
        stage.addActor(keyTable);
        rootTable.add(topImage).colspan(3).center().padBottom(20).row();

        I18NBundle bundle = game.getBundle();

        int currentMusic = controller.getMusicLevel();
        TextButton musicButton = new TextButton(currentMusic == 0 ? bundle.get("settings_music_off") : bundle.get("settings_music_vol"), buttonStyle);
        TextButton musicValue = new TextButton(String.valueOf(currentMusic), buttonStyle);
        Slider musicSlider = new Slider(0, 10, 1, false, sliderStyle);
        musicSlider.setValue(currentMusic);
        musicSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event , Actor actor){
                int amount = (int) musicSlider.getValue();
                controller.changeMusicVolume(amount);
                musicValue.setText(String.valueOf(amount));
                if(amount == 0){
                    musicButton.setText(bundle.get("settings_music_off"));
                }
                else {
                    musicButton.setText(bundle.get("settings_music_vol"));
                }
            }

        });
        setupButton(musicButton,()->{
            if(musicButton.getText().toString().equals(bundle.get("settings_music_vol"))) {
                musicButton.setText(bundle.get("settings_music_off"));
                controller.changeMusicVolume(0);
                musicSlider.setValue(0);
            }
            else {
                musicButton.setText(bundle.get("settings_music_vol"));
                controller.changeMusicVolume(1);
                musicSlider.setValue(1);
            }
        });


        int currentSfx = controller.getSfxLevel();
        TextButton sfxButton = new TextButton(currentSfx == 0 ? bundle.get("settings_sfx_off") : bundle.get("settings_sfx_vol"), buttonStyle);
        Slider sfxSlider = new Slider(0, 10, 1, false, sliderStyle);
        sfxSlider.setValue(currentSfx);
        TextButton sfxValue = new TextButton(String.valueOf(currentSfx), buttonStyle);

        sfxSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                int amount = (int) sfxSlider.getValue();
                controller.changeSfxVolume(amount);
                sfxValue.setText(String.valueOf(amount));

                if (amount == 0) {
                    sfxButton.setText(bundle.get("settings_sfx_off"));
                } else {
                    sfxButton.setText(bundle.get("settings_sfx_vol"));
                }
            }
        });
        setupButton(sfxButton, () -> {
            if (sfxButton.getText().toString().equals(bundle.get("settings_sfx_vol"))) {
                sfxSlider.setValue(0);
            } else {
                sfxSlider.setValue(8);
            }
        });

        int currentBrightness = controller.getBrightnessLevel();
        TextButton brightnessButton = new TextButton(bundle.get("settings_brightness"), buttonStyle);
        TextButton brightnessValue = new TextButton(String.valueOf(currentBrightness), buttonStyle);
        Slider brightnessSlider = new Slider(0, 10, 1, false, sliderStyle);
        brightnessSlider.setValue(currentBrightness);
        brightnessSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event , Actor actor){
                int amount = (int) brightnessSlider.getValue();
                controller.changeBrightness(amount);
                brightnessValue.setText(String.valueOf(amount));
            }
        });

        TextButton resetSounds = new TextButton(bundle.get("settings_reset_sound"), buttonStyle);
        setupButton(resetSounds,()->{
            controller.resetSounds();
            musicSlider.setValue(8);
            sfxSlider.setValue(8);
            musicValue.setText("8");
            sfxValue.setText("8");
            musicButton.setText(bundle.get("settings_music_vol"));
            sfxButton.setText(bundle.get("settings_sfx_vol"));
        });

        TextButton changeGamePlayButton = new TextButton(bundle.get("settings_change_keypad"), buttonStyle);
        setupButton(changeGamePlayButton,()->{
            keyTable.remove();

            keyTable = createKeypadTable();
            stage.addActor(keyTable);

            rootTable.setVisible(false);
            keyTable.setVisible(true);
        });
        TextButton resetGamePlay =  new TextButton(bundle.get("settings_reset_keypad"), buttonStyle);
        setupButton(resetGamePlay,()->{
            GameKeypad.resetAll();

        });
        TextButton changeLanguageButton = new TextButton(bundle.get("settings_change_language"), buttonStyle);
        setupButton(changeLanguageButton,()->{
            FileHandle baseFileHandle = Gdx.files.internal("language/strings");
            if (game.getBundle().getLocale().getLanguage().equals("tr")) {
                game.setMyBundle(I18NBundle.createBundle(baseFileHandle, Locale.ENGLISH));
            } else {
                game.setMyBundle(I18NBundle.createBundle(baseFileHandle, new Locale("tr")));
            }
            ScreenManager.getInstance().setScreen("SettingsScreen");
        });

        TextButton changeThemeButton = new TextButton(bundle.get("settings_change_theme"), buttonStyle);
        setupButton(changeThemeButton,()->{
            backgroundIndex++;
            if (backgroundIndex >= backgrounds.length) {
                backgroundIndex = 0;
            }
            super.background = backgrounds[backgroundIndex];
        });
        TextButton backButton = new TextButton(bundle.get("btn_back"), buttonStyle);
        setupButton(backButton,()->{
            String returnScreen = controller.getPreviousScreen();
            ScreenManager.getInstance().setScreen(returnScreen);
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
        rootTable.add(resetGamePlay).center().colspan(3).padBottom(20).row();
        rootTable.add(changeLanguageButton).center().colspan(3).padTop(40).row();
        rootTable.add(changeThemeButton).center().colspan(3).padTop(20).row();
        rootTable.add(backButton).center().colspan(3).padTop(40).row();


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

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

    private Table createKeypadTable() {
        I18NBundle bundle = game.getBundle();
        Table table = new Table();
        table.setFillParent(true);
        Label title = new Label(bundle.get("settings_game_keypad"), labelStyle);
        TextButton backButton = new TextButton(bundle.get("btn_back"), buttonStyle);
        setupButton(backButton,()->{
            table.setVisible(false);
            rootTable.setVisible(true);
        });
        title.setFontScale(1.25f);
        table.center().top();
        Image topDecor = new Image(AssetLoader.getInstance().getTexture("Ui/TableTop.png"));
        table.add(topDecor).colspan(2).center().padTop(20).padBottom(10).row();
        table.add(title).colspan(2).padTop(10).expandY().row();
        for(GameKeypad key : GameKeypad.values()) {
            Label nameLabel = new Label(key.name() + ":", labelStyle);
            TextButton keyButton = new TextButton(Input.Keys.toString(key.getKeyNumber()), buttonStyle);
            setupButton(keyButton, () -> {
                if (waitingButton != null && waitingForKeypad != null) {
                    waitingButton.setText(Input.Keys.toString(waitingForKeypad.getKeyNumber()));
                }
                waitingForKeypad = key;
                waitingButton = keyButton;
                keyButton.setText(bundle.get("settings_press_key"));
            });
            table.add(nameLabel).align(Align.left).padRight(40).padBottom(10);
            table.add(keyButton).align(Align.right).padBottom(10).row();
        }
        TextButton backBtn = new TextButton(bundle.get("btn_back"), buttonStyle);
        setupButton(backBtn, () -> {
            if (waitingButton != null && waitingForKeypad != null) {
                waitingButton.setText(Input.Keys.toString(waitingForKeypad.getKeyNumber()));
                waitingForKeypad = null;
                waitingButton = null;
            }
            table.setVisible(false);
            rootTable.setVisible(true);
        });
        table.add(backButton).colspan(2).center().padTop(30).row();

        Image bottomDecor = new Image(AssetLoader.getInstance().getTexture("Ui/TableBottom.png"));
        table.add(bottomDecor).colspan(2).center().padTop(10).row();
        return table;
    }
}
