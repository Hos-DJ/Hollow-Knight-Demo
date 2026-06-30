package com.Ap.HollowKnight.view;

public enum AchievementsAssets {
    DEFEAT_BOSS("Ui/Achievements/Defeat Boss.png"),
    GAME_COMPLETION("Ui/Achievements/Game Completion.png"),
    SECRET_DISCOVERED("Ui/Achievements/Secret Discovered.png"),
    SPEEDRUN("Ui/Achievements/Speedrun.png"),
    TRUE_HUNTER("Ui/Achievements/True Hunter.png");

    private final String path;
    AchievementsAssets(String path){
        this.path = path;
    }
    public String getPath(){
        return path;
    }
}
