package com.Ap.HollowKnight.view.screen.modals;

import com.Ap.HollowKnight.controller.InventoryController;
import com.Ap.HollowKnight.model.charms.Charm;
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
import com.badlogic.gdx.utils.I18NBundle;

public class InventoryModal extends Modal{
    private final InventoryController inventoryController;
    private final TextButton[] equippedCharmButtons = new TextButton[3];
    private final TextButton[] availableCharmButtons = new TextButton[Charm.values().length];
    private final Label descriptionLabel;
    public InventoryModal(BaseScreen screen) {
        super(screen);
        I18NBundle bundle = screen.getGame().getBundle();
        this.inventoryController = new InventoryController();
        this.center();
        this.setTransform(true);
        Label equippedLabel = new Label(bundle.get("inv_equipped"), screen.getLabelStyle());
        equippedLabel.setColor(Color.WHITE);
        this.add(equippedLabel).colspan(3).padBottom(20).row();
        Table equippedTable = new Table();
        for (int i = 0; i < 3; i++) {
            final int notchIndex = i;
            equippedCharmButtons[i] = new TextButton(bundle.get("inv_empty"), screen.getButtonStyle());
            screen.setupButton(equippedCharmButtons[i], () -> unequipFromNotch(notchIndex));
            equippedTable.add(equippedCharmButtons[i]).width(280f).height(60f).space(50f);
        }this.add(equippedTable).colspan(3).padBottom(40).row();
        Label availableLabel = new Label(bundle.get("inv_available"), screen.getLabelStyle());
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
                    descriptionLabel.setText(bundle.get("inv_hover_desc"));
                }
            });
            availableTable.add(availableCharmButtons[i]).width(280f).height(60f).space(25f);

            if (i==3) {
                availableTable.row().padTop(15);
            }
        }
        this.add(availableTable).colspan(3).padBottom(40).row();
        descriptionLabel = new Label(bundle.get("inv_hover_desc"), screen.getLabelStyle());
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
        I18NBundle bundle = baseScreen.getGame().getBundle();
        boolean success = inventoryController.equipCharm(charm);
        if(success){
            updateUi();
        }else if(inventoryController.isEquipped(charm)){
            descriptionLabel.setText(bundle.get("inv_already_equipped"));
        }else {
            descriptionLabel.setText(bundle.get("inv_notches_full"));
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
        I18NBundle bundle = baseScreen.getGame().getBundle();
        Charm[] active = inventoryController.getActiveCharms();

        for (int i = 0; i < 3; i++) {
            equippedCharmButtons[i].clearChildren();

            if (active[i] != null) {
                Texture charmTex = AssetLoader.getInstance().getTexture("Ui/Charms/"+active[i].name()+".png");
                Image charmImage = new Image(charmTex);

                equippedCharmButtons[i].add(charmImage).size(80f, 80f).center();
                equippedCharmButtons[i].setColor(Color.WHITE);
            } else {
                Label emptyLabel = new Label(bundle.get("inv_empty"), baseScreen.getLabelStyle());
                emptyLabel.setFontScale(1.1f);
                equippedCharmButtons[i].add(emptyLabel).center();
                equippedCharmButtons[i].setColor(Color.GRAY);
            }
        }

        Charm[] allCharms = Charm.values();
        for (int i = 0; i < allCharms.length; i++) {
            availableCharmButtons[i].clearChildren();

            if (!inventoryController.isUnlocked(allCharms[i])) {
                availableCharmButtons[i].setVisible(false);
                continue;
            }
            availableCharmButtons[i].setVisible(true);

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
        I18NBundle bundle = baseScreen.getGame().getBundle();
        return switch (charm) {
            case SOUL_CATCHER -> bundle.get("charm_soul_catcher");
            case DASH_MASTER -> bundle.get("charm_dash_master");
            case UNBREAKABLE_STRENGTH -> bundle.get("charm_unbreakable_strength");
            case QUICK_SLASH -> bundle.get("charm_quick_slash");
            case QUICK_FOCUS -> bundle.get("charm_quick_focus");
            case HEAVY_BLOW -> bundle.get("charm_heavy_blow");
            case SHARP_SHADOW -> bundle.get("charm_sharp_shadow");
            case VOID_HEART -> bundle.get("charm_void_heart");
        };
    }



}
