package com.Ap.HollowKnight.view.sounds;

public enum SfxType implements SoundType {
    ;

    private final String path;
    SfxType(String path){
        this.path = path;
    }
    @Override
    public String getPath(){
        return path;
    }
}
