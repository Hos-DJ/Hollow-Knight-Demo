package com.Ap.HollowKnight.view.animations;

import com.badlogic.gdx.graphics.g2d.Animation;

public enum ZoteAnimationType implements AnimationType{
    ATTACK("animation/Zote/Attack.png",4,1,4, Animation.PlayMode.LOOP),
    REST("animation/Zote/Fall.png",5,1,5, Animation.PlayMode.NORMAL),
    WAKE("animation/Zote/Get Up.png",4,1,4, Animation.PlayMode.NORMAL),
    IDLE ("animation/Zote/Idle.png",5,1,5, Animation.PlayMode.LOOP),
    KNOCK("animation/Zote/Roll.png",3,1,3, Animation.PlayMode.LOOP),
    TALK("animation/Zote/Talk.png",5,1,5, Animation.PlayMode.LOOP),

    ;
    private final Spec spec;
    ZoteAnimationType(String path , int frameCount , int rowCount , int colCount,Animation.PlayMode playMode){
        this.spec = new Spec(path, frameCount, rowCount, colCount, playMode);
    }
    public Spec getSpec(){
        return spec;
    }
}
