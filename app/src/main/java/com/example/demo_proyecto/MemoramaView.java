package com.example.demo_proyecto;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

import android.app.AlertDialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.airbnb.lottie.LottieAnimationView;

import java.util.Random;

public class MemoramaView extends GridLayout {
    private ImageView[] cartas;
    private int[] imagenesCartas;
    private boolean[] cartaDescubierta;
    private int primeraCarta = -1;
    private int segundaCarta = -1;
    private boolean esperando = false;
    private int puntaje = 0;
    private TextView tvPuntaje;
    private TextView tvTiempo;
    private Button btnReiniciar;
    private Button btnGuardar;
    private Handler timerHandler;
    private Runnable timerRunnable;
    private int tiempoTranscurrido = 0; // En segundos

    public MemoramaView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setBackgroundColor(ContextCompat.getColor(context, R.color.colorFondoLayout));
        init(context);
    }

    public MemoramaView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        setColumnCount(3);
        setRowCount(9);
        setUseDefaultMargins(true);
        setBackgroundColor(ContextCompat.getColor(context, R.color.colorFondoCartas));

        imagenesCartas = new int[]{
                R.drawable.leon, R.drawable.leon,
                R.drawable.perro, R.drawable.perro,
                R.drawable.jirafa, R.drawable.jirafa,
                R.drawable.gato, R.drawable.gato,
                R.drawable.conejo, R.drawable.conejo,
                R.drawable.puerco, R.drawable.puerco,
                R.drawable.elefante, R.drawable.elefante,
                R.drawable.raton, R.drawable.raton,
                R.drawable.pajaro, R.drawable.pajaro
        };

        shuffleArray(imagenesCartas);

        // Texto para mostrar el puntaje
        tvPuntaje = new TextView(context);
        tvPuntaje.setText("Puntaje: 0");
        tvPuntaje.setTextSize(20);
        tvPuntaje.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
        tvPuntaje.setTypeface(Typeface.create("sans-serif", Typeface.BOLD));

        // Texto para mostrar el tiempo
        tvTiempo = new TextView(context);
        tvTiempo.setText("Tiempo: 0s");
        tvTiempo.setTextSize(20);
        tvTiempo.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
        tvTiempo.setTypeface(Typeface.create("sans-serif", Typeface.BOLD));

        // Botón de reinicio
        btnReiniciar = new Button(context);
        btnReiniciar.setText("Reiniciar");
        btnReiniciar.setTextSize(16);
        btnReiniciar.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
        btnReiniciar.setTextColor(ContextCompat.getColor(context, R.color.white));
        btnReiniciar.setOnClickListener(v -> reiniciarJuego());

        cartas = new ImageView[imagenesCartas.length];
        cartaDescubierta = new boolean[imagenesCartas.length];
        crearCartas(context);

        // LayoutParams para el puntaje
        GridLayout.LayoutParams paramsPuntaje = new GridLayout.LayoutParams();
        paramsPuntaje.rowSpec = GridLayout.spec(6);
        paramsPuntaje.columnSpec = GridLayout.spec(0, 3);
        tvPuntaje.setLayoutParams(paramsPuntaje);

        // LayoutParams para el tiempo
        GridLayout.LayoutParams paramsTiempo = new GridLayout.LayoutParams();
        paramsTiempo.rowSpec = GridLayout.spec(7);
        paramsTiempo.columnSpec = GridLayout.spec(0, 3);
        tvTiempo.setLayoutParams(paramsTiempo);

        // LayoutParams para el botón de reinicio
        GridLayout.LayoutParams paramsReiniciar = new GridLayout.LayoutParams();
        paramsReiniciar.rowSpec = GridLayout.spec(8);
        paramsReiniciar.columnSpec = GridLayout.spec(0, 3);
        btnReiniciar.setLayoutParams(paramsReiniciar);

        // Botón para guardar datos
        btnGuardar = new Button(context);
        btnGuardar.setText("Guardar");
        btnGuardar.setTextSize(16);
        btnGuardar.setBackgroundColor(ContextCompat.getColor(context, R.color.colorAccent));
        btnGuardar.setTextColor(ContextCompat.getColor(context, R.color.white));
        btnGuardar.setEnabled(false); // Inicialmente desactivado
        btnGuardar.setOnClickListener(v -> guardarDatos());

        GridLayout.LayoutParams paramsGuardar = new GridLayout.LayoutParams();
        paramsGuardar.rowSpec = GridLayout.spec(8);
        paramsGuardar.columnSpec = GridLayout.spec(1, 2); // Colocado junto al botón de reinicio
        btnGuardar.setLayoutParams(paramsGuardar);



        addView(tvPuntaje);
        addView(tvTiempo);
        addView(btnReiniciar);
        addView(btnGuardar);

        // Inicializa el temporizador
        iniciarTemporizador();
        LottieAnimationView animacionFelicitacion = new LottieAnimationView(getContext());
        animacionFelicitacion.setAnimation(R.raw.confetti); // Asegúrate de tener el archivo de animación en la carpeta 'raw'
        animacionFelicitacion.setVisibility(View.GONE);  // Inicialmente oculto
        animacionFelicitacion.setLayoutParams(new GridLayout.LayoutParams(
                GridLayout.spec(0, 3), // La animación ocupa toda la fila de cartas
                GridLayout.spec(0, 3)  // La animación ocupa toda la columna de cartas
        ));

        // Agregar la animación al GridLayout
        addView(animacionFelicitacion);
    }

    private void crearCartas(Context context) {
        int margin = getResources().getDimensionPixelSize(R.dimen.card_margin); // Define el margen desde dimens.xml

        for (int i = 0; i < cartas.length; i++) {
            ImageView carta = new ImageView(getContext());
            carta.setImageResource(R.drawable.reversocart);
            carta.setBackground(ContextCompat.getDrawable(context, R.drawable.border_carta)); // Borde de la carta

            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 0; // Distribuir equitativamente el ancho
            params.height = getResources().getDimensionPixelSize(R.dimen.card_size);
            params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f); // Peso de distribución
            params.rowSpec = GridLayout.spec(GridLayout.UNDEFINED);
            params.setMargins(margin, margin, margin, margin); // Margen en cada lado de la carta

            carta.setLayoutParams(params);

            final int index = i;
            carta.setOnClickListener(v -> descubrirCarta(carta, index));

            cartas[i] = carta;
            addView(carta);
        }
    }

    private void shuffleArray(int[] array) {
        Random rand = new Random();
        for (int i = array.length - 1; i > 0; i--) {
            int j = rand.nextInt(i + 1);
            int temp = array[i];
            array[i] = array[j];
            array[j] = temp;
        }
    }

    public void reiniciarJuego() {
        shuffleArray(imagenesCartas);
        primeraCarta = -1;
        segundaCarta = -1;
        esperando = false;
        puntaje = 0;
        tiempoTranscurrido = 0;
        tvPuntaje.setText("Puntaje: " + puntaje);
        tvTiempo.setText("Tiempo: " + tiempoTranscurrido + "s");

        for (int i = 0; i < cartaDescubierta.length; i++) {
            cartaDescubierta[i] = false;
            cartas[i].setImageResource(R.drawable.reversocart);
        }

        iniciarTemporizador();
    }

    private void iniciarTemporizador() {
        if (timerHandler != null) {
            timerHandler.removeCallbacks(timerRunnable);
        }

        timerHandler = new Handler(Looper.getMainLooper());
        timerRunnable = new Runnable() {
            @Override
            public void run() {
                tiempoTranscurrido++;
                tvTiempo.setText("Tiempo: " + tiempoTranscurrido + "s");
                timerHandler.postDelayed(this, 1000);
            }
        };
        timerHandler.post(timerRunnable);
    }

    private void descubrirCarta(ImageView carta, int index) {
        if (esperando || cartaDescubierta[index]) return;

        carta.setImageResource(imagenesCartas[index]);
        cartaDescubierta[index] = true;

        if (primeraCarta == -1) {
            primeraCarta = index;
        } else {
            segundaCarta = index;
            esperando = true;

            new Handler(Looper.getMainLooper()).postDelayed(() -> verificarPareja(), 1000);
        }
    }

    private void verificarPareja() {
        Context context = getContext();

        if (imagenesCartas[primeraCarta] != imagenesCartas[segundaCarta]) {
            reproducirSonido(context, R.raw.error2);
            cartas[primeraCarta].setImageResource(R.drawable.reversocart);
            cartas[segundaCarta].setImageResource(R.drawable.reversocart);
            cartaDescubierta[primeraCarta] = false;
            cartaDescubierta[segundaCarta] = false;

            // Resta puntaje en caso de error
            puntaje--;
        } else {
            puntaje++;
            String animal = getAnimalName(imagenesCartas[primeraCarta]);
            reproducirSonido(context, context.getResources().getIdentifier(animal, "raw", context.getPackageName()));
            mostrarAlerta(context, "¡Encontraste al " + animal + "!");
        }

        tvPuntaje.setText("Puntaje: " + puntaje);
        primeraCarta = -1;
        segundaCarta = -1;
        esperando = false;
        // Verificar si todas las cartas han sido descubiertas y el juego ha terminado
        if (juegoCompletado()) {
            new Handler(Looper.getMainLooper()).postDelayed(this::finalizarJuego, 3000);
            //finalizarJuego();
        }
    }
    // Método para verificar si todas las cartas han sido descubiertas
    private boolean juegoCompletado() {
        for (boolean descubierta : cartaDescubierta) {
            if (!descubierta) {
                return false;
            }
        }
        return true;
    }

    // Método para finalizar el juego
    private void finalizarJuego() {
        // Detener el temporizador
        if (timerHandler != null) {
            timerHandler.removeCallbacks(timerRunnable);
        }
        // Activar el botón de guardar
        btnGuardar.setEnabled(true);


        // Reproducir sonido de felicitación
        reproducirSonido(getContext(), R.raw.felicitaciones);
        for (ImageView carta : cartas) {
            carta.setVisibility(View.GONE); // Oculta todas las cartas
        }


        // Mostrar animación de felicitación
        mostrarAnimacionFelicitacion();
    }


    private void mostrarAnimacionFelicitacion() {
        Log.d("Animacion", "Intentando mostrar la animación de felicitación");

        // Buscar el LottieAnimationView directamente
        LottieAnimationView animacion = (LottieAnimationView) getChildAt(getChildCount() - 1); // devuellve el último hijo agregado

        if (animacion == null) {
            Log.e("Animacion", "LottieAnimationView no encontrado en el layout");
            return;
        }

        animacion.setVisibility(View.VISIBLE);  // Hacer visible la animación
        animacion.playAnimation();

        // Ocultar la animación después de 3 segundos
        // Ocultar la animación después de 3 segundos
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Log.d("Animacion", "Ocultando la animación de felicitación");
            animacion.setVisibility(View.GONE);

            // Mostrar nuevamente todas las cartas después de la animación
            for (ImageView carta : cartas) {
                carta.setVisibility(View.VISIBLE); // Mostrar todas las cartas de nuevo
            }
        }, 3000);
    }
    private void guardarDatos() {
        Context context = getContext();
        DatabaseHelper dbHelper = new DatabaseHelper(context);

        // Obtener nombre, puntaje y tiempo
        String playerName = ((MainActivity) context).getIntent().getStringExtra("PLAYER_NAME");
        String tiempoFinal = tvTiempo.getText().toString().replace("Tiempo: ", "");

        // Guardar en la base de datos
        boolean resultado = dbHelper.insertarResultado(playerName, puntaje, tiempoFinal);
        if (resultado) {
            Toast.makeText(context, "Datos guardados correctamente", Toast.LENGTH_SHORT).show();
            btnGuardar.setEnabled(false); // Desactivar el botón después de guardar

            // Redirigir al menú principal
            Intent intent = new Intent(context, RegistroNombreM.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);

            // Finalizar la actividad actual
            if (context instanceof Activity) {
                ((Activity) context).finish();
            }
        } else {
            Toast.makeText(context, "Error al guardar los datos", Toast.LENGTH_SHORT).show();
        }
    }






    private void reproducirSonido(Context context, int sonidoResId) {
        MediaPlayer mediaPlayer = MediaPlayer.create(context, sonidoResId);
        mediaPlayer.setOnCompletionListener(MediaPlayer::release);
        mediaPlayer.start();
    }

    private String getAnimalName(int drawableId) {
        if (drawableId == R.drawable.leon) {
            return "lion";
        } else if (drawableId == R.drawable.perro) {
            return "dog";
        } else if (drawableId == R.drawable.jirafa) {
            return "giraffe";
        } else if (drawableId == R.drawable.gato) {
            return "cat";
        } else if (drawableId == R.drawable.conejo) {
            return "rabbit";
        } else if (drawableId == R.drawable.puerco) {
            return "pig";
        } else if (drawableId == R.drawable.elefante) {
            return "elephant";
        } else if (drawableId == R.drawable.raton) {
            return "mouse";
        } else if (drawableId == R.drawable.pajaro) {
            return "bird";
        } else {
            return "";
        }
    }

    private void mostrarAlerta(Context context, String mensaje) {
        new AlertDialog.Builder(context)
                .setTitle("Felicidades")
                .setMessage(mensaje)
                .setPositiveButton("OK", null)
                .show();
    }
}
