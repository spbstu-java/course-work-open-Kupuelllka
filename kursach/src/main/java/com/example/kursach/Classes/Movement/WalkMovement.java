package com.example.kursach.Classes.Movement;

import javafx.scene.control.TextArea;

public class WalkMovement extends BaseMovement {
    public WalkMovement(TextArea outputArea) {
        super(outputArea);
    }

    @Override
    public void move(String from, String to) {
        appendOutput("Герой идет пешком из " + from + " в " + to);
    }
}
