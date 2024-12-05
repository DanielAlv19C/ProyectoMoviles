package com.example.demo_proyecto;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class PlayerScore {
    @PrimaryKey(autoGenerate = true)
    private int id; // ID único de cada jugador

    private String name; // Nombre del jugador
    private int score;   // Puntaje

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}

