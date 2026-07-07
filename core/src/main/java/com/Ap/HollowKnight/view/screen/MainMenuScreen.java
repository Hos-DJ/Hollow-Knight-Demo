package com.Ap.HollowKnight.view.screen;

import com.Ap.HollowKnight.HollowKnight;
import com.Ap.HollowKnight.controller.GameKeypad;
import com.Ap.HollowKnight.controller.ScreenManager;
import com.Ap.HollowKnight.view.AssetLoader;
import com.Ap.HollowKnight.view.sounds.MusicType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;


public class MainMenuScreen extends BaseScreen {
    private Table guideTable;

    public MainMenuScreen(HollowKnight game) {
        super(game);
    }


    @Override
    public void show() {
        super.show();
        audioManager.playMusic(MusicType.MAIN_THEME);
        this.batch = new SpriteBatch();
        this.gameLogo = new Image(AssetLoader.getInstance().getTexture("Ui/Hollow-Knight-Logo-PNG-Pic.png"));
        this.guideTable = guideMenuTable();
        guideTable.setVisible(false);
        stage.addActor(guideTable);
        mainMenuGlowingDots.start();


        TextButton startBtn = new TextButton("Start Game", buttonStyle);
        setupButton(startBtn,()->{
            System.out.println("game is starting...");
            audioManager.stopMusic();
            ScreenManager.getInstance().setScreen("GameScreen");
        });
        TextButton achievementBtn = new TextButton("Achievement", buttonStyle);
        setupButton(achievementBtn,()->{
            ScreenManager.getInstance().setScreen("AchievementsScreen");
        });
        TextButton guideBtn = new TextButton("Guide", buttonStyle);
        setupButton(guideBtn,()->{
            rootTable.setVisible(false);
            guideTable.setVisible(true);
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

    private Table guideMenuTable(){
        Table table = new Table();
        table.padTop(20);
        Label controlTitle = new Label("---CONTROLS---",labelStyle);
        controlTitle.setColor(Color.WHITE);
        table.add(controlTitle).padTop(10).row();
        for(GameKeypad key : GameKeypad.values()) {
            String keyName = key.name();
            String currentButton = Input.Keys.toString(key.getKeyNumber());

            Label nameLabel = new Label(keyName + " :", labelStyle);
            Label keyLabel = new Label("[ " + currentButton + " ]", labelStyle);

            table.add(nameLabel).align(Align.left).padRight(40).padBottom(5);
            table.add(keyLabel).align(Align.right).padBottom(5).row();
        }
        Label abilityTitle = new Label("---ABILITIES---",labelStyle);
        abilityTitle.setColor(Color.WHITE);
        table.add(abilityTitle).padTop(20).row();
        String text = "-MASKS OF HONOR (HEALTH):\n" +
            "The path of the warrior is unforgiving. Your life is bound to your masks. Every blow you take shatters one. Let the last mask fall, and your soul shall wander the abyss forever.\n\n" +
            "-WAY OF THE BLADE (SOUL SYSTEM):\n" +
            "Your Nail thirsts for combat. With every precise strike upon your foes, you siphon their SOUL. This sacred energy fuels your ancient arts and spells.\n\n" +
            "-MEDITATION IN CHAOS (FOCUS):\n" +
            "A true samurai finds stillness in the storm. Hold the FOCUS button to channel the gathered SOUL, mending your flesh and restoring a shattered mask.\n\n" +
            "-SHADOW STEP (DASH):\n" +
            "Be as the wind and slip through enemy blades. Press the DASH button to move with lightning speed, evading fatal strikes or crossing great chasms.";
        Label mechanicsLabel = new Label(text, labelStyle);
        mechanicsLabel.setWrap(true);
        mechanicsLabel.setAlignment(Align.left);
        table.add(mechanicsLabel).width(600f).align(Align.left).padBottom(20).row();

        Label cheatsTitle = new Label("--- CHEAT CODES ---", labelStyle);
        cheatsTitle.setColor(Color.GOLD);
        table.add(cheatsTitle).padTop(20).padBottom(15).row();

        String[][] cheatCodes = {
            {"God Mode (Invincibility)", "F1"},
            {"Infinite Soul", "F2"},
            {"Instant Kill (One-hit Nail)", "F3"},
            {"Unlock All Abilities", "F4"}
        };

        for (String[] cheat : cheatCodes) {
            String cheatName = cheat[0];
            String cheatKey = cheat[1];

            Label nameLabel = new Label(cheatName + " :", labelStyle);
            Label keyLabel = new Label("[ " + cheatKey + " ]", labelStyle);

            table.add(nameLabel).align(Align.left).padRight(40).padBottom(5);
            table.add(keyLabel).align(Align.right).padBottom(5).row();
        }

        ScrollPane scrollPane = new ScrollPane(table,scrollPaneStyle);

        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollBarPositions(false, true);
        scrollPane.setOverscroll(false, false);

        Table mainContainer = new Table();
        mainContainer.setFillParent(true);

        mainContainer.add(scrollPane).expand().fill()
            .padTop(20)
            .padBottom(20)
            .padLeft(150)
            .padRight(150)
            .row();

        TextButton backButton = new TextButton("BACK", buttonStyle);
        setupButton(backButton, () -> {
            mainContainer.setVisible(false);
            rootTable.setVisible(true);
        });
        mainContainer.add(backButton).padTop(20).padBottom(20).row();

        return mainContainer;
    }
}
