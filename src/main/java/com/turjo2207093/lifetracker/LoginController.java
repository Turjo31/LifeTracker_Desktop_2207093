package com.turjo2207093.lifetracker;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    protected void onLoginClick(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";

        try (Connection conn = DatabaseHandler.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                UserSession.setSession(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("email"),
                    rs.getInt("level"),
                    rs.getInt("xp"),
                    rs.getInt("is_admin") == 1,
                    rs.getString("full_name"),
                    rs.getString("age"),
                    rs.getString("gender"),
                    rs.getString("diary_entry")
                );

                if (rs.getInt("is_admin") == 1) {
                    switchScene(event, "admin-dashboard-view.fxml");
                } else {
                    switchScene(event, "user-dashboard-view.fxml");
                }
            } else {
                showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid username or password");
            }

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error logging in: " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onSignupLinkClick(ActionEvent event) throws IOException {
        switchScene(event, "signup-view.fxml");
    }

    @FXML
    protected void onBackClick(ActionEvent event) throws IOException {
        switchScene(event, "hello-view.fxml");
    }

    private void switchScene(ActionEvent event, String fxmlFile) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(fxmlFile));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
