package com.Ap.HollowKnight.view;

import com.Ap.HollowKnight.view.sounds.MusicType;
import com.Ap.HollowKnight.view.sounds.SfxType;
import com.Ap.HollowKnight.view.sounds.SoundType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

import java.util.HashMap;

public class AudioManager{
    private static AudioManager instance;

    private final HashMap<SfxType, Sound> sfxMap = new HashMap<>();
    private final HashMap<MusicType, Music> musicMap = new HashMap<>();

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

    public void playSfx(SfxType type) {
        sfxMap.get(type).play(sfxVolume);
    }

    public void playMusic(MusicType type) {
        if (currentTrackType == type) return; // already playing
        if (currentTrack != null) currentTrack.stop();
        currentTrack = musicMap.get(type);
        currentTrackType = type;
        currentTrack.setVolume(musicVolume);
        currentTrack.play();
    }

    public void stopMusic() {
        if (currentTrack != null) currentTrack.stop();
        currentTrack = null;
        currentTrackType = null;
    }

//    public void playSfxZote() {
//        Sound[] zoteSounds =  new Sound[3];
//        zoteSounds[0]=
//        SfxType chosen = options[(int) (Math.random() * options.length)];
//        playSfx(chosen);
//    }
}
