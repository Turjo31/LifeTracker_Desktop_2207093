package com.turjo2207093.lifetracker;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserProfileController {

    @FXML
    private TextField emailField;

    @FXML
    private TextField nameField;

    @FXML
    private TextField ageField;

    @FXML
    private ComboBox<String> genderComboBox;

    @FXML
    public void initialize() {
        genderComboBox.getItems().addAll("Male", "Female", "Other");
        loadUserData();
    }

    private void loadUserData() {
        UserSession session = UserSession.getInstance();
        if (session != null) {
            emailField.setText(session.getEmail());
            nameField.setText(session.getFullName() != null ? session.getFullName() : "");
            ageField.setText(session.getAge() != null ? session.getAge() : "");
            genderComboBox.setValue(session.getGender());
        }
    }

    @FXML
    protected void onSaveProfileClick() {
        UserSession session = UserSession.getInstance();
        String fullName = nameField.getText();
        String age = ageField.getText();
        String gender = genderComboBox.getValue();

        String sql = "UPDATE users SET full_name = ?, age = ?, gender = ? WHERE id = ?";

        try (Connection conn = DatabaseHandler.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, fullName);
            pstmt.setString(2, age);
            pstmt.setString(3, gender);
            pstmt.setInt(4, session.getId());
            pstmt.executeUpdate();

            session.setFullName(fullName);
            session.setAge(age);
            session.setGender(gender);

            showAlert(Alert.AlertType.INFORMATION, "Success", "Profile updated successfully!");

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error updating profile: " + e.getMessage());
        }
    }

    @FXML
    protected void onOpenDiaryClick(ActionEvent event) throws IOException {
        switchScene(event, "diary-view.fxml");
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

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
