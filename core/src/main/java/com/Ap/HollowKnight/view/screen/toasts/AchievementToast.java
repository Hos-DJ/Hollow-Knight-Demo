package com.Ap.HollowKnight.view.screen.toasts;

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

        String name = achievement.name().replace("_", " ");
        Label title = new Label("ACHIEVEMENT UNLOCKED", labelStyle);
        title.setFontScale(0.7f);
        title.setColor(Color.GOLD);

        Label desc = new Label(name, labelStyle);
        desc.setFontScale(0.9f);

        this.add(icon).size(80, 80).pad(15);

        Table textTable = new Table();
        textTable.add(title).align(Align.left).row();
        textTable.add(desc).align(Align.left).padTop(5);
        this.add(textTable).padRight(20).align(Align.center);

        this.getColor().a = 0f;
        this.addAction(Actions.sequence(
            Actions.fadeIn(0.5f, Interpolation.fade),
            Actions.delay(3.0f),
            Actions.fadeOut(0.5f, Interpolation.fade),
            Actions.removeActor()
        ));
    }
}
