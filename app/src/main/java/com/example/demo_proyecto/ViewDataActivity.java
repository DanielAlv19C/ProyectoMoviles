package com.example.demo_proyecto;

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
import androidx.room.Room;

import java.util.List;

    public class ViewDataActivity extends AppCompatActivity {

        private TableLayout tableLayout;
        private void agregarFila(String nombre, int puntaje, String tiempo) {
            TableRow row = new TableRow(this);

            // Parámetros de diseño uniforme para cada celda
            TableRow.LayoutParams params = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1f);

            // Crear las celdas con bordes visibles
            TextView tvNombre = crearCelda(nombre, params);
            TextView tvPuntaje = crearCelda(String.valueOf(puntaje), params);
            TextView tvTiempo = crearCelda(tiempo, params);

            // Agregar las celdas a la fila
            row.addView(tvNombre);
            row.addView(tvPuntaje);
            row.addView(tvTiempo);

            // Agregar la fila completa a la tabla
            tableLayout.addView(row);
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
            drawable.setColor(Color.parseColor("#9370DB"));
            drawable.setStroke(2, Color.BLACK); // Bordes negros de 2dp
            return drawable;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_view_data);
            findViewById(android.R.id.content).setBackgroundColor(Color.parseColor("#FFD700"));

            tableLayout = findViewById(R.id.tableLayout);

            // Agregar fila de encabezados
            TableRow headerRow = new TableRow(this);

            // Parámetros para las celdas de encabezado
            TableRow.LayoutParams params = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1f);

            TextView tvHeaderNombre = crearCelda("Nombre", params);
            TextView tvHeaderPuntaje = crearCelda("Puntaje", params);
            TextView tvHeaderTiempo = crearCelda("Tiempo", params);

            // Negrita y color de texto para los encabezados
            tvHeaderNombre.setTypeface(null, Typeface.BOLD);
            tvHeaderPuntaje.setTypeface(null, Typeface.BOLD);
            tvHeaderTiempo.setTypeface(null, Typeface.BOLD);
            tvHeaderNombre.setTextColor(Color.WHITE);
            tvHeaderPuntaje.setTextColor(Color.WHITE);
            tvHeaderTiempo.setTextColor(Color.WHITE);

            // Cambiar el color de fondo del encabezado
            tvHeaderNombre.setBackgroundColor(Color.parseColor("#3F51B5"));
            tvHeaderPuntaje.setBackgroundColor(Color.parseColor("#3F51B5"));
            tvHeaderTiempo.setBackgroundColor(Color.parseColor("#3F51B5"));

            // Agregar los encabezados a la fila
            headerRow.addView(tvHeaderNombre);
            headerRow.addView(tvHeaderPuntaje);
            headerRow.addView(tvHeaderTiempo);

            // Agregar la fila de encabezado a la tabla
            tableLayout.addView(headerRow);

            // Cargar los datos en un hilo secundario
            new Thread(() -> {
                DatabaseHelper dbHelper = new DatabaseHelper(this);
                List<ResultadosB> resultados = dbHelper.obtenerResultadosConObjetos();

                runOnUiThread(() -> {
                    for (ResultadosB resultado : resultados) {
                        agregarFila(resultado.getNombre(), resultado.getPuntaje(), resultado.getTiempo());
                    }
                });
            }).start();
        }


    }

