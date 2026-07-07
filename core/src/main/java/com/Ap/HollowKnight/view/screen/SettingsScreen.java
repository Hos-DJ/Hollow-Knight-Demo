package com.Ap.HollowKnight.view.screen;

import com.Ap.HollowKnight.HollowKnight;
import com.Ap.HollowKnight.controller.GameKeypad;
import com.Ap.HollowKnight.controller.ScreenManager;
import com.Ap.HollowKnight.controller.SettingsController;
import com.Ap.HollowKnight.view.AssetLoader;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;

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

        int currentMusic = controller.getMusicLevel();
        TextButton musicButton = new TextButton(currentMusic == 0 ? "MUSIC OFF" : "MUSIC VOLUME:", buttonStyle);
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


        int currentSfx = controller.getSfxLevel();
        TextButton sfxButton = new TextButton(currentSfx == 0 ? "SFX OFF" : "SFX VOLUME:", buttonStyle);
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

        int currentBrightness = controller.getBrightnessLevel();
        TextButton brightnessButton = new TextButton("BRIGHTNESS:", buttonStyle);
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

        TextButton resetSounds = new TextButton("RESET SOUND", buttonStyle);
        setupButton(resetSounds,()->{
            controller.resetSounds();
            musicSlider.setValue(8);
            sfxSlider.setValue(8);
            musicValue.setText("8");
            sfxValue.setText("8");
            musicButton.setText("MUSIC VOLUME:");
            sfxButton.setText("SFX VOLUME:");
        });

        TextButton changeGamePlayButton = new TextButton("CHANGE GAME KEYPAD", buttonStyle);
        setupButton(changeGamePlayButton,()->{
            rootTable.setVisible(false);
            keyTable.setVisible(true);
        });
        TextButton backButton = new TextButton("BACK", buttonStyle);
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
        Table table = new Table();
        table.setFillParent(true);
        Label title = new Label("GAME KEYPAD", labelStyle);
        TextButton backButton = new TextButton("BACK", buttonStyle);
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
                keyButton.setText("[ PRESS KEY ]");
            });
            table.add(nameLabel).align(Align.left).padRight(40).padBottom(10);
            table.add(keyButton).align(Align.right).padBottom(10).row();
        }
        TextButton backBtn = new TextButton("BACK", buttonStyle);
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
