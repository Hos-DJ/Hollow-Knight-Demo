package com.Ap.HollowKnight.view.sounds;

public enum MusicType implements SoundType {
    MAIN_THEME("Ui/Audio/backgroundMusic/Main Theme.mp3"),
    CITY_OF_TEARS("Ui/Audio/backgroundMusic/City of Tears.mp3"),
    MANTIS_LORDS("Ui/Audio/backgroundMusic/Manitis Lords.mp3"),
    GREENPATH("Ui/Audio/backgroundMusic/Greenpath.mp3"),
    GAME_FINISHED("Ui/Audio/backgroundMusic/final theme.mp3")
    ;

    private final String path;
    MusicType(String path){
        this.path = path;
    }
    @Override
    public String getPath(){
        return path;
    }
}
