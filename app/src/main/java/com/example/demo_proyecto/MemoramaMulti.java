package com.example.demo_proyecto;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;
import android.os.Looper;
import android.app.Activity;

import androidx.core.content.ContextCompat;

import com.airbnb.lottie.LottieAnimationView;

import java.util.Random;

public class MemoramaMulti extends GridLayout {
    private ImageView[] cartas;
    private int[] imagenesCartas;
    private boolean[] cartaDescubierta;
    private int primeraCarta = -1;
    private int segundaCarta = -1;
    private boolean esperando = false;
    private TextView turnoTextView; // El TextView que indica el turno del jugador
    private TextView puntajeJugador1; // TextView para puntaje del jugador 1
    private TextView puntajeJugador2; // TextView para puntaje del jugador 2
    private int puntosJugador1 = 0; // Puntos del jugador 1
    private int puntosJugador2 = 0; // Puntos del jugador 2
    private String ganadorMensaje;
    private String jugador1Nombre;
    private String jugador2Nombre;

    public MemoramaMulti(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MemoramaMulti(Context context) {
        super(context);
        init(context);
    }
    public void setNombresJugadores(String jugador1, String jugador2) {
        this.jugador1Nombre = jugador1;
        this.jugador2Nombre = jugador2;

        if (turnoTextView != null) {
            turnoTextView.setText("Turno: " + jugador1Nombre);
        }

        // Actualiza el texto del TextView de puntajes
        if (puntajeJugador1 != null) {
            puntajeJugador1.setText(jugador1Nombre + ": 0");
        }
        if (puntajeJugador2 != null) {
            puntajeJugador2.setText(jugador2Nombre + ": 0");
        }

        // Verifica que se asignen correctamente
        Log.d("MemoramaMulti", "Jugador 1 en memoria: " + jugador1Nombre);
        Log.d("MemoramaMulti", "Jugador 2 en memoria: " + jugador2Nombre);
    }

    private void init(Context context) {
        setColumnCount(3); // 3 columnas
        setRowCount(9);
        setUseDefaultMargins(true);
        setBackgroundColor(ContextCompat.getColor(context, R.color.colorFondoCartas));

        // Cambiar a imágenes de frutas
        imagenesCartas = new int[] {
                R.drawable.manzana, R.drawable.manzana,
                R.drawable.fresa, R.drawable.fresa,
                R.drawable.sandia, R.drawable.sandia,
                R.drawable.platano, R.drawable.platano,
                R.drawable.mango, R.drawable.mango,
                R.drawable.uvas, R.drawable.uvas,
                R.drawable.pera, R.drawable.pera,
                R.drawable.naranja, R.drawable.naranja,
                R.drawable.pina, R.drawable.pina
        };

        shuffleArray(imagenesCartas); // Mezcla las cartas
        crearCartas(context); // Crear y agregar las cartas
        crearTurnoTextView(context); // Crear el TextView para indicar el turno
        crearPuntajes(context);

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
        int margin = getResources().getDimensionPixelSize(R.dimen.card_margin); // Margen de cada carta
        cartas = new ImageView[imagenesCartas.length];
        cartaDescubierta = new boolean[imagenesCartas.length];

        for (int i = 0; i < cartas.length; i++) {
            ImageView carta = new ImageView(context);
            carta.setImageResource(R.drawable.reversocart); // Imagen de reverso
            carta.setBackground(ContextCompat.getDrawable(context, R.drawable.border_carta)); // Borde de la carta

            // Configuración de layout para las cartas
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 0; // Distribuir equitativamente el ancho
            params.height = getResources().getDimensionPixelSize(R.dimen.card_size);
            params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f); // Peso para distribuir
            params.rowSpec = GridLayout.spec(GridLayout.UNDEFINED);
            params.setMargins(margin, margin, margin, margin); // Margen de cada carta

            carta.setLayoutParams(params);

            final int index = i;
            carta.setOnClickListener(v -> descubrirCarta(carta, index));

            cartas[i] = carta;
            addView(carta);
        }
    }

    // Método para crear el TextView que muestra el turno
    private void crearTurnoTextView(Context context) {
        turnoTextView = new TextView(context);
        //turnoTextView.setText("Turno: Jugador 1");  El primer turno es para el jugador 1
        turnoTextView.setText("Turno: " + (jugador1Nombre != null ? jugador1Nombre : "Jugador 1"));
        turnoTextView.setTextSize(18);
        turnoTextView.setTextColor(Color.BLACK);  // Color de texto negro
        turnoTextView.setTypeface(null, Typeface.BOLD_ITALIC);  // Texto en negrita y cursiva
        turnoTextView.setBackgroundColor(Color.parseColor("#8BC34A"));  // Fondo verde claro
        turnoTextView.setPadding(7, 7, 7, 7);
        turnoTextView.setGravity(Gravity.CENTER);

        // Configurar GridLayout para el TextView que indica el turno
        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.rowSpec = GridLayout.spec(6); // Fila 7 (contando desde 0)
        params.columnSpec = GridLayout.spec(0, 3); // Combina las 3 columnas
        params.setMargins(0, 14, 0, 14); // Margen arriba y abajo

        turnoTextView.setLayoutParams(params);
        addView(turnoTextView); // Agregar el TextView al GridLayout
    }
    private void crearPuntajes(Context context) {
        // Crear el TextView para el puntaje de Jugador 1
        puntajeJugador1 = new TextView(context);
        //puntajeJugador1.setText("Jug 1: 0");
        puntajeJugador1.setText((jugador1Nombre != null ? jugador1Nombre : "Jugador 1") + ": 0");
        puntajeJugador1.setTextSize(18);
        puntajeJugador1.setTextColor(Color.WHITE);
        puntajeJugador1.setTypeface(null, Typeface.BOLD);
        puntajeJugador1.setBackgroundColor(Color.parseColor("#FF5722"));  // Color de fondo (un naranja atractivo)
        puntajeJugador1.setPadding(7, 7, 7, 7);
        //puntajeJugador1.setGravity(Gravity.CENTER);

        // Crear el TextView para el puntaje de Jugador 2
        puntajeJugador2 = new TextView(context);
        //puntajeJugador2.setText("Jug 2: 0");
        puntajeJugador2.setText((jugador2Nombre != null ? jugador2Nombre : "Jugador 2") + ": 0");
        puntajeJugador2.setTextSize(18);
        puntajeJugador2.setTextColor(Color.WHITE);  // Color de texto blanco
        puntajeJugador2.setTypeface(null, Typeface.BOLD);  // Texto en negrita
        puntajeJugador2.setBackgroundColor(Color.parseColor("#2196F3"));  // Color de fondo (un azul brillante)
        puntajeJugador2.setPadding(7, 7, 7, 7);
        //puntajeJugador2.setGravity(Gravity.CENTER);

        // Configurar GridLayout para los TextViews de puntaje (Fila 8)
        GridLayout.LayoutParams params1 = new GridLayout.LayoutParams();
        params1.rowSpec = GridLayout.spec(7); // Fila 8
        params1.columnSpec = GridLayout.spec(0, 3); // Columnas 1 a 3 (span de 3 columnas)
        params1.setGravity(Gravity.CENTER); // Centrado de contenido
        puntajeJugador1.setLayoutParams(params1);
        addView(puntajeJugador1); // Agregar puntaje del Jugador 1

        // Configurar GridLayout para los TextViews de puntaje (Fila 9)
        GridLayout.LayoutParams params2 = new GridLayout.LayoutParams();
        params2.rowSpec = GridLayout.spec(8); // Fila 9
        params2.columnSpec = GridLayout.spec(0, 3); // Columnas 1 a 3 (span de 3 columnas)
        params2.setGravity(Gravity.CENTER); // Centrado de contenido
        puntajeJugador2.setLayoutParams(params2);
        addView(puntajeJugador2); // Agregar puntaje del Jugador 2
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

    private void descubrirCarta(ImageView carta, int index) {
        if (esperando || cartaDescubierta[index]) return;

        carta.setImageResource(imagenesCartas[index]); // Muestra la imagen
        cartaDescubierta[index] = true;

        if (primeraCarta == -1) {
            primeraCarta = index;
        } else {
            segundaCarta = index;
            esperando = true;

            // Verifica la pareja después de 1 segundo
            new Handler(Looper.getMainLooper()).postDelayed(this::verificarPareja, 1000);
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
            if (turnoTextView.getText().toString().equals("Turno J1:"+jugador1Nombre)) {
                cambiarTurno(2); // Cambia a Jugador 2
            } else {
                cambiarTurno(1); // Cambia a Jugador 1
            }
        } else {
            String fruta = getFrutaName(imagenesCartas[primeraCarta]);
            reproducirSonido(context, context.getResources().getIdentifier(fruta, "raw", context.getPackageName()));
            mostrarAlerta(context, "¡Encontraste la " + fruta + "!");
            if (turnoTextView.getText().toString().equals("Turno J1:"+jugador1Nombre)) {
                puntosJugador1++;
                puntajeJugador1.setText(jugador1Nombre +" : " + puntosJugador1);
            } else {
                puntosJugador2++;
                puntajeJugador2.setText(jugador2Nombre +" : "  + puntosJugador2);
            }
        }


        primeraCarta = -1;
        segundaCarta = -1;
        esperando = false;
        if (juegoCompletado()) {
            new Handler(Looper.getMainLooper()).postDelayed(this::finalizarJuego, 3000);
            //finalizarJuego();
        }
    }
    private boolean juegoCompletado() {
        for (boolean descubierta : cartaDescubierta) {
            if (!descubierta) {
                return false;
            }
        }
        return true;
    }
    private void finalizarJuego() {

        ganadorMensaje = determinarGanador();

        // Reproducir sonido de felicitación
        reproducirSonido(getContext(), R.raw.felicitaciones);
        for (ImageView carta : cartas) {
            carta.setVisibility(View.GONE); // Oculta todas las cartas
        }
        turnoTextView.setVisibility(View.GONE);
        puntajeJugador1.setVisibility(View.GONE);
        puntajeJugador2.setVisibility(View.GONE);


        // Mostrar animación de felicitación
        mostrarAnimacionFelicitacion();
    }
    private String determinarGanador() {
        if (puntosJugador1 > puntosJugador2) {
            return "¡Felicidades, jugador 1: "+jugador1Nombre+" fuiste el ganador!";
        } else if (puntosJugador2 > puntosJugador1) {
            return "¡Felicidades, jugador 2: "+jugador2Nombre+" fuiste el ganador!";
        } else {
            return "¡Es un empate!";
        }
    }
    private void mostrarAnimacionFelicitacion() {
        Log.d("Animacion", "Intentando mostrar la animación de felicitación");

        // Buscar el LottieAnimationView directamente
        LottieAnimationView animacion = (LottieAnimationView) getChildAt(getChildCount() - 1); // Esto debería devolver el último hijo agregado

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
            TextView ganadorTextView = new TextView(getContext());
            ganadorTextView.setText(ganadorMensaje); // Mensaje dinámico según el ganador
            ganadorTextView.setTextSize(24);
            ganadorTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
            ganadorTextView.setGravity(Gravity.CENTER);
            ganadorTextView.setLayoutParams(new GridLayout.LayoutParams(
                    GridLayout.spec(GridLayout.UNDEFINED, GridLayout.FILL), // Abarca toda la fila
                    GridLayout.spec(0, 3) // Abarca todas las columnas
            ));
            addView(ganadorTextView);

            // Crear botón "Reiniciar el juego"
            Button botonReiniciar = new Button(getContext());
            botonReiniciar.setText("Reiniciar el juego");
            botonReiniciar.setLayoutParams(new GridLayout.LayoutParams(
                    GridLayout.spec(GridLayout.UNDEFINED), // Colocación automática
                    GridLayout.spec(0, 1) // Ocupa una columna
            ));
            addView(botonReiniciar);

            // Crear botón "Guardar y avanzar"
            Button botonGuardar = new Button(getContext());
            botonGuardar.setText("Guardar y salir");
            botonGuardar.setLayoutParams(new GridLayout.LayoutParams(
                    GridLayout.spec(GridLayout.UNDEFINED), // Colocación automática
                    GridLayout.spec(1, 1) // Ocupa una columna
            ));
            botonReiniciar.setOnClickListener(v -> reiniciarJuego());
            botonGuardar.setOnClickListener(v -> {
                String jugador1 = jugador1Nombre;
                String jugador2 = jugador2Nombre;
                int puntajeJugador1 = puntosJugador1;
                int puntajeJugador2 = puntosJugador2;

                // Guardar los datos en la base de datos
                MemoramaDatabaseHelper dbHelper = new MemoramaDatabaseHelper(getContext());
                dbHelper.guardarPartidaMultijugador(jugador1, jugador2, puntajeJugador1, puntajeJugador2);

                Toast.makeText(getContext(), "Partida guardada correctamente", Toast.LENGTH_SHORT).show();

                // Salir de la actividad
                ((Activity) getContext()).finish();
            });
            addView(botonGuardar);



            // Mostrar nuevamente todas las cartas después de la animación
            /*for (ImageView carta : cartas) {
                carta.setVisibility(View.VISIBLE); // Mostrar todas las cartas de nuevo
            }*/
        }, 3000);
    }
    private void reiniciarJuego() {
        // Limpiar el layout
        removeAllViews();

        // Reiniciar puntajes
        puntosJugador1 = 0;
        puntosJugador2 = 0;

        // Volver a mezclar las cartas
        shuffleArray(imagenesCartas);

        // Crear de nuevo las cartas
        crearCartas(getContext());
        setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorFondoCartas));

        // Crear de nuevo los elementos adicionales (marcadores, turnos, etc.)
        crearTurnoTextView(getContext());
        crearPuntajes(getContext());
        // Crear la animación de felicitación nuevamente
        LottieAnimationView animacionFelicitacion = new LottieAnimationView(getContext());
        animacionFelicitacion.setAnimation(R.raw.confetti);
        animacionFelicitacion.setVisibility(View.GONE);  // Inicialmente oculto
        animacionFelicitacion.setLayoutParams(new GridLayout.LayoutParams(
                GridLayout.spec(0, 3), // La animación ocupa toda la fila
                GridLayout.spec(0, 3)  // La animación ocupa toda la columna
        ));
        addView(animacionFelicitacion);
    }

    private void reproducirSonido(Context context, int sonidoResId) {
        MediaPlayer mediaPlayer = MediaPlayer.create(context, sonidoResId);
        mediaPlayer.setOnCompletionListener(MediaPlayer::release);
        mediaPlayer.start();
    }

    private String getFrutaName(int drawableId) {
        if (drawableId == R.drawable.manzana) {
            return "apple";
        } else if (drawableId == R.drawable.fresa) {
            return "strawberry";
        } else if (drawableId == R.drawable.sandia) {
            return "watermelon";
        } else if (drawableId == R.drawable.platano) {
            return "banana";
        } else if (drawableId == R.drawable.mango) {
            return "mango";
        } else if (drawableId == R.drawable.uvas) {
            return "grape";
        } else if (drawableId == R.drawable.pera) {
            return "pear";
        } else if (drawableId == R.drawable.naranja) {
            return "orange";
        } else if (drawableId == R.drawable.pina) {
            return "pineapple";
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

    // Método para cambiar el turno
    public void cambiarTurno(int jugador) {
        if (jugador == 1) {
            turnoTextView.setText("Turno J1:"+jugador1Nombre);
            setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorJugador1)); // Azul para Jugador 1
        } else {
            turnoTextView.setText("Turno J2:"+jugador2Nombre);
            setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorJugador2)); // Rojo para Jugador 2
        }
    }
}
