package com.turjo2207093.lifetracker;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

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
    }

    @FXML
    protected void onSaveProfileClick() {
        System.out.println("Profile saved: " + nameField.getText());
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
}
