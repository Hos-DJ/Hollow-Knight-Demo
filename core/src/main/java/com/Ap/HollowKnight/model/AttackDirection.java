package com.Ap.HollowKnight.model;

import com.Ap.HollowKnight.view.animations.EffectAnimationType;

public enum AttackDirection {
    UP, DOWN, RIGHT, LEFT;

    public EffectAnimationType toAnimationType() {
        return switch (this) {
            case UP -> EffectAnimationType.NAIL_UP_SLASH;
            case DOWN -> EffectAnimationType.NAIL_DOWN_SLASH;
            case RIGHT, LEFT -> EffectAnimationType.NAIL_SLASH;
        };
    }
}
