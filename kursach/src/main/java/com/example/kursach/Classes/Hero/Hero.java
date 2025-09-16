package com.example.kursach.Classes.Hero;

import com.example.kursach.Classes.Movement.BaseMovement;
import com.example.kursach.Classes.Movement.WalkMovement;

public class Hero {
    private String name;
    private BaseMovement typeMovement;

    public Hero(String name) {
        this.name = name;
    }

    public void setMovementStrategy(BaseMovement typeMovement) {
        this.typeMovement = typeMovement;
    }

    public void move(String from, String to) {
        String message = name + ": ";
        typeMovement.move(from, to);
    }

    public String getCurrentMovementType() {
        return typeMovement.getClass().getSimpleName().replace("Movement", "");
    }
}