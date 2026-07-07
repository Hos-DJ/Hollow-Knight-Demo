package com.Ap.HollowKnight.view.screen.modals;

import com.Ap.HollowKnight.controller.InventoryController;
import com.Ap.HollowKnight.model.player.Charm;
import com.Ap.HollowKnight.view.AssetLoader;
import com.Ap.HollowKnight.view.screen.BaseScreen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

public class InventoryModal extends Modal{
    private final InventoryController inventoryController;
    private final TextButton[] equippedCharmButtons = new TextButton[3];
    private final TextButton[] availableCharmButtons = new TextButton[Charm.values().length];
    private final Label descriptionLabel;
    public InventoryModal(BaseScreen screen) {
        super(screen);
        this.inventoryController = new InventoryController();
        this.center();
        this.setTransform(true);
        Label equippedLabel = new Label("--- EQUIPPED CHARMS ---", screen.getLabelStyle());
        equippedLabel.setColor(Color.WHITE);
        this.add(equippedLabel).colspan(3).padBottom(20).row();
        Table equippedTable = new Table();
        for (int i = 0; i < 3; i++) {
            final int notchIndex = i;
            equippedCharmButtons[i] = new TextButton("EMPTY", screen.getButtonStyle());
            screen.setupButton(equippedCharmButtons[i], () -> unequipFromNotch(notchIndex));
            equippedTable.add(equippedCharmButtons[i]).width(280f).height(60f).space(50f);
        }this.add(equippedTable).colspan(3).padBottom(40).row();
        Label availableLabel = new Label("--- AVAILABLE CHARMS ---", screen.getLabelStyle());
        this.add(availableLabel).colspan(3).padBottom(20).row();

        Table availableTable = new Table();
        Charm[] charms = Charm.values();
        for (int i = 0; i < charms.length; i++) {
            final Charm charm = charms[i];

            String displayName = charm.name().replace("_", " ");
            availableCharmButtons[i] = new TextButton(displayName, screen.getButtonStyle());

            screen.setupButton(availableCharmButtons[i], () -> equipSelectedCharm(charm));

            availableCharmButtons[i].addListener(new ClickListener() {
                @Override
                public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                    super.enter(event, x, y, pointer, fromActor);
                    descriptionLabel.setText(getCharmDescription(charm));
                }

                @Override
                public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                    super.exit(event, x, y, pointer, toActor);
                    descriptionLabel.setText("Hover over a charm to see its description.");
                }
            });
            availableTable.add(availableCharmButtons[i]).width(280f).height(60f).space(25f);

            if (i==3) {
                availableTable.row().padTop(15);
            }
        }
        this.add(availableTable).colspan(3).padBottom(40).row();
        descriptionLabel = new Label("Hover over a charm to see its description.", screen.getLabelStyle());
        descriptionLabel.setColor(Color.CYAN);
        descriptionLabel.setWrap(true);
        descriptionLabel.setAlignment(Align.center);
        this.add(descriptionLabel).width(800f).padLeft(40).height(120f).top().center().row();
        updateUi();

    }
    @Override
    public void show() {
        super.show();
        updateUi();
    }

    private void equipSelectedCharm(Charm charm) {
        boolean success = inventoryController.equipCharm(charm);
        if(success){
            updateUi();
        }else if(inventoryController.isEquipped(charm)){
            descriptionLabel.setText("Charm already equipped.");
        }else {
            descriptionLabel.setText("Charm Notches are full");
        }
    }

    private void unequipFromNotch(int notchIndex) {
        Charm[] charms = inventoryController.getActiveCharms();
        if(charms[notchIndex] != null){
            inventoryController.unequipCharm(charms[notchIndex]);
            updateUi();
        }
    }

    private void updateUi(){
        Charm[] active = inventoryController.getActiveCharms();

        for (int i = 0; i < 3; i++) {
        equippedCharmButtons[i].clearChildren();

            if (active[i] != null) {
            Texture charmTex = AssetLoader.getInstance().getTexture("Ui/Charms/"+active[i].name()+".png");
            Image charmImage = new Image(charmTex);

            equippedCharmButtons[i].add(charmImage).size(80f, 80f).center();
            equippedCharmButtons[i].setColor(Color.WHITE);
            } else {
            Label emptyLabel = new Label("EMPTY", baseScreen.getLabelStyle());
            emptyLabel.setFontScale(1.1f);
            equippedCharmButtons[i].add(emptyLabel).center();
            equippedCharmButtons[i].setColor(Color.GRAY);
            }
        }

        Charm[] allCharms = Charm.values();
        for (int i = 0; i < allCharms.length; i++) {
        availableCharmButtons[i].clearChildren();

        Texture charmTex = AssetLoader.getInstance().getTexture("Ui/Charms/" + allCharms[i].name() + ".png");
        Image charmImage = new Image(charmTex);
        availableCharmButtons[i].add(charmImage).size(80f, 80f).center();

            if (inventoryController.isEquipped(allCharms[i])) {
                availableCharmButtons[i].setColor(Color.DARK_GRAY);
            charmImage.setColor(0.3f, 0.3f, 0.3f, 0.6f);
            } else {
                availableCharmButtons[i].setColor(Color.WHITE);
            }
        }
    }

    private String  getCharmDescription(Charm charm) {
        return switch (charm) {
            case SOUL_CATCHER ->
                "«Thirst of the Blade»\nWith every successful strike upon the foe, the Katana siphons their spiritual lifeblood (SOUL), fueling the warrior's inner resolve.";

            case DASH_MASTER ->
                "«The Boundless Wind»\nA true samurai leaves no trace. The intervals between your lightning-fast evasions (Dash Cooldown) are cut in half.";

            case UNBREAKABLE_STRENGTH ->
                "«Honor of Unyielding Steel»\nThe warrior's spirit binds with their weapon. The damage of your regular Nail strikes is heavily augmented.";

            case QUICK_SLASH ->
                "«The Ruthless Katana Dance»\nA storm of strikes that leaves the opponent no room to breathe. Your attack cooldown vanishes, cleaving the air itself.";

            case QUICK_FOCUS ->
                "«Meditation Amidst the Howling Storm»\nChannelling SOUL to heal in the heat of a fatal duel happens faster than ever. The time required to stand still and mend is shortened.";

            case HEAVY_BLOW ->
                "«The Iron Weight of Bushido»\nThe crushing impact of your Nail shatters the enemy's stance (Knockback), repelling them like autumn leaves cast into a gale.";

            case SHARP_SHADOW ->
                "«Phantom Born of the Abyss»\nBecome a razor-sharp shadow during your dash. Slip through the very flesh of your enemies, wounding them while extending your stride.";

            case VOID_HEART ->
                "«Absolute Zen of the Void»\nOne with the eternal darkness. The power of all your samurai arts (Spells) is elevated by 50%, awakening their ancient, dark forms.";
        };
    }

}
