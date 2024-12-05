package com.example.demo_proyecto;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class RegistroMultijugador extends AppCompatActivity {

    private EditText editTextNamePlayer1;
    private EditText editTextNamePlayer2;
    private Button buttonStart;
    private Button buttonViewData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla_multijugador);


        editTextNamePlayer1 = findViewById(R.id.editJugador1);
        editTextNamePlayer2 = findViewById(R.id.editJugador2);
        buttonStart = findViewById(R.id.buttonStart);
        buttonViewData = findViewById(R.id.buttonViewData);


        buttonStart.setOnClickListener(v -> {
            String namePlayer1 = editTextNamePlayer1.getText().toString().trim();
            String namePlayer2 = editTextNamePlayer2.getText().toString().trim();

            if (namePlayer1.isEmpty()) {
                editTextNamePlayer1.setError("Ingresa el nombre del jugador 1");
                return;
            }

            if (namePlayer2.isEmpty()) {
                editTextNamePlayer2.setError("Ingresa el nombre del jugador 2");
                return;
            }

            // Enviar los nombres a la actividad del juego de dos jugadores
            Intent intent = new Intent(RegistroMultijugador.this, ActivityJugadores.class);
            intent.putExtra("PLAYER_1_NAME", namePlayer1);
            intent.putExtra("PLAYER_2_NAME", namePlayer2);
            startActivity(intent);
            finish(); // Finaliza esta actividad para evitar regresar con "atrás"
        });

        // Configurar botón para ver los datos almacenados
        buttonViewData.setOnClickListener(v -> {
            Intent intent = new Intent(RegistroMultijugador.this, PartidasJugadores.class);
            startActivity(intent);
        });
    }
}