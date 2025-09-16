package com.example.kursach.Controllers;

import com.example.kursach.Classes.Tools.AppLogger;
import com.example.kursach.Classes.Translator.Exceptions;
import com.example.kursach.Classes.Translator.Translator;
import com.example.kursach.MainApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class Lab3Controller implements Initializable {
    @FXML
    public Button backToMenuButton;

    @FXML
    private ComboBox<String> dictionaryComboBox;

    @FXML
    private Label dictionaryStatusLabel;

    @FXML
    private TextArea inputTextArea;

    @FXML
    private TextArea outputTextArea;

    @FXML
    private Label statusLabel;

    @FXML
    private Button loadDictionaryButton; // Кнопка для загрузки кастомных словарей

    private Translator currentTranslator;
    private Map<String, Translator> translatorsCache;
    private Map<String, String> availableDictionaries;
    private Stage stage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        AppLogger.info("Инициализация контроллера переводчика");
        translatorsCache = new HashMap<>();
        setupDictionaries();
        setupDictionaryComboBoxListener();
        loadDefaultDictionary();
        updateDictionaryStatus();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private void setupDictionaries() {
        availableDictionaries = new HashMap<>();

        // Предустановленные словари
        availableDictionaries.put("Англо-русский (базовый)", "src/main/resources/dictionaries/english_russian.txt");
        availableDictionaries.put("Русско-английский (базовый)", "src/main/resources/dictionaries/russian_english.txt");
        availableDictionaries.put("Немецко-русский", "src/main/resources/dictionaries/german_russian.txt");
        availableDictionaries.put("Пользовательский словарь...", "custom");

        dictionaryComboBox.getItems().addAll(availableDictionaries.keySet());
        dictionaryComboBox.getSelectionModel().selectFirst();
    }

    private void setupDictionaryComboBoxListener() {
        // Слушатель изменения выбора в ComboBox
        dictionaryComboBox.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null && !newValue.equals(oldValue)) {
                        handleDictionarySelection(newValue);
                    }
                }
        );
    }

    private void handleDictionarySelection(String selectedDictName) {
        String dictPath = availableDictionaries.get(selectedDictName);

        if ("custom".equals(dictPath)) {
            // Для кастомного словаря просто меняем статус, но не загружаем автоматически
            statusLabel.setText("Выберите пользовательский словарь через кнопку 'Загрузить'");
            statusLabel.setStyle("-fx-text-fill: blue;");
            dictionaryStatusLabel.setText("Словарь не загружен");
            dictionaryStatusLabel.setStyle("-fx-text-fill: red;");
            currentTranslator = null;
        } else {
            // Для предустановленных словарей загружаем автоматически
            try {
                AppLogger.info("Автоматическая загрузка словаря: " + selectedDictName);
                loadDictionaryFromPath(selectedDictName, dictPath);
                statusLabel.setText("Словарь '" + selectedDictName + "' загружен");
                statusLabel.setStyle("-fx-text-fill: green;");
                updateDictionaryStatus();
            } catch (Exceptions.DictionaryLoadException e) {
                AppLogger.severe("Ошибка автоматической загрузки словаря: " + e.getMessage());
                statusLabel.setText("Ошибка загрузки словаря");
                statusLabel.setStyle("-fx-text-fill: red;");
                dictionaryStatusLabel.setText("Словарь не загружен");
                dictionaryStatusLabel.setStyle("-fx-text-fill: red;");
            }
        }
    }

    // Загрузка словаря по умолчанию при инициализации
    private void loadDefaultDictionary() {
        String defaultDictName = dictionaryComboBox.getSelectionModel().getSelectedItem();
        String dictPath = availableDictionaries.get(defaultDictName);

        if (!"custom".equals(dictPath)) {
            try {
                AppLogger.info("Загрузка словаря по умолчанию: " + defaultDictName);
                loadDictionaryFromPath(defaultDictName, dictPath);
                statusLabel.setText("Словарь по умолчанию загружен");
                statusLabel.setStyle("-fx-text-fill: green;");
            } catch (Exceptions.DictionaryLoadException e) {
                AppLogger.severe("Ошибка загрузки словаря по умолчанию: " + e.getMessage());
                statusLabel.setText("Ошибка загрузки словаря по умолчанию");
                statusLabel.setStyle("-fx-text-fill: red;");
            }
        }
    }

    @FXML
    private void loadDictionary() {
        String selectedDictName = dictionaryComboBox.getSelectionModel().getSelectedItem();

        if (selectedDictName == null) {
            showAlert("Ошибка", "Выберите словарь из списка");
            return;
        }

        String dictPath = availableDictionaries.get(selectedDictName);

        try {
            if ("custom".equals(dictPath)) {
                // Выбор пользовательского файла
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Выберите файл словаря");
                fileChooser.getExtensionFilters().add(
                        new FileChooser.ExtensionFilter("Текстовые файлы", "*.txt")
                );
                File selectedFile = fileChooser.showOpenDialog(stage);

                if (selectedFile != null) {
                    dictPath = selectedFile.getAbsolutePath();
                    // Для пользовательских словарей используем путь как ключ
                    loadDictionaryFromPath(dictPath, dictPath);
                    statusLabel.setText("Пользовательский словарь загружен");
                    statusLabel.setStyle("-fx-text-fill: green;");
                    updateDictionaryStatus();
                }
            } else {
                // Для предустановленных словарей просто подтверждаем, что они уже загружены
                statusLabel.setText("Словарь '" + selectedDictName + "' уже загружен");
                statusLabel.setStyle("-fx-text-fill: green;");
            }

        } catch (Exceptions.DictionaryLoadException e) {
            AppLogger.severe("Ошибка загрузки словаря: " + e.getMessage());
            showAlert("Ошибка загрузки", "Не удалось загрузить словарь: " + e.getMessage());
            statusLabel.setText("Ошибка загрузки словаря");
            statusLabel.setStyle("-fx-text-fill: red;");
        }
    }

    // Вспомогательный метод для загрузки словаря с кэшированием
    private void loadDictionaryFromPath(String cacheKey, String dictPath) throws Exceptions.DictionaryLoadException {
        // Проверяем, есть ли уже загруженный словарь в кэше
        if (translatorsCache.containsKey(cacheKey)) {
            AppLogger.info("Используем кэшированный словарь: " + cacheKey);
            currentTranslator = translatorsCache.get(cacheKey);
        } else {
            // Загружаем новый словарь и сохраняем в кэш
            AppLogger.info("Загрузка нового словаря: " + dictPath);
            Translator newTranslator = new Translator(dictPath);
            newTranslator.loadDictionary();
            translatorsCache.put(cacheKey, newTranslator);
            currentTranslator = newTranslator;
        }
    }

    @FXML
    private void translateText() {
        if (currentTranslator == null || !currentTranslator.isDictionaryLoaded()) {
            showAlert("Ошибка", "Сначала загрузите словарь");
            return;
        }

        String text = inputTextArea.getText().trim();

        if (text.isEmpty()) {
            showAlert("Ошибка", "Введите текст для перевода");
            return;
        }

        try {
            AppLogger.info("Начало перевода текста через GUI");
            String translation = currentTranslator.translateText(text);
            outputTextArea.setText(translation);

            statusLabel.setText("Перевод выполнен успешно");
            statusLabel.setStyle("-fx-text-fill: green;");

        } catch (Exceptions.TranslationException e) {
            AppLogger.severe("Ошибка перевода: " + e.getMessage());
            outputTextArea.setText("Ошибка перевода: " + e.getMessage());
            statusLabel.setText("Ошибка перевода");
            statusLabel.setStyle("-fx-text-fill: red;");
        } catch (Exception e) {
            AppLogger.severe("Неожиданная ошибка при переводе: " + e.getMessage());
            outputTextArea.setText("Произошла непредвиденная ошибка");
            statusLabel.setText("Неожиданная ошибка");
            statusLabel.setStyle("-fx-text-fill: red;");
        }
    }

    @FXML
    private void clearText() {
        inputTextArea.clear();
        outputTextArea.clear();
        statusLabel.setText("Текст очищен");
        statusLabel.setStyle("-fx-text-fill: blue;");
    }

    @FXML
    private void startInteractiveMode() {
        if (currentTranslator == null || !currentTranslator.isDictionaryLoaded()) {
            showAlert("Ошибка", "Сначала загрузите словарь");
            return;
        }

        // Создаем новое окно для интерактивного режима
        Stage interactiveStage = new Stage();
        interactiveStage.setTitle("Интерактивный режим переводчика");

        // Создаем текстовую область для вывода
        TextArea outputArea = new TextArea();
        outputArea.setEditable(false);
        outputArea.setWrapText(true);
        outputArea.setPrefSize(400, 300);

        // Создаем поле ввода
        TextField inputField = new TextField();
        inputField.setPromptText("Введите текст для перевода...");

        // Кнопка перевода
        Button translateButton = new Button("Перевести");

        VBox root = new VBox(10);
        root.setPadding(new javafx.geometry.Insets(10));
        root.getChildren().addAll(
                new Label("Интерактивный режим:"),
                inputField,
                translateButton,
                new Label("Результат:"),
                outputArea
        );

        // Обработчик перевода
        translateButton.setOnAction(e -> {
            String text = inputField.getText().trim();
            if (!text.isEmpty()) {
                try {
                    String translation = currentTranslator.translateText(text);
                    outputArea.appendText("> " + text + "\n");
                    outputArea.appendText("Перевод: " + translation + "\n\n");
                    inputField.clear();
                } catch (Exception ex) {
                    outputArea.appendText("Ошибка: " + ex.getMessage() + "\n\n");
                }
            }
        });

        // Обработчик нажатия Enter в поле ввода
        inputField.setOnAction(e -> translateButton.fire());

        interactiveStage.setScene(new javafx.scene.Scene(root));
        interactiveStage.show();

        AppLogger.info("Запущен интерактивный режим");
    }

    @FXML
    private void checkWord() {
        if (currentTranslator == null || !currentTranslator.isDictionaryLoaded()) {
            showAlert("Ошибка", "Сначала загрузите словарь");
            return;
        }

        String word = showInputDialog("Проверка слова", "Введите слово для проверки:");

        if (word != null && !word.trim().isEmpty()) {
            try {
                // Используем текущий переводчик для проверки слова
                // (предполагая, что у Translator есть соответствующие методы)
                boolean exists = currentTranslator.wordExists(word.trim());

                if (exists) {
                    String translation = currentTranslator.translateWord(word.trim());
                    showAlert("Результат проверки",
                            "Слово '" + word + "' найдено в словаре.\nПеревод: " + translation);
                } else {
                    showAlert("Результат проверки",
                            "Слово '" + word + "' не найдено в словаре.");
                }

            } catch (Exception e) {
                showAlert("Ошибка", "Ошибка при проверке слова: " + e.getMessage());
            }
        }
    }

    @FXML
    private void exitApplication() {
        AppLogger.info("Завершение работы приложения");
        if (stage != null) {
            stage.close();
        }
    }

    @FXML
    public void handleBackToMenu(ActionEvent actionEvent) {
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
            menuStage.setScene(new Scene(root, 800, 600));
            menuStage.show();

            // Закрываем текущее окно
            currentStage.close();

        } catch (IOException e) {
            System.out.println("Ошибка загрузки: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void updateDictionaryStatus() {
        if (currentTranslator != null && currentTranslator.isDictionaryLoaded()) {
            dictionaryStatusLabel.setText("Загружено выражений: " + currentTranslator.getDictionarySize());
            dictionaryStatusLabel.setStyle("-fx-text-fill: green;");
        } else {
            dictionaryStatusLabel.setText("Словарь не загружен");
            dictionaryStatusLabel.setStyle("-fx-text-fill: red;");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private String showInputDialog(String title, String message) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(title);
        dialog.setHeaderText(null);
        dialog.setContentText(message);

        return dialog.showAndWait().orElse(null);
    }
}