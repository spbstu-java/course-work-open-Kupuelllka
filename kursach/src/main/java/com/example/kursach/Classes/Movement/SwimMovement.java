package com.example.kursach.Classes.Movement;

import javafx.scene.control.TextArea;

public class SwimMovement extends BaseMovement {
    public SwimMovement(TextArea outputArea) {
        super(outputArea);
    }

    @Override
    public void move(String from, String to) {
        appendOutput("Герой плывет из " + from + " в " + to);
    }
}