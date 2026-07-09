package com.Ap.HollowKnight.model.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private static DatabaseManager instance;
    private Connection connection;
    private static final String DB_URL = "jdbc:sqlite:Data.db";

    private DatabaseManager() {
        connect();
        createTables();
    }

    public static DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    private void connect() {
        try {
            connection = DriverManager.getConnection(DB_URL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connect();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    private void createTables() {
        String query = """
            CREATE TABLE IF NOT EXISTS saves (
                slot INTEGER PRIMARY KEY,
                player_x REAL,
                player_y REAL,
                masks INTEGER,
                soul INTEGER,
                boss_dead BOOLEAN,
                achievements TEXT,
                enemies_killed INTEGER DEFAULT 0,
                deaths INTEGER DEFAULT 0,
                playtime INTEGER DEFAULT 0
            );
        """;

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(query);
            System.out.println("Database tables checked/created successfully.");
        } catch (SQLException e) {
            System.err.println("Error creating tables: " + e.getMessage());
        }
    }
}
