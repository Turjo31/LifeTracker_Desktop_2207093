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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    private int xpForNextLevel = 100;

    @FXML
    public void initialize() {
        updateLevelDisplay();
        loadHabits();
    }

    private void loadHabits() {
        habitsContainer.getChildren().clear();
        UserSession session = UserSession.getInstance();
        if (session == null) return;

        String sql = "SELECT * FROM habits WHERE user_id = ?";
        try (Connection conn = DatabaseHandler.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, session.getId());
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Habit habit = new Habit(rs.getString("name"), rs.getString("target"));
                addHabitToView(habit, rs.getInt("id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onLogoutClick(ActionEvent event) throws IOException {
        UserSession.cleanSession();
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

        result.ifPresent(habit -> {
            saveHabitToDb(habit);
        });
    }

    private void saveHabitToDb(Habit habit) {
        UserSession session = UserSession.getInstance();
        String sql = "INSERT INTO habits(user_id, name, target) VALUES(?, ?, ?)";
        
        try (Connection conn = DatabaseHandler.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, session.getId());
            pstmt.setString(2, habit.getName());
            pstmt.setString(3, habit.getTarget());
            pstmt.executeUpdate();
            
            loadHabits(); 
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addHabitToView(Habit habit, int habitId) {
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
            deleteHabitFromDb(habitId);
            habitsContainer.getChildren().remove(habitCard);
        });

        Button deleteButton = new Button("Delete");
        deleteButton.setStyle("-fx-background-color: #FFEBEE; -fx-text-fill: #D32F2F;");
        deleteButton.setOnAction(e -> {
            deleteHabitFromDb(habitId);
            habitsContainer.getChildren().remove(habitCard);
        });

        habitCard.getChildren().addAll(textContainer, checkInButton, deleteButton);
        habitsContainer.getChildren().add(habitCard);
    }

    private void deleteHabitFromDb(int habitId) {
        String sql = "DELETE FROM habits WHERE id = ?";
        try (Connection conn = DatabaseHandler.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, habitId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void gainXp(int amount) {
        UserSession session = UserSession.getInstance();
        int currentXp = session.getXp() + amount;
        int currentLevel = session.getLevel();
        
        int requiredXp = 100 + (currentLevel - 1) * 50;
        
        if (currentXp >= requiredXp) {
            currentXp -= requiredXp;
            currentLevel++;
        }
        
        session.setXp(currentXp);
        session.setLevel(currentLevel);
        
        updateUserProgressInDb(currentLevel, currentXp);
        updateLevelDisplay();
    }

    private void updateUserProgressInDb(int level, int xp) {
        UserSession session = UserSession.getInstance();
        String sql = "UPDATE users SET level = ?, xp = ? WHERE id = ?";
        try (Connection conn = DatabaseHandler.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, level);
            pstmt.setInt(2, xp);
            pstmt.setInt(3, session.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateLevelDisplay() {
        UserSession session = UserSession.getInstance();
        if (session == null) return;
        
        int currentLevel = session.getLevel();
        int currentXp = session.getXp();
        int requiredXp = 100 + (currentLevel - 1) * 50;
        
        levelLabel.setText("Level " + currentLevel);
        xpLabel.setText("XP: " + currentXp + " / " + requiredXp);
        xpProgressBar.setProgress((double) currentXp / requiredXp);
    }

    private void switchScene(ActionEvent event, String fxmlFile) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(fxmlFile));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}
