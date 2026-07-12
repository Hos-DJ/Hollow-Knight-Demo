package com.Ap.HollowKnight.view.sounds;

public enum SfxType {
    KNIGHT_FOOTSTEP("Ui/Audio/SfxMusic/knight_runStone.wav"),
    KNIGHT_DASH("Ui/Audio/SfxMusic/knight_dash.wav"),
    KNIGHT_MONARCH("Ui/Audio/SfxMusic/knight_wings.wav"),
    WALL_SLIDE("Ui/Audio/SfxMusic/knight_slidingWall.wav"),
    FOCUS_CHARGE_BEGIN("Ui/Audio/SfxMusic/focus_health_charging.wav"),
    FOCUS_CHARGE_END("Ui/Audio/SfxMusic/focus_health_heal.wav"),
    SOUL_GAIN_1("Ui/Audio/SfxMusic/soul_pickup_1.wav"),
    SOUL_GAIN_2("Ui/Audio/SfxMusic/soul_pickup_2.wav"),
    SOUL_GAIN_3("Ui/Audio/SfxMusic/soul_pickup_3.wav"),
    SOUL_GAIN_4("Ui/Audio/SfxMusic/soul_pickup_4.wav"),
    SOUL_GAIN_5("Ui/Audio/SfxMusic/soul_pickup_5.wav"),
    SOUL_GAIN_6("Ui/Audio/SfxMusic/soul_pickup_6.wav"),
    SOUL_GAIN_7("Ui/Audio/SfxMusic/soul_pickup_7.wav"),

    NAIL_SLASH("Ui/Audio/SfxMusic/hero_evade.wav"),
    NAIL_POGO_BOUNCE("Ui/Audio/SfxMusic/sword_hit_reject.wav"),


    NAIL_HIT_WALL("Ui/Audio/SfxMusic/breakable_wall_hit_1.wav"),
    WALL_BROKEN("Ui/Audio/SfxMusic/breakable_wall_death.wav"),

    CAST_FIREBALL("Ui/Audio/SfxMusic/knight_fireball.wav"),
    CAST_HOWLING("Ui/Audio/SfxMusic/hero_scream_spell.wav"),

    ENEMY_DAMAGE("Ui/Audio/SfxMusic/enemy_damage.wav"),
    ENEMY_DEATH("Ui/Audio/SfxMusic/enemy_death_sword.wav"),
    KNIGHT_HURT("Ui/Audio/SfxMusic/knight_damage.wav"),
    KNIGHT_HEAVY_HURT("Ui/Audio/SfxMusic/hero_double_damage.wav"),
    MACE_SLAM("Ui/Audio/SfxMusic/breakable_wall_hit_1.wav"),
    KNIGHT_DEATH("Ui/Audio/SfxMusic/knight_death.wav"),

    ZOTE_ATTACK("Ui/Audio/SfxMusic/Zote_battle_attack_loop.wav"),
    ZOTE_MUMBLE_1("Ui/Audio/SfxMusic/Zote_01.wav"),
    ZOTE_MUMBLE_2("Ui/Audio/SfxMusic/Zote_02.wav"),
    ZOTE_MUMBLE_3("Ui/Audio/SfxMusic/Zote_03.wav"),
    ZOTE_MUMBLE_4("Ui/Audio/SfxMusic/Zote_04.wav"),
    ;

    private final String path;

    SfxType(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
