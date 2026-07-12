package com.Ap.HollowKnight.view.screen;

import com.Ap.HollowKnight.HollowKnight;
import com.Ap.HollowKnight.controller.GameKeypad;
import com.Ap.HollowKnight.controller.SaveManager;
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
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.I18NBundle;


public class MainMenuScreen extends BaseScreen {
    private Table guideTable;
    private Table loadTable;
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
        this.loadTable = loadGameTable();
        loadTable.setVisible(false);
        stage.addActor(loadTable);
        mainMenuGlowingDots.start();
        I18NBundle bundle = game.getBundle();
        TextButton startBtn = new TextButton(bundle.get("menu_start"), buttonStyle);
        setupButton(startBtn,()->{
            rootTable.setVisible(false);
            loadTable.setVisible(true);
        });
        TextButton achievementBtn = new TextButton(bundle.get("menu_achievements"), buttonStyle);
        setupButton(achievementBtn,()->{
            ScreenManager.getInstance().setScreen("AchievementsScreen");
        });
        TextButton guideBtn = new TextButton(bundle.get("menu_guide"), buttonStyle);
        setupButton(guideBtn,()->{
            rootTable.setVisible(false);
            guideTable.setVisible(true);
        });
        TextButton settingsBtn = new TextButton(bundle.get("menu_settings"), buttonStyle);
        setupButton(settingsBtn,()->{

        });
        TextButton exitBtn = new TextButton(bundle.get("menu_exit"), buttonStyle);
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
        I18NBundle bundle = game.getBundle();
        Table table = new Table();
        table.padTop(20);
        Label controlTitle = new Label(bundle.get("guide_controls"),labelStyle);
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
        Label abilityTitle = new Label(bundle.get("guide_abilities"),labelStyle);
        abilityTitle.setColor(Color.WHITE);
        table.add(abilityTitle).padTop(20).row();
        String text = bundle.get("ability_health") + "\n\n" + bundle.get("ability_soul") + "\n\n" + bundle.get("ability_focus") + "\n\n" + bundle.get("ability_dash");
        Label mechanicsLabel = new Label(text, labelStyle);
        mechanicsLabel.setWrap(true);
        mechanicsLabel.setAlignment(Align.left);
        table.add(mechanicsLabel).width(600f).align(Align.left).padBottom(20).row();

        Label cheatsTitle = new Label(bundle.get("guide_cheats"), labelStyle);
        cheatsTitle.setColor(Color.GOLD);
        table.add(cheatsTitle).padTop(20).padBottom(15).row();
        String actKey = Input.Keys.toString(GameKeypad.CHEAT_ACTIVATOR.getKeyNumber());

        String[][] cheatCodes = {
            {bundle.get("cheat_god"),       actKey + " + " + Input.Keys.toString(GameKeypad.CHEAT_GOD.getKeyNumber())},
            {bundle.get("cheat_soul"),      actKey + " + " + Input.Keys.toString(GameKeypad.CHEAT_SOUL.getKeyNumber())},
            {bundle.get("cheat_boss"),      actKey + " + " + Input.Keys.toString(GameKeypad.CHEAT_BOSS.getKeyNumber())},
            {bundle.get("cheat_health"),    actKey + " + " + Input.Keys.toString(GameKeypad.CHEAT_EMERGENCY_HEALTH.getKeyNumber())},
            {bundle.get("cheat_spectator"), actKey + " + " + Input.Keys.toString(GameKeypad.CHEAT_SPECTATOR.getKeyNumber())},
            {bundle.get("cheat_one_hit"),   actKey + " + " + Input.Keys.toString(GameKeypad.CHEAT_DEADLY_NAIL.getKeyNumber())}
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
        TextButton backButton = new TextButton(bundle.get("btn_back"), buttonStyle);
        setupButton(backButton, () -> {
            mainContainer.setVisible(false);
            rootTable.setVisible(true);
        });
        mainContainer.add(backButton).padTop(20).padBottom(20).row();

        return mainContainer;
    }

    private Table loadGameTable() {
        I18NBundle bundle = game.getBundle();
        Table saveMenuTable = new Table();
        saveMenuTable.setFillParent(true);
        saveMenuTable.top();

        Image topDecor = new Image(AssetLoader.getInstance().getTexture("Ui/TableTop.png"));
        Image bottomDecor = new Image(AssetLoader.getInstance().getTexture("Ui/TableBottom.png"));

        saveMenuTable.add(topDecor).padTop(40).padBottom(30).row();

        Table slotsContainer = new Table();

        for (int i = 1; i <= 4; i++) {
            final int slotNum = i;

            Table rowContainer = new Table();
            rowContainer.padBottom(15).padRight(70);

            TextButton slotBtn = new TextButton("Save Slot " + slotNum, buttonStyle);
            slotBtn.getLabel().setFontScale(1.1f);

            setupButton(slotBtn, () -> {
                System.out.println("Loading game from slot " + slotNum + "...");
                SaveManager.getInstance().setSlotInPending(slotNum);
                audioManager.stopMusic();
                ScreenManager.getInstance().removeScreen("GameScreen");
                ScreenManager.getInstance().setScreen("GameScreen");
            });

            TextButton clearBtn = new TextButton("CLEAR", buttonStyle);
            clearBtn.getLabel().setFontScale(0.85f);

            setupButton(clearBtn, () -> {
                SaveManager.getInstance().clearSaveSlot(slotNum);
                slotBtn.setText("Save Slot " + slotNum + " (Empty)");
            });

            rowContainer.add(slotBtn).width(480).height(90).padRight(170);
            rowContainer.add(clearBtn).width(130).height(80).align(Align.center);

            slotsContainer.add(rowContainer).row();
        }

        saveMenuTable.add(slotsContainer).padBottom(30).row();

        TextButton backBtn = new TextButton(bundle.get("btn_back"), buttonStyle);
        setupButton(backBtn, () -> {
            saveMenuTable.setVisible(false);
            rootTable.setVisible(true);
        });

        saveMenuTable.add(backBtn).padBottom(20).row();
        saveMenuTable.add(bottomDecor).padBottom(40).row();

        return saveMenuTable;
    }
}
