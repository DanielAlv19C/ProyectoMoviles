package com.example.demo_proyecto;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.Result;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Memorama.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "Resultados";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NOMBRE = "nombre";
    private static final String COLUMN_PUNTAJE = "puntaje";
    private static final String COLUMN_TIEMPO = "tiempo";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NOMBRE + " TEXT, " +
                COLUMN_PUNTAJE + " INTEGER, " +
                COLUMN_TIEMPO + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    public List<ResultadosB> obtenerResultadosConObjetos() {
        List<ResultadosB> resultados = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        if (cursor != null && cursor.moveToFirst()) {
            // Obtiene los índices de las columnas
            int nombreIndex = cursor.getColumnIndex(COLUMN_NOMBRE);   // Índice de la columna 'nombre'
            int puntajeIndex = cursor.getColumnIndex(COLUMN_PUNTAJE); // Índice de la columna 'puntaje'
            int tiempoIndex = cursor.getColumnIndex(COLUMN_TIEMPO);   // Índice de la columna 'tiempo'

            // Verifica si los índices son válidos (diferentes de -1)
            if (nombreIndex == -1 || puntajeIndex == -1 || tiempoIndex == -1) {
                Log.e("Database Error", "Error: alguna de las columnas no fue encontrada en la base de datos.");
                cursor.close();
                return resultados; // Retorna una lista vacía
            }

            do {
                // Usa los índices para obtener los valores de las columnas
                String nombre = cursor.getString(nombreIndex);  // Obtiene el valor de 'nombre'
                int puntaje = cursor.getInt(puntajeIndex);     // Obtiene el valor de 'puntaje'
                String tiempo = cursor.getString(tiempoIndex); // Obtiene el valor de 'tiempo'

                // Crear un objeto ResultadosB con los valores obtenidos
                ResultadosB resultado = new ResultadosB(nombre, puntaje, tiempo);

                // Agregar el objeto a la lista
                resultados.add(resultado);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return resultados;
    }


    public boolean insertarResultado(String nombre, int puntaje, String tiempo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOMBRE, nombre);
        values.put(COLUMN_PUNTAJE, puntaje);
        values.put(COLUMN_TIEMPO, tiempo);

        long result = db.insert(TABLE_NAME, null, values);
        return result != -1;
    }


}

