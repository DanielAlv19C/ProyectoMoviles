package com.example.demo_proyecto;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public class MainColores extends AppCompatActivity {
    private HashMap<Integer, Integer> imageToColorMap = new HashMap<>();
    private HashMap<Integer, Integer> colorToSoundMap = new HashMap<>();
    private MediaPlayer backgroundMusic;
    private MediaPlayer colorSoundPlayer;
    private MediaPlayer incorrectSoundPlayer;

    // Nuevas variables para el contador y timer
    private TextView scoreTextView;
    private TextView timerTextView;
    private int score = 0;
    private CountDownTimer gameTimer;
    private static final long GAME_DURATION = 60000; // 60 segundos
    private boolean gameActive = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_colores);

        // Inicializar las vistas del contador y timer
        scoreTextView = findViewById(R.id.score_text);
        timerTextView = findViewById(R.id.timer_text);

        // Inicializar el score
        updateScore(0);

        // Iniciar el timer
        startGameTimer();

        // Inicializar sistema de audio
        setupAudioSystem();

        // Referencias a los 4 contenedores
        FrameLayout contenedor1 = findViewById(R.id.contenedor1);
        FrameLayout contenedor2 = findViewById(R.id.contenedor2);
        FrameLayout contenedor3 = findViewById(R.id.contenedor3);
        FrameLayout contenedor4 = findViewById(R.id.contenedor4);

        // Arreglo de colores
        ArrayList<Integer> colores = new ArrayList<>();
        colores.add(Color.RED);
        colores.add(Color.GREEN);
        colores.add(Color.BLUE);
        colores.add(Color.YELLOW);

        // Mezclar el arreglo de colores
        Collections.shuffle(colores);

        // Asignar colores únicos a los contenedores
        contenedor1.setBackgroundColor(colores.get(0));
        contenedor2.setBackgroundColor(colores.get(1));
        contenedor3.setBackgroundColor(colores.get(2));
        contenedor4.setBackgroundColor(colores.get(3));

        // Configurar listeners de drag & drop para los contenedores
        setupDropZone(contenedor1);
        setupDropZone(contenedor2);
        setupDropZone(contenedor3);
        setupDropZone(contenedor4);

        setupGridLayout();
    }

    private void startGameTimer() {
        // Cancelar cualquier temporizador existente
        if (gameTimer != null) {
            gameTimer.cancel();
        }

        gameTimer = new CountDownTimer(GAME_DURATION, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int secondsLeft = (int) (millisUntilFinished / 1000);
                timerTextView.setText(String.format("Tiempo: %ds", secondsLeft));
            }

            @Override
            public void onFinish() {
                redirectToGameOverScreen(0);
            }
        }.start();
    }

    private void updateScore(int points) {
        score += points;
        scoreTextView.setText(String.format("Puntaje: %d", score));
    }

    private void disableGameInteractions() {
        GridLayout gridLayout = findViewById(R.id.grid_layout);
        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            View child = gridLayout.getChildAt(i);
            child.setOnLongClickListener(null);
        }
    }

    private void checkGameEnd() {
        GridLayout gridLayout = findViewById(R.id.grid_layout);
        if (gridLayout.getChildCount() == 0 || !gameActive) {
            int timeRemaining = Integer.parseInt(timerTextView.getText().toString().replaceAll("[^0-9]", ""));
            redirectToGameOverScreen(timeRemaining);
        }
    }

    private void redirectToGameOverScreen(int timeRemaining) {
        gameActive = false;
        if (gameTimer != null) {
            gameTimer.cancel();
        }

        Intent intent = new Intent(this, GameOverActivity.class);
        intent.putExtra("FINAL_SCORE", score);
        intent.putExtra("TIME_REMAINING", timeRemaining);
        startActivity(intent);
        finish();
    }

    private void setupAudioSystem() {
        // Mapear colores a sus sonidos
        colorToSoundMap.put(Color.RED, R.raw.sound_red);
        colorToSoundMap.put(Color.GREEN, R.raw.sound_green);
        colorToSoundMap.put(Color.BLUE, R.raw.sound_blue);
        colorToSoundMap.put(Color.YELLOW, R.raw.sound_yellow);

        // Inicializar música de fondo
        backgroundMusic = MediaPlayer.create(this, R.raw.background);
        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(0.5f, 0.5f);
        backgroundMusic.start();
    }

    private void playColorSound(int color) {
        try {
            // Liberar el reproductor anterior si existe
            if (colorSoundPlayer != null) {
                colorSoundPlayer.release();
            }

            // Reproducir el sonido del color
            Integer soundResource = colorToSoundMap.get(color);
            if (soundResource != null) {
                colorSoundPlayer = MediaPlayer.create(this, soundResource);
                colorSoundPlayer.setVolume(1.0f, 1.0f);
                colorSoundPlayer.start();

                colorSoundPlayer.setOnCompletionListener(mp -> {
                    mp.release();
                    colorSoundPlayer = null;
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void playIncorrectSound() {
        try {
            // Liberar el reproductor anterior si existe
            if (incorrectSoundPlayer != null) {
                incorrectSoundPlayer.release();
            }

            incorrectSoundPlayer = MediaPlayer.create(this, R.raw.incorrect);
            incorrectSoundPlayer.setVolume(1.0f, 1.0f);
            incorrectSoundPlayer.start();

            incorrectSoundPlayer.setOnCompletionListener(mp -> {
                mp.release();
                incorrectSoundPlayer = null;
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupGridLayout() {
        GridLayout gridLayout = findViewById(R.id.grid_layout);

        // Mapeo de colores a imágenes
        HashMap<Integer, ArrayList<Integer>> colorToImagesMap = new HashMap<>();
        colorToImagesMap.put(Color.RED, new ArrayList<>(Arrays.asList(
                R.drawable.rojo, R.drawable.rojo2, R.drawable.rojo3)));
        colorToImagesMap.put(Color.GREEN, new ArrayList<>(Arrays.asList(
                R.drawable.verde, R.drawable.verde2, R.drawable.verde3)));
        colorToImagesMap.put(Color.BLUE, new ArrayList<>(Arrays.asList(
                R.drawable.azul, R.drawable.azul2, R.drawable.azul3)));
        colorToImagesMap.put(Color.YELLOW, new ArrayList<>(Arrays.asList(
                R.drawable.amarillo, R.drawable.amarillo2, R.drawable.amarillo3)));

        ArrayList<Integer> selectedImages = new ArrayList<>();

        // Seleccionar 2 imágenes para cada color y mapear imágenes a colores
        for (Integer color : colorToImagesMap.keySet()) {
            ArrayList<Integer> imagesForColor = colorToImagesMap.get(color);
            Collections.shuffle(imagesForColor);
            selectedImages.add(imagesForColor.get(0));
            selectedImages.add(imagesForColor.get(1));

            // Mapear cada imagen a su color correspondiente
            for (Integer imageRes : imagesForColor) {
                imageToColorMap.put(imageRes, color);
            }
        }

        Collections.shuffle(selectedImages);

        // Agregar imágenes al GridLayout con funcionalidad de drag
        for (int imageRes : selectedImages) {
            ImageView imageView = createDraggableImageView(imageRes);
            gridLayout.addView(imageView);
        }
        gridLayout.getViewTreeObserver().addOnGlobalLayoutListener(() -> checkGameEnd());
    }

    private ImageView createDraggableImageView(int imageRes) {
        ImageView imageView = new ImageView(this);
        imageView.setImageResource(imageRes);
        imageView.setTag(imageRes);

        // Configurar escala y tamaño
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageView.setAdjustViewBounds(true);

        // Configuración del diseño
        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.width = 0;
        params.height = GridLayout.LayoutParams.WRAP_CONTENT;
        params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
        params.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
        params.setMargins(8, 8, 8, 8);
        imageView.setLayoutParams(params);

        // Configurar drag listener
        imageView.setOnLongClickListener(v -> {
            ClipData.Item item = new ClipData.Item((CharSequence) v.getTag().toString());
            ClipData dragData = new ClipData(
                    v.getTag().toString(),
                    new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN},
                    item
            );

            View.DragShadowBuilder myShadow = new View.DragShadowBuilder(imageView);
            v.startDragAndDrop(dragData, myShadow, v, 0);
            return true;
        });

        return imageView;
    }

    // El resto de los métodos permanecen igual, solo modificamos setupDropZone
    private void setupDropZone(FrameLayout container) {
        container.setOnDragListener((v, event) -> {
            if (!gameActive) return false;  // Si el juego terminó, no procesar más drops

            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    return event.getClipDescription()
                            .hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN);

                case DragEvent.ACTION_DRAG_ENTERED:
                case DragEvent.ACTION_DRAG_LOCATION:
                    return true;

                case DragEvent.ACTION_DROP:
                    View viewBeingDragged = (View) event.getLocalState();
                    int imageResource = (int) viewBeingDragged.getTag();
                    int imageColor = imageToColorMap.get(imageResource);
                    int containerColor = ((ColorDrawable) container.getBackground()).getColor();

                    if (imageColor == containerColor) {
                        // Remover la imagen del GridLayout
                        ((ViewGroup) viewBeingDragged.getParent()).removeView(viewBeingDragged);

                        // Ajustar la imagen al contenedor
                        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                                FrameLayout.LayoutParams.MATCH_PARENT,
                                FrameLayout.LayoutParams.MATCH_PARENT
                        );
                        params.setMargins(10, 10, 10, 10);
                        viewBeingDragged.setLayoutParams(params);

                        // Añadir la imagen al contenedor
                        container.addView(viewBeingDragged);

                        // Reproducir sonido de color correcto
                        playColorSound(containerColor);

                        // Sumar un punto
                        updateScore(1);

                        // Verificar si el juego debe terminar
                        checkGameEnd();

                        Toast.makeText(MainColores.this, "¡Correct color!", Toast.LENGTH_SHORT).show();
                        return true;
                    } else {
                        // Reproducir sonido de error
                        playIncorrectSound();

                        // Restar un punto
                        updateScore(-1);

                        Toast.makeText(MainColores.this, "Incorrect color", Toast.LENGTH_SHORT).show();
                        return false;
                    }

                case DragEvent.ACTION_DRAG_ENDED:
                    return true;

                default:
                    return false;
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (backgroundMusic != null && backgroundMusic.isPlaying()) {
            backgroundMusic.pause();
        }
        if (gameTimer != null) {
            gameTimer.cancel();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (backgroundMusic != null && !backgroundMusic.isPlaying()) {
            backgroundMusic.start();
        }
        // Opcional: reiniciar el timer si el juego está activo
        if (gameActive) {
            startGameTimer();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (backgroundMusic != null) {
            backgroundMusic.release();
            backgroundMusic = null;
        }
        if (colorSoundPlayer != null) {
            colorSoundPlayer.release();
            colorSoundPlayer = null;
        }
        if (incorrectSoundPlayer != null) {
            incorrectSoundPlayer.release();
            incorrectSoundPlayer = null;
        }
        if (gameTimer != null) {
            gameTimer.cancel();
        }
    }
}