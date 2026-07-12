package com.Ap.HollowKnight.controller;

import com.Ap.HollowKnight.controller.events.StatisticsListener;
import com.Ap.HollowKnight.model.data.DatabaseManager;
import com.Ap.HollowKnight.model.boss.FalseKnight;
import com.Ap.HollowKnight.model.enemy.EnemyModel;
import com.Ap.HollowKnight.model.level.LevelModel;
import com.Ap.HollowKnight.model.Knight.Knight;
import com.badlogic.gdx.math.Vector2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class SaveManager {
    private static SaveManager instance;
    private int slotInPending = -1;
    private SaveManager() {}

    public static SaveManager getInstance() {
        if (instance == null) instance = new SaveManager();
        return instance;
    }

    public boolean hasSave(int slot) {
        String query = "SELECT 1 FROM saves WHERE slot = ?";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, slot);
            return pstmt.executeQuery().next();
        } catch (SQLException e) {
            return false;
        }
    }

    public void saveGame(int slot, boolean isinBossArena, String achievements) {
        LevelModel levelModel = LevelModel.getInstance();
        Knight knight = levelModel.getKnight();

        StatisticsListener stats = StatisticsListener.getInstance();
        int killsToSave = stats.getEnemiesKilled();
        int deathsToSave = stats.getDeathCount();
        int timeToSave = stats.getSecondsPassed();

        boolean bossDead = true;
        for (EnemyModel enemy : levelModel.getEnemies()) {
            if (enemy instanceof FalseKnight) {
                bossDead = false;
                break;
            }
        }

        String query = "INSERT OR REPLACE INTO saves " +
            "(slot, player_x, player_y, masks, soul, boss_dead, achievements, enemies_killed, deaths, playtime) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, slot);
            pstmt.setFloat(2, knight.getPosition().x);
            pstmt.setFloat(3, knight.getPosition().y);
            pstmt.setInt(4, knight.getCurrentMasks());
            pstmt.setInt(5, knight.getCurrentSoul());
            pstmt.setBoolean(6, bossDead);
            pstmt.setString(7, achievements != null ? achievements : "");

            pstmt.setInt(8, killsToSave);
            pstmt.setInt(9, deathsToSave);
            pstmt.setInt(10, timeToSave);

            pstmt.executeUpdate();
            System.out.println("Game Saved Successfully to Slot " + slot + " with Statistics!");

        } catch (SQLException e) {
            System.err.println("Error saving game: " + e.getMessage());
        }
    }

    public void loadGame(int slot) {
        LevelModel levelModel = LevelModel.getInstance();
        Knight knight = levelModel.getKnight();
        String query = "SELECT * FROM saves WHERE slot = ?";

        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, slot);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                knight.getPosition().set(rs.getFloat("player_x"), rs.getFloat("player_y"));
                knight.setCurrentMasks(rs.getInt("masks"));
                knight.setCurrentSoul(rs.getInt("soul"));

                if (rs.getBoolean("boss_dead")) {
                    levelModel.getEnemies().removeIf(enemy -> enemy instanceof FalseKnight);
                }

                String achString = rs.getString("achievements");
                if (achString != null && !achString.isEmpty()) {
                    List<String> loadedAchievements = Arrays.asList(achString.split(","));
                }
                int kills = rs.getInt("enemies_killed");
                int deaths = rs.getInt("deaths");
                int time = rs.getInt("playtime");
                StatisticsListener.getInstance().loadStats(kills, deaths, time);
                System.out.println("Game Loaded from SQLite! (Slot: " + slot + ")");
            } else {
                Vector2 spawn = levelModel.getSpawnPoint();
                knight.getPosition().set(spawn.x, spawn.y);
                knight.setCurrentMasks(knight.MAX_MASKS);
                knight.setCurrentSoul(0);
                StatisticsListener.getInstance().resetStats();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void clearSaveSlot(int slotNumber) {
        String query = "DELETE FROM saves WHERE slot = ?";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, slotNumber);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Slot " + slotNumber + " cleared from database.");
            } else {
                System.out.println("Slot " + slotNumber + " was already empty.");
            }

        } catch (SQLException e) {
            System.err.println("Clear Slot Error: " + e.getMessage());
        }
    }

    public int getSlotInPending() {
        return slotInPending;
    }

    public void setSlotInPending(int slotInPending) {
        this.slotInPending = slotInPending;
    }
    public void clearSlotInPending() {
        this.slotInPending = -1;
    }
}
