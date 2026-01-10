package com.turjo2207093.lifetracker;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LeaderboardController {

    @FXML
    private TableView<LeaderboardEntry> leaderboardTable;

    @FXML
    private TableColumn<LeaderboardEntry, Integer> rankColumn;

    @FXML
    private TableColumn<LeaderboardEntry, String> usernameColumn;

    @FXML
    private TableColumn<LeaderboardEntry, Integer> levelColumn;

    @FXML
    private TableColumn<LeaderboardEntry, Integer> xpColumn;

    @FXML
    public void initialize() {
        rankColumn.setCellValueFactory(new PropertyValueFactory<>("rank"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        levelColumn.setCellValueFactory(new PropertyValueFactory<>("level"));
        xpColumn.setCellValueFactory(new PropertyValueFactory<>("xp"));

        loadLeaderboardData();
    }

    private void loadLeaderboardData() {
        ObservableList<LeaderboardEntry> data = FXCollections.observableArrayList();
        String sql = "SELECT username, level, xp FROM users WHERE is_admin = 0 ORDER BY level DESC, xp DESC";

        try (Connection conn = DatabaseHandler.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            int rank = 1;
            while (rs.next()) {
                data.add(new LeaderboardEntry(
                    rank++,
                    rs.getString("username"),
                    rs.getInt("level"),
                    rs.getInt("xp")
                ));
            }
            leaderboardTable.setItems(data);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onBackClick(ActionEvent event) throws IOException {
        switchScene(event, "user-dashboard-view.fxml");
    }

    private void switchScene(ActionEvent event, String fxmlFile) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(fxmlFile));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}
