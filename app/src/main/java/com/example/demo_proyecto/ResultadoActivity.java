package com.example.demo_proyecto;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ResultadoActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    //private Button btnSalir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultado);

        TextView textoResultado = findViewById(R.id.textoResultado);
        TextView textoTitulo = findViewById(R.id.tituloJuego);
        Button btnSalir = findViewById(R.id.btnsalir);

        // Animaciones
        Animation slideDown = AnimationUtils.loadAnimation(this, R.anim.slide_down);
        textoResultado.startAnimation(slideDown);

        Animation bounce = AnimationUtils.loadAnimation(this, R.anim.bounce);
        textoTitulo.startAnimation(bounce);

        // Obtener puntaje
        Intent intent = getIntent();
        int puntaje = intent.getIntExtra("puntaje", 0);
        int totalPreguntas = intent.getIntExtra("totalPreguntas", 10);

        // Mostrar mensaje según el puntaje
        if (puntaje > 7) {
            textoTitulo.setText("¡Congratulation!");
            reproducirMusica(R.raw.victorysound);
        } else {
            textoTitulo.setText("Good Try");
            reproducirMusica(R.raw.musica_fondo);
        }

        // Mostrar puntaje
        String resultado = puntaje + " of " + totalPreguntas;
        textoResultado.setText(resultado);
        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Enviar broadcast para detener la música
                Intent detenerMusicaIntent = new Intent("com.miapp.DETENER_MUSICA");
                sendBroadcast(detenerMusicaIntent);

                // Navegar a la siguiente actividad
                Intent regresarMain = new Intent(ResultadoActivity.this, MenuJuego.class);
                startActivity(regresarMain);
                finish(); // Cierra la actividad actual para que no se quede en la pila de actividades
            }
        });


    }

    private void reproducirMusica(int recursoMusical) {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        mediaPlayer = MediaPlayer.create(this, recursoMusical);
        mediaPlayer.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}