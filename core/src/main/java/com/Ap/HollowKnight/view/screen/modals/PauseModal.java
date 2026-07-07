package com.Ap.HollowKnight.view.screen.modals;

import com.Ap.HollowKnight.controller.ScreenManager;
import com.Ap.HollowKnight.controller.SettingsController;
import com.Ap.HollowKnight.view.screen.BaseScreen;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class PauseModal extends Modal {
    public PauseModal(BaseScreen screen) {
        super(screen);
        Label pauseTitle = new Label("PAUSED", screen.getLabelStyle());
        pauseTitle.setFontScale(2f);

        TextButton resumeBtn = new TextButton("RESUME", screen.getButtonStyle());
        screen.setupButton(resumeBtn, () -> {
            hide();
            onResume();
        });

        TextButton settingsBtn = new TextButton("SETTINGS", screen.getButtonStyle());
        screen.setupButton(settingsBtn, this::onSettings);

        TextButton exitBtn = new TextButton("EXIT TO MENU", screen.getButtonStyle());
        screen.setupButton(exitBtn, this::onExit);

        this.add(pauseTitle).padBottom(40).row();
        this.add(resumeBtn).padBottom(15).row();
        this.add(settingsBtn).padBottom(15).row();
        this.add(exitBtn).padBottom(15).row();
    }


    public void onResume() {
    }

    public void onSettings() {
        SettingsController.getInstance().setPreviousScreen("GameScreen");
        SettingsController.getInstance().setReturnToPauseMenu(true);
        ScreenManager.getInstance().setScreen("SettingsScreen");
    }

    public void onExit() {
        ScreenManager.getInstance().setScreen("MainMenuScreen");
    }

        @Override
    public void onHide() {
        onResume();
    }
}
