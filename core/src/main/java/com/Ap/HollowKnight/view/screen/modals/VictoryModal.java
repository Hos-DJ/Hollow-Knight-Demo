package com.Ap.HollowKnight.view.screen.modals;


import com.Ap.HollowKnight.controller.ScreenManager;
import com.Ap.HollowKnight.controller.events.GameEvent;
import com.Ap.HollowKnight.controller.events.GameEventMessenger;
import com.Ap.HollowKnight.controller.events.StatisticsListener;
import com.Ap.HollowKnight.view.screen.BaseScreen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class VictoryModal extends Modal {
    public VictoryModal(BaseScreen screen) {
        super(screen);

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(new Color(0.15f, 0.15f, 0.15f, 0.95f));
        pixmap.fill();
        this.setBackground(new TextureRegionDrawable(new Texture(pixmap)));
        pixmap.dispose();

        this.pad(40);
        this.center();

        StatisticsListener stats = StatisticsListener.getInstance();
        int time = stats.getSecondsPassed();
        if(time<=180){
            GameEventMessenger.getInstance().dispatch(GameEvent.SPEED_RUN,null);
        }
        int deaths = stats.getDeathCount();
        int kills = stats.getEnemiesKilled();

        String formattedTime = String.format("%02d:%02d", time / 60, time % 60);

        Label titleLabel = new Label("VICTORY!", screen.getLabelStyle());
        titleLabel.setFontScale(1.8f);
        titleLabel.setColor(Color.GOLD);

        Label timeLabel = new Label("Time Passed: " + formattedTime, screen.getLabelStyle());
        Label deathLabel = new Label("Deaths: " + deaths, screen.getLabelStyle());
        Label killsLabel = new Label("Enemies Killed: " + kills, screen.getLabelStyle());

        TextButton restartBtn = new TextButton("Start Again", screen.getButtonStyle());
        screen.setupButton(restartBtn, () -> {
            hide();
            StatisticsListener.getInstance().resetStats();
            ScreenManager.getInstance().removeScreen("GameScreen");
            ScreenManager.getInstance().setScreen("GameScreen");
        });

        TextButton mainMenuBtn = new TextButton("Main Menu", screen.getButtonStyle());
        screen.setupButton(mainMenuBtn, () -> {
            hide();
            ScreenManager.getInstance().setScreen("MainMenuScreen");
        });

        this.add(titleLabel).padBottom(30).colspan(2).center().row();

        this.add(timeLabel).padBottom(15).colspan(2).center().row();
        this.add(deathLabel).padBottom(15).colspan(2).center().row();
        this.add(killsLabel).padBottom(35).colspan(2).center().row();

        this.add(restartBtn).padRight(30).width(200);
        this.add(mainMenuBtn).width(200);
    }
}
