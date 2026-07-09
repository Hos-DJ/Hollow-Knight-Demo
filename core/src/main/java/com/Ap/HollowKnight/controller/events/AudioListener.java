package com.Ap.HollowKnight.controller.events;

import com.Ap.HollowKnight.view.AudioManager;
import com.Ap.HollowKnight.view.sounds.MusicType;
import com.Ap.HollowKnight.view.sounds.SfxType;

import java.util.Random;

public class AudioListener implements GameEventListener {
    private final AudioManager audioManager = AudioManager.getInstance();
    private final Random random = new Random();

    private long walkingSoundId = -1;
    private boolean isWalking = false;

    private long wallSlideSoundId = -1;
    private boolean isSliding = false;

    private long focusSoundId = -1;
    private boolean isFocusing = false;

    private long zoteAttackSoundId = -1;
    private boolean isZoteAttacking = false;

    @Override
    public void onEvent(GameEvent event, Object payload) {
        switch (event) {


            case ENTER_CROSSROADS -> audioManager.playMusic(MusicType.CITY_OF_TEARS);
            case ENTER_GREENPATH -> audioManager.playMusic(MusicType.GREENPATH);
            case ENTERED_BOSS_ROOM -> audioManager.playMusic(MusicType.MANTIS_LORDS);
            case BOSS_DEFEATED -> audioManager.playMusic(MusicType.GAME_FINISHED);


            case PLAYER_STARTED_WALKING -> {
                if (!isWalking) {
                    walkingSoundId = audioManager.loopSfx(SfxType.KNIGHT_FOOTSTEP);
                    isWalking = true;
                }
            }
            case PLAYER_ENDED_WALKING -> {
                if (isWalking) {
                    audioManager.stopSfx(SfxType.KNIGHT_FOOTSTEP, walkingSoundId);
                    isWalking = false;
                }
            }
            case PLAYER_STARTED_WALL_SLIDING -> {
                if (!isSliding) {
                    wallSlideSoundId = audioManager.loopSfx(SfxType.WALL_SLIDE);
                    isSliding = true;
                }
            }
            case PLAYER_ENDED_WALL_SLIDING -> {
                if (isSliding) {
                    audioManager.stopSfx(SfxType.WALL_SLIDE, wallSlideSoundId);
                    isSliding = false;
                }
            }
            case PLAYER_FOCUS_START -> {
                if (!isFocusing) {
                    focusSoundId = audioManager.loopSfx(SfxType.FOCUS_CHARGE_BEGIN);
                    isFocusing = true;
                }
            }


            case PLAYER_FOCUS_END -> {
                if (isFocusing) {
                    audioManager.stopSfx(SfxType.FOCUS_CHARGE_BEGIN, focusSoundId);
                    isFocusing = false;
                }
                audioManager.playSfx(SfxType.FOCUS_CHARGE_END);
            }
            case PLAYER_SOUL_GAIN -> {
                    int index = random.nextInt(7)+1;
                    switch (index) {
                        case 1 -> audioManager.playSfx(SfxType.SOUL_GAIN_1);
                        case 2 -> audioManager.playSfx(SfxType.SOUL_GAIN_2);
                        case 3 -> audioManager.playSfx(SfxType.SOUL_GAIN_3);
                        case 4 -> audioManager.playSfx(SfxType.SOUL_GAIN_4);
                        case 5 -> audioManager.playSfx(SfxType.SOUL_GAIN_5);
                        case 6 -> audioManager.playSfx(SfxType.SOUL_GAIN_6);
                        case 7 -> audioManager.playSfx(SfxType.SOUL_GAIN_7);
                    }
            }
            case PLAYER_DASH -> audioManager.playSfx(SfxType.KNIGHT_DASH);
            case PLAYER_MONARCH_WINGS -> audioManager.playSfx(SfxType.KNIGHT_MONARCH);

            case PLAYER_ATTACKING -> audioManager.playSfx(SfxType.NAIL_SLASH);
            case POGO_SPIKE -> audioManager.playSfx(SfxType.NAIL_POGO_BOUNCE);
            case ATTACKING_WALL -> audioManager.playSfx(SfxType.NAIL_HIT_WALL);
            case WALL_DESTROYED -> audioManager.playSfx(SfxType.WALL_BROKEN);

            case PLAYER_VENGEFUL -> audioManager.playSfx(SfxType.CAST_FIREBALL);
            case PLAYER_HOWLING -> audioManager.playSfx(SfxType.CAST_HOWLING);


            case ENEMY_HURT -> audioManager.playSfx(SfxType.ENEMY_DAMAGE);
            case ENEMY_KILLED -> audioManager.playSfx(SfxType.ENEMY_DEATH);
            case PLAYER_HURT -> audioManager.playSfx(SfxType.KNIGHT_HURT);
            case PLAYER_DOUBLE_HURT -> audioManager.playSfx(SfxType.KNIGHT_HEAVY_HURT);
            case PLAYER_DEATH -> {
                audioManager.playSfx(SfxType.KNIGHT_DEATH);
                audioManager.playMusic(MusicType.CITY_OF_TEARS);
            }
            case SLAM_MACE -> {

            }


            case ZOTE_IS_TALKING -> {
                int randomVoice = random.nextInt(4) + 1;
                switch (randomVoice) {
                    case 1 -> audioManager.playSfx(SfxType.ZOTE_MUMBLE_1);
                    case 2 -> audioManager.playSfx(SfxType.ZOTE_MUMBLE_2);
                    case 3 -> audioManager.playSfx(SfxType.ZOTE_MUMBLE_3);
                    case 4 -> audioManager.playSfx(SfxType.ZOTE_MUMBLE_4);
                }
            }
            case ZOTE_STARTED_ATTACKING -> {
                if (!isZoteAttacking) {
                    zoteAttackSoundId = audioManager.loopSfx(SfxType.ZOTE_ATTACK);
                    isZoteAttacking = true;
                }
            }
            case ZOTE_ENDED_ATTACKING -> {
                if (isZoteAttacking) {
                    audioManager.stopSfx(SfxType.ZOTE_ATTACK, zoteAttackSoundId);
                    isZoteAttacking = false;
                }
            }

            case GAME_COMPLETED, SECRET_DISCOVERED -> {}
        }
    }
}
