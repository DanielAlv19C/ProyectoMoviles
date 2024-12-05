package com.example.demo_proyecto;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import androidx.appcompat.widget.AppCompatImageView;

public class CartaView extends AppCompatImageView {

    private int imagen;       // Imagen asignada a la carta (ej. R.drawable.leon)
    private boolean volteada; // Estado de la carta (true = imagen visible, false = reverso)

    // Constructor para inicializar desde XML
    public CartaView(Context context, AttributeSet attrs) {
        super(context, attrs);
        volteada = false; // Por defecto, la carta está tapada
    }

    /**
     * Asigna una imagen a la carta.
     * @param imagen El recurso de imagen a asignar.
     */
    public void setImagen(int imagen) {
        this.imagen = imagen;
    }

    /**
     * Obtiene la imagen asignada a la carta.
     * @return El recurso de imagen asignado.
     */
    public int getImagen() {
        return imagen;
    }

    /**
     * Verifica si la carta está volteada.
     * @return true si está volteada, false si está tapada.
     */
    public boolean isVolteada() {
        return volteada;
    }

    /**
     * Cambia el estado de la carta (volteada o tapada).
     * @param mostrar true para mostrar la imagen, false para mostrar el reverso.
     */
    public void voltear(boolean mostrar) {
        volteada = mostrar;
        if (mostrar) {
            setImageResource(imagen); // Muestra la imagen real de la carta
        } else {
            setImageResource(R.drawable.reversocart); // Muestra la imagen de reverso
        }
    }
}

