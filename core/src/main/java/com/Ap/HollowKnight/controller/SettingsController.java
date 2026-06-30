package com.Ap.HollowKnight.controller;

import com.Ap.HollowKnight.view.AudioManager;

public class SettingsController {
    private static SettingsController instance;
    AudioManager manager = AudioManager.getInstance();
    private SettingsController() {

    }

    public static SettingsController getInstance() {
        if(instance == null) {
            instance = new SettingsController();
        }
        return instance;
    }
    private float brightness =0.2f;
    private static final float DEFAULT_SOUND_VOLUME = 0.8f;
    public void changeMusicVolume(int amount){
        float changeAmount= amount/10f;
        if(changeAmount <0)return;
        manager.changeMusicVolume(changeAmount);
    }

    public void changeSfxVolume(int amount){
        float changeAmount= amount/10f;
        if(changeAmount <=0)return;
        manager.changeSfxVolume(changeAmount);
    }
    public float getBrightness(){
        return brightness;
    }

    public void changeBrightness(int amount){
        float changeAmount = 1-amount/10f;
        brightness = changeAmount;
    }

    public void resetSounds(){
        manager.changeMusicVolume(DEFAULT_SOUND_VOLUME);
        manager.changeSfxVolume(DEFAULT_SOUND_VOLUME);
    }

}
