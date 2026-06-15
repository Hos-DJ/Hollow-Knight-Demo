package com.Ap.HollowKnight.model.player;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import java.util.Map;

public class Knight {
    private Skin skin;
    private PlayerCondition playerCondition = PlayerCondition.IDLE;
    Map<PlayerCondition, Animation<TextureRegion>> animations;
    private int currentMasks= 5;
    private int currentSoul = 99;
    private boolean isItCoolDown = false;


}
