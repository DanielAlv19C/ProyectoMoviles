package com.example.demo_proyecto;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ActivityJugadores extends AppCompatActivity {
    private MemoramaMulti memoramaTablero;
    private String namePlayer1, namePlayer2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dos_jugadores);
        // Obtener nombres desde el Intent
        Intent intent = getIntent();
        namePlayer1 = intent.getStringExtra("PLAYER_1_NAME");
        namePlayer2 = intent.getStringExtra("PLAYER_2_NAME");

        Log.d("ActivityJugadores", "Jugador 1: " + namePlayer1);
        Log.d("ActivityJugadores", "Jugador 2: " + namePlayer2);


        //memoramaTablero = findViewById(R.id.memoramaDos);
        //memoramaTablero.setNombresJugadores(namePlayer1, namePlayer2);
        if (namePlayer1 != null && namePlayer2 != null) {
            MemoramaMulti memoramaTablero = findViewById(R.id.memoramaDos);
            memoramaTablero.setNombresJugadores(namePlayer1, namePlayer2);
        } else {
            Log.e("ActivityJugadores", "Error: Nombres son nulos");
        }

    }
}

