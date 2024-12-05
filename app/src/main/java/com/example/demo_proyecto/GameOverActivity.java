package com.example.demo_proyecto;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class GameOverActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        TextView finalScoreTextView = findViewById(R.id.final_score);
        TextView timeRemainingTextView = findViewById(R.id.time_remaining);
        Button goHomeButton = findViewById(R.id.btn_go_home);
        Button playAgainButton = findViewById(R.id.btn_play_again);

        // Obtener datos del Intent
        int finalScore = getIntent().getIntExtra("FINAL_SCORE", 0);
        int timeRemaining = getIntent().getIntExtra("TIME_REMAINING", 0);

        finalScoreTextView.setText(String.format("Puntaje Final: %d", finalScore));
        timeRemainingTextView.setText(String.format("Tiempo Restante: %ds", timeRemaining));

        // Botón para regresar al inicio
        goHomeButton.setOnClickListener(v -> {
            Intent homeIntent = new Intent(GameOverActivity.this, MenuJuego.class);
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(homeIntent);
            finish();
        });

        // Botón para jugar de nuevo
        playAgainButton.setOnClickListener(v -> {
            Intent playAgainIntent = new Intent(GameOverActivity.this, MenuJuego.class);
            startActivity(playAgainIntent);
            finish();
        });
    }
}