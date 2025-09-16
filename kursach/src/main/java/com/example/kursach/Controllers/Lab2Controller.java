package com.example.kursach.Controllers;

import com.example.kursach.Classes.Hero.HeroTraining;
import com.example.kursach.Classes.Hero.Repeat;
import com.example.kursach.Classes.Tools.AppLogger;
import com.example.kursach.MainApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;

public class Lab2Controller implements Initializable {

    @FXML
    private TextField heroNameField;

    @FXML
    private Button createHeroButton;

    @FXML
    private VBox trainingPanel;

    @FXML
    private TextField pushUpsCountField;

    @FXML
    private Button backToMenuButton;

    @FXML
    private Button pushUpsButton;

    @FXML
    private Button restButton;

    @FXML
    private TextField arrowsCountField;

    @FXML
    private Button archeryButton;

    @FXML
    private TextField spellNameField;

    @FXML
    private TextField spellComplexityField;

    @FXML
    private Button studySpellButton;

    @FXML
    private CheckBox deepMeditationCheckbox;

    @FXML
    private Button meditateButton;

    @FXML
    private TextField distanceField;

    @FXML
    private TextField speedField;

    @FXML
    private Button runMarathonButton;

    @FXML
    private TextField weightField;

    @FXML
    private TextField weaponTypeField;

    @FXML
    private Button liftWeightsButton;

    @FXML
    private Button showStatsButton;

    @FXML
    private Button checkBattleReadyButton;

    @FXML
    private TextArea outputArea;

    @FXML
    private Label heroStatsLabel;

    private HeroTraining hero;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        AppLogger.info("Инициализация контроллера Lab2Controller");
        trainingPanel.setDisable(true);
        outputArea.setEditable(false);
        AppLogger.info("Контроллер Lab2Controller успешно инициализирован");
    }

    @FXML
    private void createHero() {
        String name = heroNameField.getText().trim();
        AppLogger.info("Попытка создания героя с именем: " + name);

        if (name.isEmpty()) {
            AppLogger.warning("Попытка создания героя с пустым именем");
            showAlert("Ошибка", "Введите имя героя");
            return;
        }

        hero = new HeroTraining(name);
        trainingPanel.setDisable(false);
        createHeroButton.setDisable(true);
        heroNameField.setDisable(true);

        appendOutput("Герой " + name + " создан! Начинаем тренировку!");
        updateStatsLabel();

        AppLogger.info("Герой успешно создан: " + name);
    }

    @FXML
    private void doPushUps() {
        AppLogger.info("Выполнение отжиманий");
        try {
            int count = Integer.parseInt(pushUpsCountField.getText());
            AppLogger.fine("Количество отжиманий: " + count);

            if (count <= 0) {
                AppLogger.warning("Некорректное количество отжиманий: " + count);
                showAlert("Ошибка", "Количество должно быть положительным");
                return;
            }

            executeWithRepeat("doPushUps", new Class[]{int.class}, new Object[]{count});
            updateStatsLabel();
            AppLogger.info("Отжимания выполнены успешно: " + count + " раз");

        } catch (NumberFormatException e) {
            AppLogger.warning("Ошибка парсинга количества отжиманий: " + pushUpsCountField.getText());
            showAlert("Ошибка", "Введите корректное число");
        }
    }

    @FXML
    private void doRest() {
        AppLogger.info("Выполнение отдыха");
        // Метод rest() теперь возвращает строку
        String result = hero.rest();
        appendOutput(result);
        updateStatsLabel();
        AppLogger.info("Отдых выполнен: " + result);
    }

    @FXML
    private void practiceArchery() {
        AppLogger.info("Практика стрельбы из лука");
        try {
            int arrows = Integer.parseInt(arrowsCountField.getText());
            AppLogger.fine("Количество стрел: " + arrows);

            if (arrows <= 0) {
                AppLogger.warning("Некорректное количество стрел: " + arrows);
                showAlert("Ошибка", "Количество должно быть положительным");
                return;
            }

            executeWithRepeat("practiceArchery", new Class[]{int.class}, new Object[]{arrows});
            updateStatsLabel();
            AppLogger.info("Стрельба из лука выполнена: " + arrows + " стрел");

        } catch (NumberFormatException e) {
            AppLogger.warning("Ошибка парсинга количества стрел: " + arrowsCountField.getText());
            showAlert("Ошибка", "Введите корректное число");
        }
    }

    @FXML
    private void studySpell() {
        AppLogger.info("Изучение заклинания");
        try {
            String spellName = spellNameField.getText().trim();
            int complexity = Integer.parseInt(spellComplexityField.getText());

            AppLogger.fine("Заклинание: " + spellName + ", сложность: " + complexity);

            if (spellName.isEmpty()) {
                AppLogger.warning("Пустое название заклинания");
                showAlert("Ошибка", "Введите название заклинания");
                return;
            }
            if (complexity <= 0) {
                AppLogger.warning("Некорректная сложность заклинания: " + complexity);
                showAlert("Ошибка", "Сложность должна быть положительной");
                return;
            }

            executeWithRepeat("studySpell", new Class[]{String.class, int.class},
                    new Object[]{spellName, complexity});
            updateStatsLabel();
            AppLogger.info("Заклинание изучено: " + spellName + " (сложность: " + complexity + ")");

        } catch (NumberFormatException e) {
            AppLogger.warning("Ошибка парсинга сложности заклинания: " + spellComplexityField.getText());
            showAlert("Ошибка", "Введите корректную сложность");
        }
    }

    @FXML
    private void meditate() {
        boolean deep = deepMeditationCheckbox.isSelected();
        AppLogger.info("Медитация (глубокая: " + deep + ")");

        executeWithRepeat("meditate", new Class[]{boolean.class}, new Object[]{deep});
        updateStatsLabel();
        AppLogger.info("Медитация завершена (глубокая: " + deep + ")");
    }

    @FXML
    private void runMarathon() {
        AppLogger.info("Запуск марафона");
        try {
            int distance = Integer.parseInt(distanceField.getText());
            int speed = Integer.parseInt(speedField.getText());

            AppLogger.fine("Дистанция: " + distance + ", скорость: " + speed);

            if (distance <= 0 || speed <= 0) {
                AppLogger.warning("Некорректные параметры марафона: distance=" + distance + ", speed=" + speed);
                showAlert("Ошибка", "Дистанция и скорость должны быть положительными");
                return;
            }

            executeWithRepeat("runMarathon", new Class[]{int.class, int.class},
                    new Object[]{distance, speed});
            updateStatsLabel();
            AppLogger.info("Марафон завершен: дистанция=" + distance + ", скорость=" + speed);

        } catch (NumberFormatException e) {
            AppLogger.warning("Ошибка парсинга параметров марафона");
            showAlert("Ошибка", "Введите корректные числа");
        }
    }

    @FXML
    private void liftWeights() {
        AppLogger.info("Поднятие тяжестей");
        try {
            double weight = Double.parseDouble(weightField.getText());
            String weaponType = weaponTypeField.getText().trim();

            AppLogger.fine("Вес: " + weight + ", тип оружия: " + weaponType);

            if (weight <= 0) {
                AppLogger.warning("Некорректный вес: " + weight);
                showAlert("Ошибка", "Вес должен быть положительным");
                return;
            }
            if (weaponType.isEmpty()) {
                AppLogger.warning("Пустой тип оружия");
                showAlert("Ошибка", "Введите тип оружия");
                return;
            }

            executeWithRepeat("liftWeights", new Class[]{double.class, String.class},
                    new Object[]{weight, weaponType});
            updateStatsLabel();
            AppLogger.info("Тяжести подняты: вес=" + weight + ", оружие=" + weaponType);

        } catch (NumberFormatException e) {
            AppLogger.warning("Ошибка парсинга веса: " + weightField.getText());
            showAlert("Ошибка", "Введите корректный вес");
        }
    }

    @FXML
    private void showStats() {
        AppLogger.info("Показать статистику героя");
        // Метод showStats() теперь возвращает строку
        String stats = hero.showStats();
        appendOutput(stats);
        updateStatsLabel();
        AppLogger.fine("Статистика героя: " + stats.replace("\n", " | "));
    }

    @FXML
    private void checkBattleReady() {
        AppLogger.info("Проверка готовности к битве");
        boolean ready = hero.isReadyForBattle();
        String message = ready ?
                hero.getHeroName() + " готов к битве! 💪" :
                hero.getHeroName() + " еще не готов к битве. Продолжайте тренироваться! 🏋️";

        appendOutput(message);

        if (!ready) {
            int needed = 50 - (hero.getStrength() + hero.getAgility() + hero.getIntelligence());
            appendOutput("Нужно еще " + needed + " очков характеристик");
            AppLogger.info("Герой не готов к битве. Нужно еще " + needed + " очков");
        } else {
            AppLogger.info("Герой готов к битве!");
        }
    }

    private void executeWithRepeat(String methodName, Class<?>[] parameterTypes, Object[] parameters) {
        try {
            AppLogger.fine("Выполнение метода с повторением: " + methodName);
            Method method = HeroTraining.class.getDeclaredMethod(methodName, parameterTypes);
            method.setAccessible(true);

            Repeat repeatAnnotation = method.getAnnotation(Repeat.class);
            int repeatCount = repeatAnnotation != null ? repeatAnnotation.value() : 1;

            AppLogger.fine("Количество повторений: " + repeatCount);

            for (int i = 0; i < repeatCount; i++) {
                Object result = method.invoke(hero, parameters);
                if (result != null) {
                    appendOutput(result.toString());
                    AppLogger.fine("Результат выполнения [" + (i+1) + "]: " + result.toString());
                }
            }

        } catch (Exception e) {
            AppLogger.log(Level.SEVERE, "Ошибка при выполнении метода: " + methodName, e);
            appendOutput("Ошибка при выполнении: " + e.getMessage());
        }
    }

    private void updateStatsLabel() {
        if (hero != null) {
            String stats = String.format(
                    "Сила: %d | Ловкость: %d | Интеллект: %d | Общий: %d",
                    hero.getStrength(), hero.getAgility(), hero.getIntelligence(),
                    hero.getStrength() + hero.getAgility() + hero.getIntelligence()
            );
            heroStatsLabel.setText(stats);
            AppLogger.fine("Обновлена статистика: " + stats);
        }
    }

    private void appendOutput(String text) {
        outputArea.appendText(text + "\n");
    }

    private void showAlert(String title, String message) {
        AppLogger.warning("Показано предупреждение: " + title + " - " + message);
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void clearOutput() {
        AppLogger.info("Очистка вывода");
        outputArea.clear();
    }

    @FXML
    private void resetHero() {
        AppLogger.info("Сброс героя");
        createHeroButton.setDisable(false);
        heroNameField.setDisable(false);
        trainingPanel.setDisable(true);
        hero = null;
        heroStatsLabel.setText("Статистика героя");
        outputArea.clear();
        clearAllFields();
        AppLogger.info("Герой сброшен, интерфейс восстановлен");
    }

    private void clearAllFields() {
        AppLogger.fine("Очистка всех полей ввода");
        pushUpsCountField.clear();
        arrowsCountField.clear();
        spellNameField.clear();
        spellComplexityField.clear();
        distanceField.clear();
        speedField.clear();
        weightField.clear();
        weaponTypeField.clear();
        deepMeditationCheckbox.setSelected(false);
    }

    @FXML
    public void handleBackToMenu(ActionEvent actionEvent) {
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