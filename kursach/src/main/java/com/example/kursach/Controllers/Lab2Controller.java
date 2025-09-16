package com.example.kursach.Controllers;

import com.example.kursach.Classes.Hero.HeroTraining;
import com.example.kursach.Classes.Hero.Repeat;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ResourceBundle;

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
        trainingPanel.setDisable(true);
        outputArea.setEditable(false);
    }

    @FXML
    private void createHero() {
        String name = heroNameField.getText().trim();
        if (name.isEmpty()) {
            showAlert("Ошибка", "Введите имя героя");
            return;
        }

        hero = new HeroTraining(name);
        trainingPanel.setDisable(false);
        createHeroButton.setDisable(true);
        heroNameField.setDisable(true);

        appendOutput("Герой " + name + " создан! Начинаем тренировку!");
        updateStatsLabel();
    }

    @FXML
    private void doPushUps() {
        try {
            int count = Integer.parseInt(pushUpsCountField.getText());
            if (count <= 0) {
                showAlert("Ошибка", "Количество должно быть положительным");
                return;
            }

            executeWithRepeat("doPushUps", new Class[]{int.class}, new Object[]{count});
            updateStatsLabel();

        } catch (NumberFormatException e) {
            showAlert("Ошибка", "Введите корректное число");
        }
    }

    @FXML
    private void doRest() {
        // Метод rest() теперь возвращает строку
        String result = hero.rest();
        appendOutput(result);
        updateStatsLabel();
    }

    @FXML
    private void practiceArchery() {
        try {
            int arrows = Integer.parseInt(arrowsCountField.getText());
            if (arrows <= 0) {
                showAlert("Ошибка", "Количество должно быть положительным");
                return;
            }

            executeWithRepeat("practiceArchery", new Class[]{int.class}, new Object[]{arrows});
            updateStatsLabel();

        } catch (NumberFormatException e) {
            showAlert("Ошибка", "Введите корректное число");
        }
    }

    @FXML
    private void studySpell() {
        try {
            String spellName = spellNameField.getText().trim();
            int complexity = Integer.parseInt(spellComplexityField.getText());

            if (spellName.isEmpty()) {
                showAlert("Ошибка", "Введите название заклинания");
                return;
            }
            if (complexity <= 0) {
                showAlert("Ошибка", "Сложность должна быть положительной");
                return;
            }

            executeWithRepeat("studySpell", new Class[]{String.class, int.class},
                    new Object[]{spellName, complexity});
            updateStatsLabel();

        } catch (NumberFormatException e) {
            showAlert("Ошибка", "Введите корректную сложность");
        }
    }

    @FXML
    private void meditate() {
        boolean deep = deepMeditationCheckbox.isSelected();
        executeWithRepeat("meditate", new Class[]{boolean.class}, new Object[]{deep});
        updateStatsLabel();
    }

    @FXML
    private void runMarathon() {
        try {
            int distance = Integer.parseInt(distanceField.getText());
            int speed = Integer.parseInt(speedField.getText());

            if (distance <= 0 || speed <= 0) {
                showAlert("Ошибка", "Дистанция и скорость должны быть положительными");
                return;
            }

            executeWithRepeat("runMarathon", new Class[]{int.class, int.class},
                    new Object[]{distance, speed});
            updateStatsLabel();

        } catch (NumberFormatException e) {
            showAlert("Ошибка", "Введите корректные числа");
        }
    }

    @FXML
    private void liftWeights() {
        try {
            double weight = Double.parseDouble(weightField.getText());
            String weaponType = weaponTypeField.getText().trim();

            if (weight <= 0) {
                showAlert("Ошибка", "Вес должен быть положительным");
                return;
            }
            if (weaponType.isEmpty()) {
                showAlert("Ошибка", "Введите тип оружия");
                return;
            }

            executeWithRepeat("liftWeights", new Class[]{double.class, String.class},
                    new Object[]{weight, weaponType});
            updateStatsLabel();

        } catch (NumberFormatException e) {
            showAlert("Ошибка", "Введите корректный вес");
        }
    }

    @FXML
    private void showStats() {
        // Метод showStats() теперь возвращает строку
        String stats = hero.showStats();
        appendOutput(stats);
        updateStatsLabel();
    }

    @FXML
    private void checkBattleReady() {
        boolean ready = hero.isReadyForBattle();
        String message = ready ?
                hero.getHeroName() + " готов к битве! 💪" :
                hero.getHeroName() + " еще не готов к битве. Продолжайте тренироваться! 🏋️";

        appendOutput(message);

        if (!ready) {
            int needed = 50 - (hero.getStrength() + hero.getAgility() + hero.getIntelligence());
            appendOutput("Нужно еще " + needed + " очков характеристик");
        }
    }

    private void executeWithRepeat(String methodName, Class<?>[] parameterTypes, Object[] parameters) {
        try {
            Method method = HeroTraining.class.getDeclaredMethod(methodName, parameterTypes);
            method.setAccessible(true);

            Repeat repeatAnnotation = method.getAnnotation(Repeat.class);
            int repeatCount = repeatAnnotation != null ? repeatAnnotation.value() : 1;

            for (int i = 0; i < repeatCount; i++) {
                Object result = method.invoke(hero, parameters);
                if (result != null) {
                    appendOutput(result.toString());
                }
            }

        } catch (Exception e) {
            appendOutput("Ошибка при выполнении: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void updateStatsLabel() {
        if (hero != null) {
            heroStatsLabel.setText(String.format(
                    "Сила: %d | Ловкость: %d | Интеллект: %d | Общий: %d",
                    hero.getStrength(), hero.getAgility(), hero.getIntelligence(),
                    hero.getStrength() + hero.getAgility() + hero.getIntelligence()
            ));
        }
    }

    private void appendOutput(String text) {
        outputArea.appendText(text + "\n");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void clearOutput() {
        outputArea.clear();
    }

    @FXML
    private void resetHero() {
        createHeroButton.setDisable(false);
        heroNameField.setDisable(false);
        trainingPanel.setDisable(true);
        hero = null;
        heroStatsLabel.setText("Статистика героя");
        outputArea.clear();
        clearAllFields();
    }

    private void clearAllFields() {
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

}