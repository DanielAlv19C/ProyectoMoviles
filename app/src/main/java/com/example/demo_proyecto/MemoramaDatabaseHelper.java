package com.example.demo_proyecto;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MemoramaDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "memorama.db";
    private static final int DATABASE_VERSION = 2; // Incrementa la versión

    // Tabla para partidas multijugador
    private static final String TABLE_PARTIDAS_MULTIJUGADOR = "partidas_multijugador";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_JUGADOR1 = "jugador1";
    private static final String COLUMN_JUGADOR2 = "jugador2";
    private static final String COLUMN_PUNTAJE_JUGADOR1 = "puntaje_jugador1";
    private static final String COLUMN_PUNTAJE_JUGADOR2 = "puntaje_jugador2";
    private static final String COLUMN_GANADOR = "ganador";

    public MemoramaDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Crear tabla para el memorama en solitario (deja tu código existente aquí)
        db.execSQL("CREATE TABLE partidas_solitario ("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "jugador TEXT, "
                + "puntaje INTEGER);");

        // Crear tabla para partidas multijugador
        db.execSQL("CREATE TABLE " + TABLE_PARTIDAS_MULTIJUGADOR + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_JUGADOR1 + " TEXT, "
                + COLUMN_JUGADOR2 + " TEXT, "
                + COLUMN_PUNTAJE_JUGADOR1 + " INTEGER, "
                + COLUMN_PUNTAJE_JUGADOR2 + " INTEGER, "
                + COLUMN_GANADOR + " TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            // Crear tabla para partidas multijugador si no existía
            db.execSQL("CREATE TABLE " + TABLE_PARTIDAS_MULTIJUGADOR + " ("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_JUGADOR1 + " TEXT, "
                    + COLUMN_JUGADOR2 + " TEXT, "
                    + COLUMN_PUNTAJE_JUGADOR1 + " INTEGER, "
                    + COLUMN_PUNTAJE_JUGADOR2 + " INTEGER, "
                    + COLUMN_GANADOR + " TEXT);");
        }
    }

    // Método para guardar una partida multijugador
    public void guardarPartidaMultijugador(String jugador1, String jugador2, int puntajeJugador1, int puntajeJugador2) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Determinar el ganador
        String ganador;
        if (puntajeJugador1 > puntajeJugador2) {
            ganador = jugador1;
        } else if (puntajeJugador1 < puntajeJugador2) {
            ganador = jugador2;
        } else {
            ganador = "Empate";
        }

        // Crear un registro
        ContentValues values = new ContentValues();
        values.put(COLUMN_JUGADOR1, jugador1);
        values.put(COLUMN_JUGADOR2, jugador2);
        values.put(COLUMN_PUNTAJE_JUGADOR1, puntajeJugador1);
        values.put(COLUMN_PUNTAJE_JUGADOR2, puntajeJugador2);
        values.put(COLUMN_GANADOR, ganador);

        // Insertar en la base de datos
        db.insert(TABLE_PARTIDAS_MULTIJUGADOR, null, values);
        db.close(); // Cerrar conexión para liberar recursos
    }
}
