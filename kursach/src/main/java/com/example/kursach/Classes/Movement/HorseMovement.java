package com.example.kursach.Classes.Movement;

import javafx.scene.control.TextArea;

public class HorseMovement extends BaseMovement {
    public HorseMovement(TextArea outputArea) {
        super(outputArea);
    }

    @Override
    public void move(String from, String to) {
        appendOutput("Герой скачет на лошади из " + from + " в " + to);
    }
}
