package com.Ap.HollowKnight.view.screen.modals;

import com.Ap.HollowKnight.controller.GameKeypad;
import com.Ap.HollowKnight.controller.ScreenManager;
import com.Ap.HollowKnight.controller.SettingsController;
import com.Ap.HollowKnight.controller.events.GameEvent;
import com.Ap.HollowKnight.controller.events.GameEventMessenger;
import com.Ap.HollowKnight.view.screen.BaseScreen;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.I18NBundle;

public class PauseModal extends Modal {
    public PauseModal(BaseScreen screen) {
        super(screen);
        I18NBundle bundle = screen.getGame().getBundle();

        this.center();

        Label pauseTitle = new Label(bundle.get("pause_title"), screen.getLabelStyle());
        pauseTitle.setFontScale(2f);

        TextButton resumeBtn = new TextButton(bundle.get("pause_resume"), screen.getButtonStyle());
        screen.setupButton(resumeBtn, () -> {
            hide();
            onResume();
        });

        TextButton settingsBtn = new TextButton(bundle.get("pause_settings"), screen.getButtonStyle());
        screen.setupButton(settingsBtn, this::onSettings);

        TextButton exitBtn = new TextButton(bundle.get("pause_exit"), screen.getButtonStyle());
        screen.setupButton(exitBtn, this::onExit);

        this.add(pauseTitle).padBottom(40).row();
        this.add(resumeBtn).padBottom(15).row();
        this.add(settingsBtn).padBottom(15).row();
        this.add(exitBtn).padBottom(150).row();


        Table cheatTable = new Table();

        Label cheatTitle = new Label("--- CHEAT CODES ---", screen.getLabelStyle());
        cheatTitle.setFontScale(0.8f);
        cheatTitle.setColor(Color.LIGHT_GRAY);
        cheatTable.add(cheatTitle).colspan(2).center().padBottom(15).row();

        String actKey = Input.Keys.toString(GameKeypad.CHEAT_ACTIVATOR.getKeyNumber());

        String[][] cheatCodes = {
            {bundle.get("cheat_god"),       actKey + " + " + Input.Keys.toString(GameKeypad.CHEAT_GOD.getKeyNumber())},
            {bundle.get("cheat_soul"),      actKey + " + " + Input.Keys.toString(GameKeypad.CHEAT_SOUL.getKeyNumber())},
            {bundle.get("cheat_boss"),      actKey + " + " + Input.Keys.toString(GameKeypad.CHEAT_BOSS.getKeyNumber())},
            {bundle.get("cheat_health"),    actKey + " + " + Input.Keys.toString(GameKeypad.CHEAT_EMERGENCY_HEALTH.getKeyNumber())},
            {bundle.get("cheat_spectator"), actKey + " + " + Input.Keys.toString(GameKeypad.CHEAT_SPECTATOR.getKeyNumber())},
            {bundle.get("cheat_one_hit"),   actKey + " + " + Input.Keys.toString(GameKeypad.CHEAT_DEADLY_NAIL.getKeyNumber())}
        };

        for(String[] cheatCode : cheatCodes) {
            String cheatName = cheatCode[0];
            String cheatKey = cheatCode[1];

            Label nameLabel = new Label(cheatName, screen.getLabelStyle());
            nameLabel.setFontScale(0.7f);
            nameLabel.setColor(Color.LIGHT_GRAY);

            Label keyLabel = new Label("[ " + cheatKey + " ]", screen.getLabelStyle());
            keyLabel.setFontScale(0.7f);
            keyLabel.setColor(Color.GOLD);

            cheatTable.add(nameLabel).align(Align.left).padRight(40).padBottom(8);
            cheatTable.add(keyLabel).align(Align.right).padBottom(8).row();
        }

        this.add(cheatTable).center();
    }


    public void onResume() {
    }

    public void onSettings() {
        SettingsController.getInstance().setPreviousScreen("GameScreen");
        SettingsController.getInstance().setReturnToPauseMenu(true);
        ScreenManager.getInstance().setScreen("SettingsScreen");
    }

    public void onExit() {
        GameEventMessenger.getInstance().dispatch(GameEvent.SAVE_GAME, null);
        ScreenManager.getInstance().setScreen("MainMenuScreen");
    }

    @Override
    public void onHide() {
        onResume();
    }
}
