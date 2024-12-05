package com.example.demo_proyecto;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SensorGameActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor accelerometer;

    private TextView questionText;
    private TextView answerLeft;
    private TextView answerRight;

    private boolean answerSelected = false;

    private int currentQuestionIndex = 0; // Índice para rastrear la pregunta actual

    // Banco de preguntas y respuestas
    private String[] questions = {
            "What is 'manzana' in English?",
            "What is 'banana' in English?",
            "What is 'uva' in English?"
    };

    private String[][] answers = {
            {"Apple", "Banana"},
            {"Banana", "Apple"},
            {"Grape", "Peach"}
    };

    private int[] correctAnswers = {0, 0, 0}; // Índices de las respuestas correctas

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_game);

        questionText = findViewById(R.id.questionText);
        answerLeft = findViewById(R.id.answerLeft);
        answerRight = findViewById(R.id.answerRight);

        // Inicializar el SensorManager y el acelerómetro
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        // Asegúrate de que el acelerómetro esté disponible
        if (accelerometer == null) {
            Toast.makeText(this, "Acelerómetro no disponible", Toast.LENGTH_SHORT).show();
        }

        // Cargar la primera pregunta
        cargarNuevaPregunta();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Registrar el sensor de acelerómetro para recibir actualizaciones
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Dejar de escuchar el sensor cuando la actividad no está activa
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            Log.d("SensorEvent", "Aceleración en X: " + x);

            if (!answerSelected && currentQuestionIndex < questions.length) {
                if (x < -5) {
                    Log.d("Respuesta", "Seleccionaste la opción izquierda");
                    answerSelected = true;
                    verificarRespuesta(0);
                } else if (x > 5) {
                    Log.d("Respuesta", "Seleccionaste la opción derecha");
                    answerSelected = true;
                    verificarRespuesta(1);
                }
            }
        }
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // No se necesita en este caso
    }



    private void verificarRespuesta(int selectedIndex) {
        if (currentQuestionIndex < questions.length) {
            if (selectedIndex == correctAnswers[currentQuestionIndex]) {
                Toast.makeText(this, "Respuesta correcta", Toast.LENGTH_SHORT).show();
                currentQuestionIndex++;
                if (currentQuestionIndex < questions.length) {
                    cargarNuevaPregunta();
                } else {
                    questionText.setText("Juego terminado!");
                    answerLeft.setText("");
                    answerRight.setText("");
                }
            } else {
                Toast.makeText(this, "Mala respuesta, intenta otra vez.", Toast.LENGTH_SHORT).show();
                answerSelected = false;
            }
        } else {
            Toast.makeText(this, "Sin preguntas", Toast.LENGTH_SHORT).show();
        }
    }

    private void cargarNuevaPregunta() {
        // Reiniciar el estado de selección
        answerSelected = false;

        // Verificar si hay más preguntas
        if (currentQuestionIndex < questions.length) {
            questionText.setText(questions[currentQuestionIndex]);
            answerLeft.setText(answers[currentQuestionIndex][0]);
            answerRight.setText(answers[currentQuestionIndex][1]);
        } else {
            // Si no hay más preguntas, mostrar un mensaje
            questionText.setText("Game Over!");
            answerLeft.setText("");
            answerRight.setText("");
        }
    }

}
