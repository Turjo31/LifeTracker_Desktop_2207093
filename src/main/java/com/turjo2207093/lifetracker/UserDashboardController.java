package com.turjo2207093.lifetracker;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class UserDashboardController {

    @FXML
    private VBox habitsContainer;

    @FXML
    private Label levelLabel;

    @FXML
    private Label xpLabel;

    @FXML
    private ProgressBar xpProgressBar;

    private int currentLevel = 1;
    private int currentXp = 0;
    private int xpForNextLevel = 100;

    @FXML
    public void initialize() {
        updateLevelDisplay();
        habitsContainer.getChildren().clear();
    }

    @FXML
    protected void onLogoutClick(ActionEvent event) throws IOException {
        switchScene(event, "hello-view.fxml");
    }

    @FXML
    protected void onProfileClick(ActionEvent event) throws IOException {
        switchScene(event, "user-profile-view.fxml");
    }

    @FXML
    protected void onAddHabitClick() {
        Dialog<Habit> dialog = new Dialog<>();
        dialog.setTitle("Add New Habit");
        dialog.setHeaderText("Create a new habit to track");

        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        VBox content = new VBox(10);
        content.setPadding(new Insets(20, 150, 10, 10));

        TextField nameField = new TextField();
        nameField.setPromptText("Habit Name (e.g., Drink Water)");
        TextField targetField = new TextField();
        targetField.setPromptText("Target (e.g., 8 glasses)");

        content.getChildren().addAll(new Label("Name:"), nameField, new Label("Target:"), targetField);
        dialog.getDialogPane().setContent(content);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                return new Habit(nameField.getText(), targetField.getText());
            }
            return null;
        });

        Optional<Habit> result = dialog.showAndWait();

        result.ifPresent(this::addHabitToView);
    }

    private void addHabitToView(Habit habit) {
        HBox habitCard = new HBox(15);
        habitCard.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        habitCard.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 5, 0, 0, 0);");
        habitCard.setPadding(new Insets(15));

        VBox textContainer = new VBox();
        HBox.setHgrow(textContainer, javafx.scene.layout.Priority.ALWAYS);
        
        Label nameLabel = new Label(habit.getName());
        nameLabel.setTextFill(javafx.scene.paint.Color.web("#333333"));
        nameLabel.setFont(Font.font("System", FontWeight.BOLD, 16));

        Label targetLabel = new Label("Target: " + habit.getTarget());
        targetLabel.setTextFill(javafx.scene.paint.Color.web("#666666"));

        textContainer.getChildren().addAll(nameLabel, targetLabel);

        Button checkInButton = new Button("Check In");
        checkInButton.setStyle("-fx-background-color: #E8F5E9; -fx-text-fill: #2E7D32;");
        checkInButton.setOnAction(e -> {
            gainXp(10);
            habitsContainer.getChildren().remove(habitCard);
        });

        Button deleteButton = new Button("Delete");
        deleteButton.setStyle("-fx-background-color: #FFEBEE; -fx-text-fill: #D32F2F;");
        deleteButton.setOnAction(e -> {
            habitsContainer.getChildren().remove(habitCard);
        });

        habitCard.getChildren().addAll(textContainer, checkInButton, deleteButton);
        habitsContainer.getChildren().add(habitCard);
    }

    private void gainXp(int amount) {
        currentXp += amount;
        if (currentXp >= xpForNextLevel) {
            levelUp();
        }
        updateLevelDisplay();
    }

    private void levelUp() {
        currentXp -= xpForNextLevel;
        currentLevel++;
        xpForNextLevel += 50;
    }

    private void updateLevelDisplay() {
        levelLabel.setText("Level " + currentLevel);
        xpLabel.setText("XP: " + currentXp + " / " + xpForNextLevel);
        xpProgressBar.setProgress((double) currentXp / xpForNextLevel);
    }

    private void switchScene(ActionEvent event, String fxmlFile) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(fxmlFile));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}
