package com.Ap.HollowKnight.view.screen;

import com.Ap.HollowKnight.HollowKnight;
import com.Ap.HollowKnight.controller.ScreenManager;
import com.Ap.HollowKnight.controller.SettingsController;
import com.Ap.HollowKnight.view.AssetLoader;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;

public class SettingsScreen extends BaseScreen {
    private final SettingsController controller = new SettingsController();
    public SettingsScreen(HollowKnight game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();
        batch = new SpriteBatch();
        rootTable.center();
        rootTable.setFillParent(true);
        Image topImage = new Image(AssetLoader.getInstance().getTexture("Ui/TableTop.png"));
        rootTable.add(topImage).colspan(3).center().padBottom(20).row();

        // ۳. ردیف دوم: بخش اصلی منو (۳ ستونه و بزرگترین ردیف)
        // نمونه یک سطر از تنظیمات (می‌توانی سطرها را بیشتر کنی)
        TextButton musicButton = new TextButton("MUSIC VOLUME:", buttonStyle);
        setupButton(musicButton,()->{
            musicButton.setText("MUSIC OFF");
        });
        Slider musicSlider = new Slider(0, 10, 1, false, sliderStyle);
        TextButton musicValue = new TextButton("10", buttonStyle);

        TextButton sfxButton = new TextButton("SFX:", buttonStyle);
        Slider sfxSlider = new Slider(0, 10, 1, false, sliderStyle);
        TextButton sfxValue = new TextButton("10", buttonStyle);

        TextButton resetSounds = new TextButton("RESET SOUND", buttonStyle);

        TextButton brightnessButton = new TextButton("BRIGHTNESS:", buttonStyle);
        Slider brightnessSlider = new Slider(0, 10, 1, false, sliderStyle);
        TextButton brightnessValue = new TextButton("10", buttonStyle);

        TextButton changeGamePlayButton = new TextButton("CHANGE GAME KEYPAD", buttonStyle);
        TextButton backButton = new TextButton("BACK", buttonStyle);
        setupButton(backButton,()->{
            ScreenManager.getInstance().setScreen("MainMenuScreen");
        });


        // expandY() در این ردیف باعث می‌شود این بخش کل فضای عمودی باقی‌مانده را تصاحب کند
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
}
