package com.example.demo_proyecto;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PartidasJugadores extends AppCompatActivity {

    private TableLayout tableLayout;
    private MemoramaDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partidas_jugadores);

        tableLayout = findViewById(R.id.tableLayoutDos);
        dbHelper = new MemoramaDatabaseHelper(this);

        // Agregar encabezados a la tabla con estilos
        addTableHeader();

        // Cargar datos de la base de datos y mostrarlos en la tabla
        loadMultiplayerData();
    }

    private void addTableHeader() {
        TableRow headerRow = new TableRow(this);

        // Parámetros para las celdas de encabezado
        TableRow.LayoutParams params = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1f);

        // Crear encabezados con estilo
        TextView headerJugador1 = crearCelda("Jugador 1", params);
        TextView headerJugador2 = crearCelda("Jugador 2", params);
        TextView headerPuntaje1 = crearCelda("Puntaje J1", params);
        TextView headerPuntaje2 = crearCelda("Puntaje J2", params);
        TextView headerGanador = crearCelda("Ganador", params);

        // Negrita para encabezados
        headerJugador1.setTypeface(Typeface.DEFAULT_BOLD);
        headerJugador2.setTypeface(Typeface.DEFAULT_BOLD);
        headerPuntaje1.setTypeface(Typeface.DEFAULT_BOLD);
        headerPuntaje2.setTypeface(Typeface.DEFAULT_BOLD);
        headerGanador.setTypeface(Typeface.DEFAULT_BOLD);

        headerJugador1.setBackgroundColor(Color.parseColor("#3F51B5"));
        headerJugador2.setBackgroundColor(Color.parseColor("#3F51B5"));
        headerPuntaje1.setBackgroundColor(Color.parseColor("#3F51B5"));
        headerPuntaje2.setBackgroundColor(Color.parseColor("#3F51B5"));
        headerGanador.setBackgroundColor(Color.parseColor("#3F51B5"));

        headerGanador.setTextColor(Color.WHITE);
        headerPuntaje1.setTextColor(Color.WHITE);
        headerPuntaje2.setTextColor(Color.WHITE);
        headerJugador1.setTextColor(Color.WHITE);
        headerJugador2.setTextColor(Color.WHITE);


        // Agregar encabezados a la fila
        headerRow.addView(headerJugador1);
        headerRow.addView(headerJugador2);
        headerRow.addView(headerPuntaje1);
        headerRow.addView(headerPuntaje2);
        headerRow.addView(headerGanador);

        // Agregar fila de encabezado a la tabla
        tableLayout.addView(headerRow);
    }

    private void loadMultiplayerData() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Consultar los datos de la tabla
        Cursor cursor = db.query(
                "partidas_multijugador",
                new String[]{"jugador1", "jugador2", "puntaje_jugador1", "puntaje_jugador2", "ganador"},
                null, null, null, null, null
        );

        TableRow.LayoutParams params = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1f);

        // Recorrer los resultados y agregarlos a la tabla
        while (cursor.moveToNext()) {
            String jugador1 = cursor.getString(cursor.getColumnIndexOrThrow("jugador1"));
            String jugador2 = cursor.getString(cursor.getColumnIndexOrThrow("jugador2"));
            int puntajeJugador1 = cursor.getInt(cursor.getColumnIndexOrThrow("puntaje_jugador1"));
            int puntajeJugador2 = cursor.getInt(cursor.getColumnIndexOrThrow("puntaje_jugador2"));
            String ganador = cursor.getString(cursor.getColumnIndexOrThrow("ganador"));

            TableRow dataRow = new TableRow(this);

            // Crear celdas con estilos
            TextView textJugador1 = crearCelda(jugador1, params);
            TextView textJugador2 = crearCelda(jugador2, params);
            TextView textPuntaje1 = crearCelda(String.valueOf(puntajeJugador1), params);
            TextView textPuntaje2 = crearCelda(String.valueOf(puntajeJugador2), params);
            TextView textGanador = crearCelda(ganador, params);

            // Agregar celdas a la fila
            dataRow.addView(textJugador1);
            dataRow.addView(textJugador2);
            dataRow.addView(textPuntaje1);
            dataRow.addView(textPuntaje2);
            dataRow.addView(textGanador);

            // Agregar fila a la tabla
            tableLayout.addView(dataRow);
        }

        cursor.close();
        db.close();
    }

    private TextView crearCelda(String texto, TableRow.LayoutParams params) {
        TextView textView = new TextView(this);
        textView.setText(texto);
        textView.setPadding(10, 10, 10, 10);
        textView.setTextColor(Color.parseColor("white")); // Color del texto
        textView.setBackground(crearFondoConBordes()); // Fondo con bordes
        textView.setLayoutParams(params);
        textView.setGravity(Gravity.CENTER); // Centrar texto
        return textView;
    }

    private GradientDrawable crearFondoConBordes() {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(Color.parseColor("#9370DB")); // Color de fondo amarillo
        drawable.setStroke(2, Color.BLACK); // Bordes negros de 2dp
        return drawable;
    }
}
