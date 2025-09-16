package com.example.kursach.Controllers;

import com.example.kursach.Classes.Stream.StreamExceptions;
import com.example.kursach.Classes.Stream.StreamUtils;
import com.example.kursach.Classes.Tools.AppLogger;
import com.example.kursach.MainApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class Lab4Controller {
    @FXML
    public Button backToMenuButton;

    @FXML
    private TextArea inputTextArea;

    @FXML
    private TextArea outputTextArea;

    @FXML
    private ComboBox<String> operationComboBox;

    @FXML
    private Button executeButton;

    @FXML
    private Button clearButton;

    @FXML
    private VBox resultContainer;

    @FXML
    private Label resultLabel;

    @FXML
    public void initialize() {
        AppLogger.info("Инициализация контроллера Lab4Controller");

        // Заполняем комбобокс операциями
        operationComboBox.getItems().addAll(
                "Среднее значение чисел",
                "Строки в верхний регистр с префиксом",
                "Квадраты уникальных элементов",
                "Последний элемент коллекции",
                "Сумма четных чисел",
                "Строки в Map"
        );
        operationComboBox.getSelectionModel().selectFirst();

        // Настройка обработчиков событий
        setupEventHandlers();

        AppLogger.info("Контроллер Lab4Controller успешно инициализирован");
    }

    private void setupEventHandlers() {
        executeButton.setOnAction(event -> executeOperation());
        clearButton.setOnAction(event -> clearFields());
    }

    private void executeOperation() {
        try {
            String selectedOperation = operationComboBox.getValue();
            String inputText = inputTextArea.getText().trim();

            AppLogger.info("Выполнение операции: " + selectedOperation);
            AppLogger.fine("Входные данные: " + inputText);

            if (inputText.isEmpty()) {
                AppLogger.warning("Попытка выполнения операции с пустыми входными данными");
                showError("Введите данные для обработки");
                return;
            }

            String result = processOperation(selectedOperation, inputText);
            outputTextArea.setText(result);
            resultLabel.setText("Операция выполнена успешно!");
            resultLabel.setStyle("-fx-text-fill: green;");

            AppLogger.info("Операция выполнена успешно: " + selectedOperation);

        } catch (StreamExceptions.EmptyCollectionException e) {
            AppLogger.warning("Ошибка EmptyCollectionException: " + e.getMessage());
            showError("Ошибка: " + e.getMessage());
        } catch (StreamExceptions.InvalidInputException e) {
            AppLogger.warning("Ошибка InvalidInputException: " + e.getMessage());
            showError("Неверный ввод: " + e.getMessage());
        } catch (StreamExceptions.StreamProcessingException e) {
            AppLogger.warning("Ошибка StreamProcessingException: " + e.getMessage());
            showError("Ошибка обработки: " + e.getMessage());
        } catch (Exception e) {
            AppLogger.log(Level.SEVERE, "Неожиданная ошибка при выполнении операции", e);
            showError("Неожиданная ошибка: " + e.getMessage());
        }
    }

    private String processOperation(String operation, String input) {
        AppLogger.fine("Обработка операции: " + operation);
        return switch (operation) {
            case "Среднее значение чисел" -> processAverage(input);
            case "Строки в верхний регистр с префиксом" -> processUpperCaseWithPrefix(input);
            case "Квадраты уникальных элементов" -> processSquaresOfUnique(input);
            case "Последний элемент коллекции" -> processLastElement(input);
            case "Сумма четных чисел" -> processSumOfEven(input);
            case "Строки в Map" -> processStringsToMap(input);
            default -> {
                AppLogger.warning("Неизвестная операция: " + operation);
                yield "Неизвестная операция";
            }
        };
    }

    private String processAverage(String input) {
        try {
            List<Integer> numbers = parseIntegerList(input);
            double average = StreamUtils.average(numbers);
            AppLogger.fine("Среднее значение вычислено: " + average);
            return String.format("Среднее значение: %.2f\nИсходные числа: %s",
                    average, numbers);
        } catch (Exception e) {
            AppLogger.log(Level.WARNING, "Ошибка при вычислении среднего значения", e);
            throw e;
        }
    }

    private String processUpperCaseWithPrefix(String input) {
        try {
            List<String> strings = Arrays.asList(input.split("\n"));
            List<String> result = StreamUtils.toUpperCaseWithPrefix(strings);
            AppLogger.fine("Строки преобразованы в верхний регистр с префиксом");
            return "Результат:\n" + String.join("\n", result) +
                    "\n\nИсходные строки:\n" + input;
        } catch (Exception e) {
            AppLogger.log(Level.WARNING, "Ошибка при преобразовании строк в верхний регистр", e);
            throw e;
        }
    }

    private String processSquaresOfUnique(String input) {
        try {
            List<Integer> numbers = parseIntegerList(input);
            List<Integer> result = StreamUtils.squaresOfUniqueElements(numbers);
            AppLogger.fine("Квадраты уникальных элементов вычислены: " + result);
            return String.format("Квадраты уникальных элементов: %s\nИсходные числа: %s",
                    result, numbers);
        } catch (Exception e) {
            AppLogger.log(Level.WARNING, "Ошибка при вычислении квадратов уникальных элементов", e);
            throw e;
        }
    }

    private String processLastElement(String input) {
        try {
            List<String> elements = Arrays.asList(input.split("\n"));
            String lastElement = StreamUtils.getLastElement(elements);
            AppLogger.fine("Последний элемент получен: " + lastElement);
            return String.format("Последний элемент: %s\nВсе элементы: %s",
                    lastElement, elements);
        } catch (Exception e) {
            AppLogger.log(Level.WARNING, "Ошибка при получении последнего элемента", e);
            throw e;
        }
    }

    private String processSumOfEven(String input) {
        try {
            int[] numbers = parseIntArray(input);
            int sum = StreamUtils.sumOfEvenNumbers(numbers);
            AppLogger.fine("Сумма четных чисел вычислена: " + sum);
            return String.format("Сумма четных чисел: %d\nИсходный массив: %s",
                    sum, Arrays.toString(numbers));
        } catch (Exception e) {
            AppLogger.log(Level.WARNING, "Ошибка при вычислении суммы четных чисел", e);
            throw e;
        }
    }

    private String processStringsToMap(String input) {
        try {
            List<String> strings = Arrays.asList(input.split("\n"));
            Map<Character, String> result = StreamUtils.stringsToMap(strings);
            AppLogger.fine("Строки преобразованы в Map");
            return String.format("Результирующая Map:\n%s\nИсходные строки:\n%s",
                    formatMap(result), input);
        } catch (Exception e) {
            AppLogger.log(Level.WARNING, "Ошибка при преобразовании строк в Map", e);
            throw e;
        }
    }

    private List<Integer> parseIntegerList(String input) {
        try {
            return Arrays.stream(input.split("[,\\s]+"))
                    .filter(s -> !s.isEmpty())
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
        } catch (NumberFormatException e) {
            AppLogger.warning("Ошибка парсинга целых чисел из строки: " + input);
            throw new StreamExceptions.InvalidInputException("Некорректный формат чисел");
        }
    }

    private int[] parseIntArray(String input) {
        try {
            return Arrays.stream(input.split("[,\\s]+"))
                    .filter(s -> !s.isEmpty())
                    .mapToInt(Integer::parseInt)
                    .toArray();
        } catch (NumberFormatException e) {
            AppLogger.warning("Ошибка парсинга целых чисел в массив из строки: " + input);
            throw new StreamExceptions.InvalidInputException("Некорректный формат чисел");
        }
    }

    private String formatMap(Map<Character, String> map) {
        return map.entrySet().stream()
                .map(entry -> String.format("'%s' -> \"%s\"", entry.getKey(), entry.getValue()))
                .collect(Collectors.joining("\n"));
    }

    @FXML
    public void handleBackToMenu(ActionEvent actionEvent) {
        AppLogger.info("Нажата кнопка возврата в главное меню");

        try {
            URL url = MainApplication.class.getResource("main-menu.fxml");
            if (url == null) {
                AppLogger.severe("Файл main-menu.fxml не найден!");
                showError("Файл главного меню не найден!");
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
            showError("Ошибка загрузки главного меню: " + e.getMessage());
        }
    }

    private void showError(String message) {
        resultLabel.setText(message);
        resultLabel.setStyle("-fx-text-fill: red;");
        outputTextArea.setText("");
    }

    private void clearFields() {
        AppLogger.fine("Очистка полей ввода/вывода");
        inputTextArea.clear();
        outputTextArea.clear();
        resultLabel.setText("");
    }
}