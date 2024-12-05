package com.example.demo_proyecto;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainQuiz extends AppCompatActivity implements View.OnClickListener {

    TextView textoPregunta;
    TextView textoTotalPreguntas;
    Button respuestaA, respuestaB, respuestaC, respuestaD;
    Button btnEnviar;

    int puntaje = 0;
    int totalPreguntas = 10; // Limitar a 10 preguntas
    int indicePreguntaActual = 0;
    String respuestaSeleccionada = "";
    List<Integer> listaPreguntasAleatorias;
    ImageView questionImage;

    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_quiz);

        mediaPlayer = MediaPlayer.create(this, R.raw.musica_fondo);
        mediaPlayer.setLooping(true); // Repite la música en bucle
        mediaPlayer.start();

        IntentFilter filter = new IntentFilter("com.miapp.DETENER_MUSICA");
        registerReceiver(detenerMusicaReceiver, filter);

        textoTotalPreguntas = findViewById(R.id.total_question);
        textoPregunta = findViewById(R.id.preguntaText);
        respuestaA = findViewById(R.id.ans_a);
        respuestaB = findViewById(R.id.ans_b);
        respuestaC = findViewById(R.id.ans_c);
        respuestaD = findViewById(R.id.ans_d);
        btnEnviar = findViewById(R.id.btnEnviar);
        questionImage = findViewById(R.id.question_image);

        questionImage.getLayoutParams().height = 575;
        questionImage.setScaleType(ImageView.ScaleType.FIT_CENTER);

        respuestaA.setOnClickListener(this);
        respuestaB.setOnClickListener(this);
        respuestaC.setOnClickListener(this);
        respuestaD.setOnClickListener(this);
        btnEnviar.setOnClickListener(this);


        listaPreguntasAleatorias = new ArrayList<>();
        for (int i = 0; i < Preguntas.preguntas.length; i++) {
            listaPreguntasAleatorias.add(i);
        }
        Collections.shuffle(listaPreguntasAleatorias);
        listaPreguntasAleatorias = listaPreguntasAleatorias.subList(0, Math.min(totalPreguntas, listaPreguntasAleatorias.size()));

        cargarNuevaPregunta();
    }
    private BroadcastReceiver detenerMusicaReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (mediaPlayer != null) {
                mediaPlayer.stop(); // Detener la música
                mediaPlayer.release(); // Liberar los recursos
                mediaPlayer = null; // Nullificar el objeto mediaPlayer
            }
        }
    };
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Asegurarse de liberar el receptor del BroadcastReceiver
        unregisterReceiver(detenerMusicaReceiver);

        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private void cargarNuevaPregunta() {
        if (indicePreguntaActual == totalPreguntas) {
            finalizarQuiz();
            return;
        }

        // Obtén el índice aleatorio de la pregunta actual
        int preguntaAleatoria = listaPreguntasAleatorias.get(indicePreguntaActual);

        // Actualiza el texto de la pregunta y las opciones
        textoTotalPreguntas.setText("Pregunta: " + (indicePreguntaActual + 1) + " de " + totalPreguntas);
        textoPregunta.setText(Preguntas.preguntas[preguntaAleatoria]);
        respuestaA.setText(Preguntas.opciones[preguntaAleatoria][0]);
        respuestaB.setText(Preguntas.opciones[preguntaAleatoria][1]);
        respuestaC.setText(Preguntas.opciones[preguntaAleatoria][2]);
        respuestaD.setText(Preguntas.opciones[preguntaAleatoria][3]);

        questionImage.setImageResource(Preguntas.imagenes[preguntaAleatoria]);

        // Resetea la respuesta seleccionada
        respuestaSeleccionada = "";
    }

    private void finalizarQuiz() {
        Intent intent = new Intent(MainQuiz.this, ResultadoActivity.class);
        intent.putExtra("puntaje", puntaje);
        intent.putExtra("totalPreguntas", totalPreguntas);
        startActivity(intent);
        finish(); // Finaliza MainActivity para evitar volver atrás
    }


    private void reiniciarQuiz() {
        puntaje = 0;
        indicePreguntaActual = 0;

        // Mezcla las preguntas y limita nuevamente a 10
        Collections.shuffle(listaPreguntasAleatorias);
        listaPreguntasAleatorias = listaPreguntasAleatorias.subList(0, Math.min(totalPreguntas, Preguntas.preguntas.length));
        cargarNuevaPregunta();
    }

    @Override
    public void onClick(View view) {
        // Restablecer el fondo de las opciones a blanco
        respuestaA.setBackgroundColor(Color.WHITE);
        respuestaB.setBackgroundColor(Color.WHITE);
        respuestaC.setBackgroundColor(Color.WHITE);
        respuestaD.setBackgroundColor(Color.WHITE);

        Button botonClicado = (Button) view;

        if (botonClicado.getId() == R.id.btnEnviar) {
            if (respuestaSeleccionada.isEmpty()) {
                // Si no se seleccionó una respuesta, muestra un mensaje de advertencia
                new AlertDialog.Builder(this)
                        .setTitle("Warning")
                        .setMessage("Please Select a Question!!!")
                        .setPositiveButton("OK", null)
                        .show();
            } else {
                // Obtén la pregunta actual y verifica si la respuesta es correcta
                int preguntaAleatoria = listaPreguntasAleatorias.get(indicePreguntaActual);
                if (respuestaSeleccionada.equals(Preguntas.respuestasCorrectas[preguntaAleatoria])) {
                    puntaje++;
                } else {
                    botonClicado.setBackgroundColor(Color.MAGENTA);
                }
                indicePreguntaActual++;
                cargarNuevaPregunta();
            }
        } else {
            // Marca la opción seleccionada en amarillo y guarda la respuesta seleccionada
            respuestaSeleccionada = botonClicado.getText().toString();
            botonClicado.setBackgroundColor(Color.YELLOW);
        }
    }

}