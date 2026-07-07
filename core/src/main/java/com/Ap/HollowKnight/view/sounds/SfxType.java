package com.Ap.HollowKnight.view.sounds;

public enum SfxType {
    // --- PLAYER MOVEMENT & ACTIONS ---
    KNIGHT_FOOTSTEP(""),
    KNIGHT_DASH(""),
    WALL_SLIDE(""),
    FOCUS_CHARGE(""),

    // --- PLAYER COMBAT ---
    NAIL_SLASH(""),
    NAIL_POGO_BOUNCE(""),
    NAIL_HIT_WALL(""),

    // --- SPELLS ---
    CAST_FIREBALL(""),
    CAST_HOWLING(""),

    // --- DAMAGE & IMPACTS ---
    ENEMY_DAMAGE(""),
    KNIGHT_HURT(""),
    KNIGHT_HEAVY_HURT(""),
    MACE_SLAM(""),

    // --- ZOTE SFX ---
    ZOTE_ATTACK(""),
    ZOTE_MUMBLE_1(""),
    ZOTE_MUMBLE_2(""),
    ZOTE_MUMBLE_3(""),
    ZOTE_MUMBLE_4("");

    private final String path;

    SfxType(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
