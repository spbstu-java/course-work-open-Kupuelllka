package com.example.kursach.Controllers;

import com.example.kursach.Controllers.Lab1Controller;
import com.example.kursach.MainApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import java.io.IOException;

public class MainMenuController {

    @FXML
    private void openLab1(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("lab1.fxml"));
            Parent root = fxmlLoader.load();

            Lab1Controller controller = fxmlLoader.getController();

            Stage stage = new Stage();
            stage.setTitle("Лабораторная работа 1 - Паттерн Стратегия");
            stage.setScene(new Scene(root, 900, 700));
            stage.show();

            // Закрываем меню (опционально)
            closeCurrentWindow(event);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleExit(ActionEvent event) {
        closeCurrentWindow(event);
    }

    private void closeCurrentWindow(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
}