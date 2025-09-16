package com.example.kursach.Classes.Movement;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

public abstract class BaseMovement {
    protected TextArea outputArea;

    public BaseMovement(TextArea outputArea) {
        this.outputArea = outputArea;
    }

    public abstract void move(String from, String to);

    protected void appendOutput(String text) {
        Platform.runLater(() -> {
            outputArea.appendText(text + "\n");
        });
    }
}