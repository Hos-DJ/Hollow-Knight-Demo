package com.Ap.HollowKnight.controller;

import com.Ap.HollowKnight.HollowKnight;
import com.Ap.HollowKnight.model.level.LevelModel;
import com.Ap.HollowKnight.model.map.TiledMapHelper;
import com.Ap.HollowKnight.view.screen.*;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

import java.util.HashMap;

public class ScreenManager {
    private static ScreenManager instance;
    private HollowKnight game;
    private HashMap<String, Screen> screens;

    private ScreenManager() {
        screens = new HashMap<String, Screen>();
    }

    public static ScreenManager getInstance() {
        if (instance == null) {
            instance = new ScreenManager();
        }
        return instance;
    }

    public void initialize(HollowKnight game) {
        this.game = game;
    }

    public void addScreen(String name) {
        switch (name) {
            case "MainMenuScreen" -> screens.put(name, new MainMenuScreen(game));
            case "SettingsScreen" -> screens.put(name, new SettingsScreen(game));
            case "AchievementsScreen" -> screens.put(name, new AchievementsScreen(game));
            case "GameScreen" -> makeGameScreen(name);
        }
    }

    private void makeGameScreen(String name) {
        TiledMapHelper mapHelper = new TiledMapHelper();
        TiledMap map = mapHelper.loadMap("assets/Forgotton main.tmx");
        TiledMapTileLayer mainLayer = (TiledMapTileLayer) map.getLayers().get(0);
        float mapWidth = mainLayer.getWidth() * mainLayer.getTileWidth();
        float mapHeight = mainLayer.getHeight() * mainLayer.getTileHeight();
        LevelModel.getInstance(mapHelper.getRectangles(), mapHelper.getEnemies(), mapWidth,
            mapHeight, mapHelper.getSafeSpots(), mapHelper.spawnPoint(),
            mapHelper.getZote(),mapHelper.getDestructibleWall(),mapHelper.getTheGate());
        screens.put(name, new GameScreen(game, map));
    }

    public void setScreen(String name) {
        if (!screens.containsKey(name)) {
            getInstance().addScreen(name);
        }
        game.setScreen(screens.get(name));
    }

    public void removeScreen(String name) {
        screens.remove(name);
    }

    public HollowKnight getGame() {
        return game;
    }
}
