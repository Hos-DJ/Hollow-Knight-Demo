package com.Ap.HollowKnight.view.animations;

import com.badlogic.gdx.graphics.g2d.Animation;

public enum EffectAnimationType implements AnimationType {
    BLAST_SOUL("animation/Effects/BlastSoul.png", 8, 1, 8, Animation.PlayMode.NORMAL),
    CRYSTAL_LASER("animation/Effects/Crystal_extended.png", 24, 1, 24, Animation.PlayMode.NORMAL),
    DASH_EFFECT("animation/Effects/Dash Effect.png", 8, 1, 8, Animation.PlayMode.NORMAL),
    LASER_CIRCLE("animation/Effects/LaserCircle.png", 4, 1, 4, Animation.PlayMode.NORMAL),
    NAIL_DOWN_SLASH("animation/Effects/DownSlashEffect.png", 6, 1, 6, Animation.PlayMode.NORMAL),
    NAIL_SLASH("animation/Effects/SlashEffect.png", 6, 1, 6, Animation.PlayMode.NORMAL),
    NAIL_SLASH_ALT("animation/Effects/SlashEffectAlt.png", 6, 1, 6, Animation.PlayMode.NORMAL),
    NAIL_UP_SLASH("animation/Effects/UpSlashEffect.png", 6, 1, 6, Animation.PlayMode.NORMAL),
    SHADOW_SCREAM("animation/Effects/ShadowScream.png", 13, 1, 14, Animation.PlayMode.NORMAL),
    SOUL_SCREAM("animation/Effects/SoulScream.png", 13, 1, 13, Animation.PlayMode.NORMAL),
    SOUL_BALL("animation/Projectile/SoulBall.png",4,1,4, Animation.PlayMode.NORMAL);
    private final Spec spec;

    EffectAnimationType(String path, int frameCount, int rowCount, int colCount, Animation.PlayMode playMode) {
        this.spec = new Spec(path, frameCount, rowCount, colCount, playMode);
    }

    @Override
    public Spec getSpec() {
        return spec;
    }
}
