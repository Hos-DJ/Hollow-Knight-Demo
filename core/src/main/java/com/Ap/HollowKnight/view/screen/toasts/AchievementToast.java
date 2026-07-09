package com.Ap.HollowKnight.view.screen.toasts;

import com.Ap.HollowKnight.controller.ScreenManager;
import com.Ap.HollowKnight.view.AchievementsAssets;
import com.Ap.HollowKnight.view.AssetLoader;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.I18NBundle;

public class AchievementToast extends Table {

    public AchievementToast(AchievementsAssets achievement, Label.LabelStyle labelStyle) {
        this.setTransform(true);

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(new Color(0.1f, 0.1f, 0.1f, 0.9f));
        pixmap.fill();
        this.setBackground(new TextureRegionDrawable(new Texture(pixmap)));
        pixmap.dispose();

        Texture tex = AssetLoader.getInstance().getAchievement(achievement);
        Image icon = new Image(tex);

        I18NBundle bundle = ScreenManager.getInstance().getGame().getBundle();

        Label title = new Label(bundle.get("toast_unlocked"), labelStyle);
        title.setFontScale(0.7f);
        title.setColor(Color.GOLD);

        String translatedName;
        try {
            String achievementKey = "achiev_" + achievement.name().toLowerCase();
            translatedName = bundle.get(achievementKey).split("\n")[0].replace(":", "");
        } catch (Exception e) {
            translatedName = achievement.name().replace("_", " ");
        }

        Label desc = new Label(translatedName, labelStyle);
        desc.setFontScale(0.9f);

        this.add(icon).size(80, 80).pad(15);

        Table textTable = new Table();
        textTable.add(title).align(Align.left).row();
        textTable.add(desc).align(Align.left).padTop(5);
        this.add(textTable).padRight(20).align(Align.center);

    }
}
