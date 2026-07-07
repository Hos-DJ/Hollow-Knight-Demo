package com.Ap.HollowKnight.view.animations;

import com.badlogic.gdx.graphics.g2d.Animation;

public enum HudAnimationType implements AnimationType{
    EMPTY_MASK("HUD/EmptyHealth.png",1,1,1, Animation.PlayMode.NORMAL),
    FULL_MASK("HUD/FilledHealth.png",1,1,1, Animation.PlayMode.NORMAL),
    BREAK_MASK("HUD/BreakHealth.png",6,1,6, Animation.PlayMode.NORMAL),
    HEALTH_REFILL("HUD/HealthRefill.png",5,1,5, Animation.PlayMode.NORMAL),
    HEALTH_BAR("HUD/HealthBar_005.png", 1,1,1, Animation.PlayMode.NORMAL),
    SOUL_ORB("HUD/SoulOrb_Full.png",1,1,1, Animation.PlayMode.NORMAL);



    private final Spec spec;
    HudAnimationType(String path , int frameCount, int rowCount, int colCount , Animation.PlayMode playMode){
        this.spec = new Spec(path,frameCount,rowCount,colCount,playMode);
    }


    @Override
    public Spec getSpec() {
        return spec;
    }
}
