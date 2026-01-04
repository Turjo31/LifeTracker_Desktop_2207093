package com.turjo2207093.lifetracker;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;

import java.io.IOException;

public class SignupController {

    @FXML
    private TextField usernameField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    protected void onSignupClick() {
        String username = usernameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        System.out.println("Attempting signup for: " + username);
    }

    @FXML
    protected void onLoginLinkClick(ActionEvent event) throws IOException {
        switchScene(event, "login-view.fxml");
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
}
