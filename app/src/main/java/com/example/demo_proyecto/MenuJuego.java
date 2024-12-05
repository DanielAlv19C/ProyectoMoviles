package com.example.demo_proyecto;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MenuJuego extends AppCompatActivity {
    private ImageView widgetSolo;
    private ImageView widgetDos;
    private ImageView widgetTres;
    private ImageView widgetCuatro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_principal);

        // Inicializar los ImageViews si es necesario.
        widgetSolo = findViewById(R.id.widget_soloM);
        widgetDos = findViewById(R.id.widget_dosC);
        widgetTres = findViewById(R.id.widget_tresQ);
        widgetCuatro = findViewById(R.id.widget_cuatroS);
    }

    public void startMemorama(View view) {
        Toast.makeText(this, "Iniciar Memorama", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MenuJuego.this, MainMemorama.class);
        startActivity(intent); // Inicia la nueva actividad

    }

    public void startJuego2(View view) {

        Toast.makeText(this, "Iniciar Juego de dos jugadores", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MenuJuego.this, MainColores.class);
        startActivity(intent); // Inicia la nueva actividad


    }

    public void startJuego3(View view) {
        Toast.makeText(this, "Iniciar Quiz", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MenuJuego.this, MainQuiz.class);
        startActivity(intent); // Inicia la nueva actividad


    }

    public void startJuego4(View view) {
        Toast.makeText(this, "Iniciar Juego Sensor", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MenuJuego.this, SensorGameActivity.class);
        startActivity(intent); // Inicia la nueva actividad
    }
}