package com.example.kursach.Controllers;

import com.example.kursach.Classes.Hero.Hero;
import com.example.kursach.Classes.Movement.*;
import com.example.kursach.Classes.Tools.AppLogger;
import com.example.kursach.MainApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;

public class Lab1Controller implements Initializable {

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
        AppLogger.info("Инициализация контроллера Lab1Controller");
        initializeHero();
        setupControls();
        setupEventHandlers();
        AppLogger.info("Контроллер Lab1Controller успешно инициализирован");
    }

    private void initializeHero() {
        AppLogger.info("Создание героя и стратегий перемещения");
        hero = new Hero("Артур");
        strategies = new BaseMovement[]{
                new WalkMovement(outputArea),
                new HorseMovement(outputArea),
                new FlyMovement(outputArea),
                new SwimMovement(outputArea)
        };
        // default movement
        hero.setMovementStrategy(new WalkMovement(outputArea));
        AppLogger.info("Герой создан: Артур, стратегии перемещения инициализированы");
    }

    private void setupControls() {
        AppLogger.info("Настройка элементов управления");
        movementComboBox.getItems().addAll("Пешком", "На лошади", "Полёт", "Плавание");
        movementComboBox.getSelectionModel().selectFirst();

        moveButton.setStyle("-fx-font-size: 14px; -fx-padding: 10 50;");
        autoDemoButton.setStyle("-fx-font-size: 14px; -fx-padding: 8 16;");

        outputArea.setEditable(false);
        outputArea.setStyle("-fx-font-family: 'Monospaced'; -fx-font-size: 12px;");

        AppLogger.info("Элементы управления настроены");
    }

    private void setupEventHandlers() {
        AppLogger.info("Настройка обработчиков событий");
        movementComboBox.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    AppLogger.fine("Изменение стратегии перемещения: " + oldValue + " -> " + newValue);
                    updateMovementStrategy(newValue);
                }
        );

        moveButton.setOnAction(event -> handleMove());
        autoDemoButton.setOnAction(event -> handleAutoDemo());
        AppLogger.info("Обработчики событий настроены");
    }

    private void updateMovementStrategy(String strategyName) {
        AppLogger.info("Обновление стратегии перемещения: " + strategyName);
        switch (strategyName) {
            case "Пешком":
                hero.setMovementStrategy(strategies[0]);
                AppLogger.fine("Установлена стратегия: Пешком");
                break;
            case "На лошади":
                hero.setMovementStrategy(strategies[1]);
                AppLogger.fine("Установлена стратегия: На лошади");
                break;
            case "Полёт":
                hero.setMovementStrategy(strategies[2]);
                AppLogger.fine("Установлена стратегия: Полёт");
                break;
            case "Плавание":
                hero.setMovementStrategy(strategies[3]);
                AppLogger.fine("Установлена стратегия: Плавание");
                break;
        }
        appendOutput("Стратегия изменена на: " + strategyName);
        AppLogger.info("Стратегия успешно изменена на: " + strategyName);
    }

    @FXML
    private void handleMove() {
        AppLogger.info("Обработка перемещения героя");
        String from = startField.getText().trim();
        String to = endField.getText().trim();

        AppLogger.fine("Параметры перемещения: from='" + from + "', to='" + to + "'");

        if (from.isEmpty() || to.isEmpty()) {
            AppLogger.warning("Попытка перемещения с пустыми полями: from='" + from + "', to='" + to + "'");
            showAlert("Ошибка", "Заполните оба поля: начальная и конечная точки");
            return;
        }

        appendOutput("=== ПЕРЕМЕЩЕНИЕ ===");
        AppLogger.info("Начало перемещения: " + from + " -> " + to);
        hero.move(from, to);
        AppLogger.info("Перемещение завершено: " + from + " -> " + to);
    }

    @FXML
    private void handleAutoDemo() {
        AppLogger.info("Запуск автоматической демонстрации");
        appendOutput("\n=== АВТОМАТИЧЕСКАЯ ДЕМОНСТРАЦИЯ ===");
        String[] locations = {"деревня", "река", "лес", "гора", "пещера"};

        AppLogger.fine("Локации для демонстрации: " + String.join(" -> ", locations));

        for (int i = 0; i < locations.length - 1; i++) {
            String strategyName = movementComboBox.getItems().get(i % 4);
            movementComboBox.getSelectionModel().select(i % 4);

            appendOutput("\n--- Используем " + strategyName + " ---");
            AppLogger.info("Демонстрация шаг " + (i+1) + ": " + locations[i] + " -> " + locations[i+1] + " (" + strategyName + ")");

            hero.move(locations[i], locations[i + 1]);

            try {
                Thread.sleep(1000);
                AppLogger.fine("Пауза между перемещениями: 1000ms");
            } catch (InterruptedException e) {
                AppLogger.warning("Прерывание потока во время демонстрации");
                Thread.currentThread().interrupt();
                break;
            }
        }
        AppLogger.info("Автоматическая демонстрация завершена");
    }

    @FXML
    private void handleClearOutput() {
        AppLogger.info("Очистка области вывода");
        outputArea.clear();
        appendOutput("Очистка завершена. Готов к работе!");
        AppLogger.info("Область вывода очищена");
    }

    @FXML
    private void handleAbout() {
        AppLogger.info("Показана информация о программе");
        showAlert("О программе",
                "Курсовая работа - Паттерн 'Стратегия'\n" +
                        "Реализация различных способов перемещения героя");
    }

    private void appendOutput(String text) {
        outputArea.appendText(text + "\n");
        AppLogger.fine("Добавлен вывод: " + text);
    }

    private void showAlert(String title, String message) {
        AppLogger.info("Показано информационное окно: " + title + " - " + message);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleBackToMenu(ActionEvent event) {
        AppLogger.info("Нажата кнопка возврата в главное меню");

        try {
            URL url = MainApplication.class.getResource("main-menu.fxml");
            if (url == null) {
                AppLogger.severe("Файл main-menu.fxml не найден!");
                showAlert("Ошибка", "Файл главного меню не найден!");
                return;
            }

            FXMLLoader fxmlLoader = new FXMLLoader(url);
            Parent root = fxmlLoader.load();

            // Получаем текущее окно ДО создания нового
            Stage currentStage = (Stage) backToMenuButton.getScene().getWindow();

            // Создаем новое окно для меню
            Stage menuStage = new Stage();
            menuStage.setTitle("Курсовая работа - Главное меню");
            menuStage.setScene(new Scene(root, 800, 600));
            menuStage.show();

            // Закрываем текущее окно
            currentStage.close();

            AppLogger.info("Успешный переход в главное меню");

        } catch (IOException e) {
            AppLogger.log(Level.SEVERE, "Ошибка загрузки главного меню", e);
            showAlert("Ошибка", "Ошибка загрузки главного меню: " + e.getMessage());
        }
    }
}