package com.example.demo_proyecto;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PlayerDao {
    // Método para insertar un jugador en la tabla
    @Insert
    void insert(PlayerScore player);

    // Método para obtener todos los jugadores
    @Query("SELECT * FROM PlayerScore")
    List<PlayerScore> getAll();
}
