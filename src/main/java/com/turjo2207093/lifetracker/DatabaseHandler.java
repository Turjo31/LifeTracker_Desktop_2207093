package com.turjo2207093.lifetracker;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

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
                    "is_admin INTEGER DEFAULT 0," +
                    "full_name TEXT," +
                    "age TEXT," +
                    "gender TEXT," +
                    "diary_entry TEXT" +
                    ")";
            stmt.execute(createUsersTable);

            addColumnIfNotExists(stmt, "users", "full_name", "TEXT");
            addColumnIfNotExists(stmt, "users", "age", "TEXT");
            addColumnIfNotExists(stmt, "users", "gender", "TEXT");
            addColumnIfNotExists(stmt, "users", "diary_entry", "TEXT");

            String createHabitsTable = "CREATE TABLE IF NOT EXISTS habits (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "user_id INTEGER," +
                    "name TEXT NOT NULL," +
                    "target TEXT," +
                    "progress INTEGER DEFAULT 0," +
                    "FOREIGN KEY(user_id) REFERENCES users(id)" +
                    ")";
            stmt.execute(createHabitsTable);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void addColumnIfNotExists(Statement stmt, String tableName, String columnName, String columnType) {
        try {
            ResultSet rs = stmt.executeQuery("PRAGMA table_info(" + tableName + ")");
            boolean columnExists = false;
            while (rs.next()) {
                if (rs.getString("name").equals(columnName)) {
                    columnExists = true;
                    break;
                }
            }
            if (!columnExists) {
                stmt.execute("ALTER TABLE " + tableName + " ADD COLUMN " + columnName + " " + columnType);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
