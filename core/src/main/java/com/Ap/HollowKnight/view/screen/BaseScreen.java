package com.Ap.HollowKnight.view.screen;

import com.Ap.HollowKnight.HollowKnight;
import com.Ap.HollowKnight.controller.SettingsController;
import com.Ap.HollowKnight.view.AssetLoader;
import com.Ap.HollowKnight.view.AudioManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public abstract class BaseScreen implements Screen {
    protected final HollowKnight game;
    private SettingsController settingsController;
    protected Stage stage;
    protected Table rootTable;
    protected AudioManager audioManager;
    private Texture brightnessTexture;
    protected Stack mainStack;
    protected Stack modalStack;
    protected Stack toastStack;
    protected Texture background;
    protected Image gameLogo;
    protected SpriteBatch batch;
    protected Sound clickSound;
    protected Sound hoverSound;
    protected AssetLoader loader;
    protected ParticleEffect mainMenuGlowingDots;
    protected Slider.SliderStyle sliderStyle;
    protected TextButton.TextButtonStyle  buttonStyle;
    protected Label.LabelStyle labelStyle;
    protected InputMultiplexer inputMultiplexer;

    protected BaseScreen(HollowKnight game) {
        loader = AssetLoader.getInstance();
        hoverSound = loader.getSound("Ui/Audio/button-hover.wav");
        clickSound = loader.getSound("Ui/Audio/button-click.wav");
        this.mainMenuGlowingDots = new ParticleEffect();
        this.audioManager = AudioManager.getInstance();
        this.background = new Texture("Ui/Menu_Theme_Surface.png");
        mainMenuGlowingDots.load(Gdx.files.internal("Ui/MainMenuParticle.p"),Gdx.files.internal("Ui"));
        buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = loader.getFont("font_24");
        buttonStyle.fontColor = Color.LIGHT_GRAY;
        buttonStyle.overFontColor = Color.WHITE;
        labelStyle = new Label.LabelStyle();
        labelStyle.font = loader.getFont("font_24");
        labelStyle.fontColor = Color.WHITE;
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.BLACK);
        pixmap.fill();
        brightnessTexture = new Texture(pixmap);
        pixmap.dispose();
        this.game = game;
        sliderStyle = getSliderStyle();
        settingsController = SettingsController.getInstance();
    }

    private Slider.SliderStyle getSliderStyle() {
        TextureAtlas atlas = loader.getAtlas("Ui/Slider/slider.atlas");
        TextureRegionDrawable bgDrawable = new TextureRegionDrawable(atlas.findRegion("slider_white"));
        bgDrawable.setMinHeight(2f);
        TextureRegionDrawable knobDrawable = new TextureRegionDrawable(atlas.findRegion("slider_arrow"));
        Slider.SliderStyle sliderStyle = new Slider.SliderStyle(bgDrawable, knobDrawable);
        return sliderStyle;
    }

    protected void setupButton(TextButton button, Runnable clickAction) {
        button.setTransform(true);
        button.setOrigin(Align.center);

        button.addListener(new ClickListener() {

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                super.enter(event, x, y, pointer, fromActor);
                button.addAction(Actions.scaleTo(1.15f, 1.15f, 0.1f, Interpolation.sineOut));
                if (hoverSound != null) hoverSound.play();
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                super.exit(event, x, y, pointer, toActor);
                button.addAction(Actions.scaleTo(1.0f, 1.0f, 0.1f, Interpolation.sineOut));
            }

            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (clickSound != null) clickSound.play();

                if (clickAction != null) {
                    clickAction.run();
                }
            }
        });
    }
    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());

        mainStack = new Stack();
        mainStack.setFillParent(true);

        rootTable = new Table();
        modalStack = new Stack();
        toastStack = new Stack();

        mainStack.add(rootTable);
        mainStack.add(modalStack);
        mainStack.add(toastStack);

        stage.addActor(mainStack);

        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(stage);
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    protected void addInputProcessor(InputProcessor processor) {
        inputMultiplexer.addProcessor(processor);
    }

    protected void clearScreen(float red, float green, float blue, float alpha) {
        Gdx.gl.glClearColor(red, green, blue, alpha);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void render(float delta) {
        if (stage != null) {
            stage.act(delta);
            stage.draw();
        }
        float brightness = settingsController.getBrightness();
        if(brightness>0.9f)
            brightness=0.9f;
        batch.begin();
        batch.setColor(0f, 0f, 0f, brightness);
        batch.draw(brightnessTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.setColor(Color.WHITE);
        batch.end();

    }

    @Override
    public void resize(int width, int height) {
        if (stage != null) {
            stage.getViewport().update(width, height, true);
        }
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        if (stage != null) {
            stage.dispose();
        }
    }
}
