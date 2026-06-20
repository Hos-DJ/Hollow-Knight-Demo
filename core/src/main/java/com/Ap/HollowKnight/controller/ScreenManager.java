package com.Ap.HollowKnight.controller;

import com.Ap.HollowKnight.HollowKnight;
import com.Ap.HollowKnight.model.level.LevelModel;
import com.Ap.HollowKnight.model.map.TiledMapHelper;
import com.Ap.HollowKnight.view.screen.AchievementsScreen;
import com.Ap.HollowKnight.view.screen.GameScreen;
import com.Ap.HollowKnight.view.screen.InventoryScreen;
import com.Ap.HollowKnight.view.screen.MainMenuScreen;
import com.Ap.HollowKnight.view.screen.PauseScreen;
import com.Ap.HollowKnight.view.screen.SettingsScreen;
import com.Ap.HollowKnight.view.screen.VictoryScreen;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

import java.util.HashMap;

public class ScreenManager {
    private static ScreenManager instance;
    private HollowKnight game;
    private HashMap<String,Screen> screens;
    private ScreenManager(){
        screens = new HashMap<String, Screen>();
    }
    public static ScreenManager getInstance(){
        if(instance==null){
            instance = new ScreenManager();
        }
        return instance;
    }
    public void initialize(HollowKnight game) {
        this.game = game;
    }
    public void addScreen(String name){
        switch(name){
            case "MainMenuScreen" -> screens.put(name, new MainMenuScreen(game));
            case "SettingsScreen" -> screens.put(name, new SettingsScreen(game));
                case "AchievementsScreen" -> screens.put(name, new AchievementsScreen(game));
            case "InventoryScreen" -> screens.put(name, new InventoryScreen(game));
            case "PauseScreen" -> screens.put(name, new PauseScreen(game));
            case "GameScreen" -> {
                TiledMapHelper mapHelper = new TiledMapHelper();
                TiledMap map =mapHelper.loadMap("assets/Forgotton main.tmx");
                TiledMapTileLayer mainLayer = (TiledMapTileLayer) map.getLayers().get(0);
                float mapWidth = mainLayer.getWidth() * mainLayer.getTileWidth();
                float mapHeight = mainLayer.getHeight() * mainLayer.getTileHeight();
                LevelModel levelModel = new LevelModel(mapHelper.getRectangles(), mapWidth, mapHeight);
                screens.put(name, new GameScreen(game, levelModel, map));
            }
            case "VictoryScreen" -> screens.put(name, new VictoryScreen(game));
        }
    }
    public void setScreen (String name){
        if(!screens.containsKey(name)){
            getInstance().addScreen(name);
        }
        game.setScreen(screens.get(name));
    }
    public void removeScreen(String name){
        screens.remove(name);
    }


}
