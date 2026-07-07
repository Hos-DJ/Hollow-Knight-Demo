package com.Ap.HollowKnight.view.screen.modals;

import com.Ap.HollowKnight.controller.GameKeypad;
import com.Ap.HollowKnight.view.screen.BaseScreen;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class Modal extends Table {
    protected BaseScreen baseScreen;
    private Table wrapperTable;

    public Modal(BaseScreen baseScreen) {
        this.baseScreen = baseScreen;

        wrapperTable = new Table();
        wrapperTable.setFillParent(true);
        wrapperTable.setTouchable(Touchable.enabled);
        this.setTouchable(Touchable.enabled);
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(new Color(0,0,0,0.8f));
        pixmap.fill();
        wrapperTable.setBackground(new TextureRegionDrawable(new Texture(pixmap)));
        pixmap.dispose();
        wrapperTable.add(this);

        wrapperTable.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (event.getTarget() == wrapperTable){
                    hide();
                    onHide();
                }
            }
        });
        wrapperTable.addListener(new InputListener(){
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == GameKeypad.INVENTORY.getKeyNumber() || keycode == Input.Keys.ESCAPE) {
                    return false;
                }
                return true;
            }

            @Override
            public boolean keyUp(InputEvent event, int keycode) {
                return true;
            }
        });
    }
    public void show(){
        baseScreen.getModalStack().add(wrapperTable);
        if (wrapperTable.getStage() != null) {
            wrapperTable.getStage().setKeyboardFocus(wrapperTable);
        }
    }

    public void hide(){
        wrapperTable.remove();
    }

    public void onHide(){

    }
}
