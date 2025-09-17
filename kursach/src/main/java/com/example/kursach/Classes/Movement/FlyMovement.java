package com.example.kursach.Classes.Movement;

import javafx.scene.control.TextArea;

public class FlyMovement extends BaseMovement {
    public FlyMovement(TextArea outputArea) {
        super(outputArea);
    }

    @Override
    public void move(String from, String to) {
        appendOutput("Герой летит из " + from + " в " + to);
    }
}
