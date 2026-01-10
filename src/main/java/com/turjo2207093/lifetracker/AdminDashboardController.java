package com.turjo2207093.lifetracker;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class AdminDashboardController {

    @FXML
    private ListView<String> userListView;

    @FXML
    private Label selectedUserLabel;

    @FXML
    private Button resetProgressButton;

    @FXML
    private Button deleteUserButton;

    private ObservableList<String> users = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        loadUsers();
        userListView.setItems(users);

        userListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectedUserLabel.setText("Selected: " + newValue);
                resetProgressButton.setDisable(false);
                deleteUserButton.setDisable(false);
            } else {
                selectedUserLabel.setText("No user selected");
                resetProgressButton.setDisable(true);
                deleteUserButton.setDisable(true);
            }
        });
    }

    private void loadUsers() {
        users.clear();
        String sql = "SELECT username FROM users WHERE is_admin = 0";
        try (Connection conn = DatabaseHandler.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                users.add(rs.getString("username"));
            }
        } catch (SQLException e) {
            System.err.println("Error loading users: " + e.getMessage());
        }
        
        // Add dummy data if DB is empty for demonstration
        if (users.isEmpty()) {
            users.addAll("john_doe", "jane_smith", "habit_master");
        }
    }

    @FXML
    protected void onResetProgressClick() {
        String selectedUser = userListView.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            String sql = "UPDATE users SET level = 1, xp = 0 WHERE username = ?";
            try (Connection conn = DatabaseHandler.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                
                pstmt.setString(1, selectedUser);
                pstmt.executeUpdate();
                System.out.println("Reset progress for user: " + selectedUser);
                
            } catch (SQLException e) {
                System.err.println("Error resetting progress: " + e.getMessage());
            }
        }
    }

    @FXML
    protected void onDeleteUserClick() {
        String selectedUser = userListView.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            String sql = "DELETE FROM users WHERE username = ?";
            try (Connection conn = DatabaseHandler.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                
                pstmt.setString(1, selectedUser);
                pstmt.executeUpdate();
                System.out.println("Deleted user: " + selectedUser);
                loadUsers(); // Refresh list
                
            } catch (SQLException e) {
                System.err.println("Error deleting user: " + e.getMessage());
            }
        }
    }

    @FXML
    protected void onLogoutClick(ActionEvent event) throws IOException {
        switchScene(event, "hello-view.fxml");
    }

    private void switchScene(ActionEvent event, String fxmlFile) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(fxmlFile));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}
