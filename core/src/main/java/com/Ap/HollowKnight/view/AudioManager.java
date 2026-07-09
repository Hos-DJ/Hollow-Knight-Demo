package com.Ap.HollowKnight.view;

import com.Ap.HollowKnight.view.sounds.MusicType;
import com.Ap.HollowKnight.view.sounds.SfxType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

import java.util.HashMap;

public class AudioManager {
    private enum FadeState { NONE, FADING_OUT, SILENT, FADING_IN }
    private FadeState fadeState = FadeState.NONE;
    private static AudioManager instance;

    private final HashMap<SfxType, Sound> sfxMap = new HashMap<>();
    private final HashMap<MusicType, Music> musicMap = new HashMap<>();
    private Music nextMusic;
    private boolean isFading = false;
    private float fadeTimer = 0f;
    private final float FADE_OUT_DURATION = 1.5f;
    private final float SILENCE_DURATION = 2.0f;
    private final float FADE_IN_DURATION = 1.5f;
    private Music currentTrack;
    private MusicType currentTrackType;
    private float sfxVolume = 1f;
    private float musicVolume = 0.6f;

    private AudioManager() {
        loadSfx();
        loadMusic();
    }

    public static AudioManager getInstance() {
        if (instance == null) instance = new AudioManager();
        return instance;
    }

    private void loadSfx() {
        for (SfxType type : SfxType.values()) {
            sfxMap.put(type, Gdx.audio.newSound(Gdx.files.internal(type.getPath())));
        }
    }

    private void loadMusic() {
        for (MusicType type : MusicType.values()) {
            Music music = Gdx.audio.newMusic(Gdx.files.internal(type.getPath()));
            music.setLooping(true);
            musicMap.put(type, music);
        }
    }

    public long loopSfx(SfxType type) {
        Sound sound = getSound(type);
        if (sound != null) {
            return sound.loop(sfxVolume);
        }
        return -1;
    }

    public void playSfx(SfxType type) {
        sfxMap.get(type).play(sfxVolume);
    }

    public void playMusic(MusicType type) {
        Music requestedMusic = getMusic(type);


        if (requestedMusic == null || requestedMusic == currentTrack || requestedMusic == nextMusic) {
            return;
        }

        nextMusic = requestedMusic;

        if (currentTrack != null && currentTrack.isPlaying()) {
            fadeState = FadeState.FADING_OUT;
            fadeTimer = 0f;
        } else {
            currentTrack = nextMusic;
            currentTrackType = type;
            currentTrack.setVolume(musicVolume);
            currentTrack.setLooping(true);
            currentTrack.play();

            fadeState = FadeState.NONE;
        }
    }
    public void update(float delta) {
        switch (fadeState) {

            case FADING_OUT:
                fadeTimer += delta;
                float outProgress = Math.min(fadeTimer / FADE_OUT_DURATION, 1.0f);

                if (currentTrack != null) {
                    currentTrack.setVolume(musicVolume * (1f - outProgress));
                }

                if (outProgress >= 1.0f) {
                    if (currentTrack != null) currentTrack.stop();
                    fadeState = FadeState.SILENT;
                    fadeTimer = 0f;
                }
                break;

            case SILENT:
                fadeTimer += delta;

                if (fadeTimer >= SILENCE_DURATION) {
                    currentTrack = nextMusic;
                    nextMusic = null;

                    if (currentTrack != null) {
                        currentTrack.setVolume(0f);
                        currentTrack.setLooping(true);
                        currentTrack.play();
                    }

                    fadeState = FadeState.FADING_IN;
                    fadeTimer = 0f;
                }
                break;

            case FADING_IN:
                fadeTimer += delta;
                float inProgress = Math.min(fadeTimer / FADE_IN_DURATION, 1.0f);

                if (currentTrack != null) {
                    currentTrack.setVolume(musicVolume * inProgress);
                }

                if (inProgress >= 1.0f) {
                    fadeState = FadeState.NONE;
                    fadeTimer = 0f;
                }
                break;

            case NONE:
            default:
                break;
        }
    }


    public void stopMusic() {
        if (currentTrack != null) currentTrack.stop();
        currentTrack = null;
        currentTrackType = null;

        nextMusic = null;
        fadeState = FadeState.NONE;
    }

    public void stopSfx(SfxType type, long soundId) {
        if (soundId == -1) return;
        Sound sound = getSound(type);
        if (sound != null) {
            sound.stop(soundId);
        }
    }
    public void changeSfxVolume(float volume) {
        sfxVolume = volume;
    }

    public void changeMusicVolume(float volume) {
        musicVolume = volume;
        if (currentTrack != null) {
            currentTrack.setVolume(musicVolume);
        }
    }

    private Sound getSound(SfxType type) {
        if (type.getPath() == null || type.getPath().isEmpty()) return null;

        if (!sfxMap.containsKey(type)) {
            try {
                Sound sound = Gdx.audio.newSound(Gdx.files.internal(type.getPath()));
                sfxMap.put(type, sound);
            } catch (Exception e) {
                Gdx.app.error("Audio", "Failed to load SFX: " + type.getPath());
                return null;
            }
        }
        return sfxMap.get(type);
    }

    private Music getMusic(MusicType type) {
        if (type.getPath() == null || type.getPath().isEmpty()) return null;

        if (!musicMap.containsKey(type)) {
            try {
                Music music = Gdx.audio.newMusic(Gdx.files.internal(type.getPath()));
                musicMap.put(type, music);
            } catch (Exception e) {
                Gdx.app.error("Audio", "Failed to load Music: " + type.getPath());
                return null;
            }
        }
        return musicMap.get(type);
    }


}
