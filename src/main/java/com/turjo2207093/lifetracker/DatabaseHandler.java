package com.turjo2207093.lifetracker;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseHandler {

    private static final String DB_URL = "jdbc:sqlite:lifetracker.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    public static void initializeDatabase() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            String createUsersTable = "CREATE TABLE IF NOT EXISTS users (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "username TEXT UNIQUE NOT NULL," +
                    "password TEXT NOT NULL," +
                    "email TEXT," +
                    "level INTEGER DEFAULT 1," +
                    "xp INTEGER DEFAULT 0," +
                    "is_admin INTEGER DEFAULT 0" +
                    ")";
            stmt.execute(createUsersTable);

            String createHabitsTable = "CREATE TABLE IF NOT EXISTS habits (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "user_id INTEGER," +
                    "name TEXT NOT NULL," +
                    "target TEXT," +
                    "progress INTEGER DEFAULT 0," +
                    "FOREIGN KEY(user_id) REFERENCES users(id)" +
                    ")";
            stmt.execute(createHabitsTable);

            System.out.println("Database initialized.");

        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
        }
    }
}
