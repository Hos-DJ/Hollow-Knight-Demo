package com.Ap.HollowKnight.controller;

public class SettingsController {
    private static SettingsController instance;
    private String previousScreen = "MainMenuScreen";
    private boolean returnToPauseMenu = false;
    AudioManager manager = AudioManager.getInstance();

    private int musicLevel = 6;
    private int sfxLevel = 10;
    private int brightnessLevel = 8;
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
        this.musicLevel = amount;
        float changeAmount= amount/10f;
        if(changeAmount <0)return;
        manager.changeMusicVolume(changeAmount);
    }

    public void changeSfxVolume(int amount){
        this.sfxLevel = amount;
        float changeAmount= amount/10f;
        if(changeAmount <0)return;
        manager.changeSfxVolume(changeAmount);
    }
    public float getBrightness(){
        return brightness;
    }

    public void changeBrightness(int amount){
        this.brightnessLevel = amount;
        float changeAmount = 1-amount/10f;
        brightness = changeAmount;
    }

    public void resetSounds(){
        manager.changeMusicVolume(DEFAULT_SOUND_VOLUME);
        manager.changeSfxVolume(DEFAULT_SOUND_VOLUME);
    }

    public String getPreviousScreen() {
        return previousScreen;
    }

    public void setPreviousScreen(String previousScreen) {
        this.previousScreen = previousScreen;
    }

    public int getMusicLevel() {
        return musicLevel;
    }

    public int getSfxLevel() {
        return sfxLevel;
    }

    public int getBrightnessLevel() {
        return brightnessLevel;
    }

    public boolean isReturnToPauseMenu() {
        return returnToPauseMenu;
    }

    public void setReturnToPauseMenu(boolean returnToPauseMenu) {
        this.returnToPauseMenu = returnToPauseMenu;
    }
}
