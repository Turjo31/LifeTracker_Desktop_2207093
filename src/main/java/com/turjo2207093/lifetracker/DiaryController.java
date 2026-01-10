package com.turjo2207093.lifetracker;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DiaryController {

    @FXML
    private TextArea diaryTextArea;

    @FXML
    private Label wordCountLabel;

    @FXML
    public void initialize() {
        loadDiaryEntry();
        
        diaryTextArea.textProperty().addListener((observable, oldValue, newValue) -> {
            String[] words = newValue.trim().split("\\s+");
            int count = newValue.trim().isEmpty() ? 0 : words.length;
            
            if (count > 200) {
                diaryTextArea.setText(oldValue);
            } else {
                wordCountLabel.setText(count + " / 200 words");
            }
        });
    }

    private void loadDiaryEntry() {
        UserSession session = UserSession.getInstance();
        if (session != null && session.getDiaryEntry() != null) {
            diaryTextArea.setText(session.getDiaryEntry());
        }
    }

    @FXML
    protected void onSaveDiaryClick() {
        UserSession session = UserSession.getInstance();
        String entry = diaryTextArea.getText();

        String sql = "UPDATE users SET diary_entry = ? WHERE id = ?";

        try (Connection conn = DatabaseHandler.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, entry);
            pstmt.setInt(2, session.getId());
            pstmt.executeUpdate();

            session.setDiaryEntry(entry);

            showAlert(Alert.AlertType.INFORMATION, "Success", "Diary entry saved!");

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error saving diary: " + e.getMessage());
        }
    }

    @FXML
    protected void onBackClick(ActionEvent event) throws IOException {
        switchScene(event, "user-profile-view.fxml");
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
