package com.Ap.HollowKnight.view.screen;

import com.Ap.HollowKnight.HollowKnight;
import com.Ap.HollowKnight.controller.AchievementManager;
import com.Ap.HollowKnight.controller.ScreenManager;
import com.Ap.HollowKnight.view.AchievementsAssets;
import com.Ap.HollowKnight.view.AssetLoader;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.I18NBundle;

public class AchievementsScreen extends BaseScreen {
    private Label.LabelStyle littleLabelStyle;
    private AchievementManager manager;
    public AchievementsScreen(HollowKnight game) {
        super(game);
        littleLabelStyle = new Label.LabelStyle();
        littleLabelStyle.font = loader.getFont("font_14");
        littleLabelStyle.fontColor = Color.WHITE;
        manager = AchievementManager.getInstance();
    }

    @Override
    public void show() {
        super.show();
        batch = new SpriteBatch();
        rootTable.center();
        rootTable.setFillParent(true);
        I18NBundle bundle = game.getBundle();
        Label titleLabel = new Label(bundle.get("achiev_title"), labelStyle);
        titleLabel.setFontScale(2.5f);
        Image topImage = new Image(AssetLoader.getInstance().getTexture("Ui/TableTop.png"));
        Image bottomImage = new Image(AssetLoader.getInstance().getTexture("Ui/TableBottom.png"));
        rootTable.add(topImage).colspan(2).center().padBottom(10).row();
        rootTable.add(titleLabel).colspan(2).center().padBottom(40).row();

        Image gameCompleted = new Image(AssetLoader.getInstance().getAchievement(AchievementsAssets.GAME_COMPLETION));
        Image bossKilled = new  Image(AssetLoader.getInstance().getAchievement(AchievementsAssets.DEFEAT_BOSS));
        Image secretDiscovered = new Image (AssetLoader.getInstance().getAchievement(AchievementsAssets.SECRET_DISCOVERED));
        Image trueHunter = new Image (AssetLoader.getInstance().getAchievement(AchievementsAssets.TRUE_HUNTER));
        Image speedrun = new Image (AssetLoader.getInstance().getAchievement(AchievementsAssets.SPEEDRUN));

        addAchievementToTable(gameCompleted,
            bundle.get("achiev_game_completed"),
            AchievementsAssets.GAME_COMPLETION);

        addAchievementToTable(bossKilled,
            bundle.get("achiev_defeat_boss"),
            AchievementsAssets.DEFEAT_BOSS);

        addAchievementToTable(secretDiscovered,
            bundle.get("achiev_secret_discovered"),
            AchievementsAssets.SECRET_DISCOVERED);

        addAchievementToTable(trueHunter,
            bundle.get("achiev_true_hunter"),
            AchievementsAssets.TRUE_HUNTER);

        addAchievementToTable(speedrun,
            bundle.get("achiev_speedrun"),
            AchievementsAssets.SPEEDRUN);
        rootTable.add(bottomImage).colspan(2).center().padTop(10).row();
        TextButton resetButton = new TextButton(bundle.get("achiev-reset"),buttonStyle);
        setupButton(resetButton,()->{
            manager.resetAllAchievements();
            ScreenManager.getInstance().setScreen("AchievementsScreen");
        });
        rootTable.add(resetButton).colspan(2).center().padTop(5).padBottom(10).row();
        TextButton backButton = new TextButton(bundle.get("btn_back"), buttonStyle);
        setupButton(backButton,()->{
            ScreenManager.getInstance().setScreen("MainMenuScreen");
        });
        rootTable.add(backButton).colspan(2).center().padBottom(30).row();
    }

    private void addAchievementToTable(Image image, String text , AchievementsAssets as) {
        rootTable.add(image).align(Align.left).padTop(20);
        Label label = new Label(text, labelStyle);
        if(!manager.isUnlocked(as)){
            image.setColor(0.2f, 0.2f, 0.2f, 0.5f);
            label.setColor(Color.DARK_GRAY);
        }
        rootTable.add(label).align(Align.left).padTop(20).padLeft(30).row();
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
