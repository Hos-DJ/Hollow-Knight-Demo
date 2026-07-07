package com.Ap.HollowKnight.view.animations;

import com.badlogic.gdx.graphics.g2d.Animation;

public enum FalseKnightAnimationType implements AnimationType{
    MACE_SLAM("animation/False_knight/Attack.png",3,1,3, Animation.PlayMode.NORMAL),
    CHARGING_THE_MACE("animation/False_knight/Attack Antic.png",6,1,6, Animation.PlayMode.LOOP),
    RETURN_FROM_ATTACK_TO_IDLE("animation/False_knight/Attack Recover.png",5,1,5, Animation.PlayMode.NORMAL),
    STUNNED_BODY("animation/False_knight/Body.png",5,1,5, Animation.PlayMode.LOOP),
    GETTING_STUNNED("animation/False_knight/DeathLand.png",11,1,11, Animation.PlayMode.NORMAL),
    IDLE("animation/False_knight/Idle.png",5,1,5, Animation.PlayMode.LOOP),
    JUMP_DEFENSE("animation/False_knight/Jump.png",4,1,4, Animation.PlayMode.NORMAL),
    JUMP_ATTACK("animation/False_knight/Jump Attack.png",8,1,8, Animation.PlayMode.NORMAL),
    LAND("animation/False_knight/Land.png",5,1,5, Animation.PlayMode.NORMAL),
    RUN("animation/False_knight/Run.png",5,1,5, Animation.PlayMode.LOOP),
    STUN_TO_IDLE("animation/False_knight/Stun Recover.png",6,1,6, Animation.PlayMode.NORMAL);

    private final Spec spec;

    FalseKnightAnimationType(String path, int frameCount, int rowCount, int colCount, Animation.PlayMode playMode) {
        this.spec = new Spec(path, frameCount, rowCount, colCount, playMode);
    }

    @Override
    public Spec getSpec() {
        return spec;
    }
}
