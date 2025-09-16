package com.example.kursach.Controllers;

import com.example.kursach.MainApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import com.example.kursach.Classes.Hero.Hero;
import com.example.kursach.Classes.Movement.*;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Lab1Controller implements Initializable {

    @FXML private VBox mainContainer;
    @FXML private ComboBox<String> movementComboBox;
    @FXML private TextField startField;
    @FXML private TextField endField;
    @FXML private Button moveButton;
    @FXML private Button autoDemoButton;
    @FXML private TextArea outputArea;
    @FXML private Button backToMenuButton;

    private Hero hero;
    private BaseMovement[] strategies;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeHero();
        setupControls();
        setupEventHandlers();
    }

    private void initializeHero() {
        hero = new Hero("Артур");
        strategies = new BaseMovement[]{
                new WalkMovement(outputArea),
                new HorseMovement(outputArea),
                new FlyMovement(outputArea),
                new SwimMovement(outputArea)
        };
        // default movement
        hero.setMovementStrategy(new WalkMovement(outputArea));
    }

    private void setupControls() {
        movementComboBox.getItems().addAll("Пешком", "На лошади", "Полёт", "Плавание");
        movementComboBox.getSelectionModel().selectFirst();

        moveButton.setStyle("-fx-font-size: 14px; -fx-padding: 10 50;");
        autoDemoButton.setStyle("-fx-font-size: 14px; -fx-padding: 8 16;");

        outputArea.setEditable(false);
        outputArea.setStyle("-fx-font-family: 'Monospaced'; -fx-font-size: 12px;");

    }

    private void setupEventHandlers() {
        movementComboBox.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> updateMovementStrategy(newValue)
        );

        moveButton.setOnAction(event -> handleMove());
        autoDemoButton.setOnAction(event -> handleAutoDemo());
    }

    private void updateMovementStrategy(String strategyName) {
        switch (strategyName) {
            case "Пешком": hero.setMovementStrategy(strategies[0]); break;
            case "На лошади": hero.setMovementStrategy(strategies[1]); break;
            case "Полёт": hero.setMovementStrategy(strategies[2]); break;
            case "Плавание": hero.setMovementStrategy(strategies[3]); break;
        }
        appendOutput("Стратегия изменена на: " + strategyName);
    }

    @FXML
    private void handleMove() {
        String from = startField.getText().trim();
        String to = endField.getText().trim();

        if (from.isEmpty() || to.isEmpty()) {
            showAlert("Ошибка", "Заполните оба поля: начальная и конечная точки");
            return;
        }

        appendOutput("=== ПЕРЕМЕЩЕНИЕ ===");
        hero.move(from, to);
    }

    @FXML
    private void handleAutoDemo() {
        appendOutput("\n=== АВТОМАТИЧЕСКАЯ ДЕМОНСТРАЦИЯ ===");
        String[] locations = {"деревня", "река", "лес", "гора", "пещера"};

        for (int i = 0; i < locations.length - 1; i++) {
            String strategyName = movementComboBox.getItems().get(i % 4);
            movementComboBox.getSelectionModel().select(i % 4);

            appendOutput("\n--- Используем " + strategyName + " ---");
            hero.move(locations[i], locations[i + 1]);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    @FXML
    private void handleClearOutput() {
        outputArea.clear();
        appendOutput("Очистка завершена. Готов к работе!");
    }

    @FXML
    private void handleAbout() {
        showAlert("О программе",
                "Курсовая работа - Паттерн 'Стратегия'\n\n" +
                        "Реализация различных способов перемещения героя");
    }

    private void appendOutput(String text) {
        outputArea.appendText(text + "\n");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleBackToMenu(ActionEvent event) {
        try {
            URL url = MainApplication.class.getResource("main-menu.fxml");
            if (url == null) {
                System.out.println("Файл main-menu.fxml не найден!");
                return;
            }

            FXMLLoader fxmlLoader = new FXMLLoader(url);
            Parent root = fxmlLoader.load();

            // Получаем текущее окно ДО создания нового
            Stage currentStage = (Stage) backToMenuButton.getScene().getWindow();


            // Создаем новое окно для меню
            Stage menuStage = new Stage();
            menuStage.setTitle("Курсовая работа - Главное меню");
            menuStage.setScene(new Scene(root, 400, 300));
            menuStage.show();

            // Закрываем текущее окно
            currentStage.close();

        } catch (IOException e) {
            System.out.println("Ошибка загрузки: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void closeCurrentWindow(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
    public void setPrimaryStage(Stage primaryStage) {
        // Для дополнительных операций
    }
}