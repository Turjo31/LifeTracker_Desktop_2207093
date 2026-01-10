package com.turjo2207093.lifetracker;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;

public class DiaryController {

    @FXML
    private TextArea diaryTextArea;

    @FXML
    private Label wordCountLabel;

    @FXML
    public void initialize() {
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

    @FXML
    protected void onSaveDiaryClick() {
        System.out.println("Diary entry saved.");
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
}
