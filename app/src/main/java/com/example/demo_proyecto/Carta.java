package com.example.demo_proyecto;

public class Carta {
    private int imagenTapada;
    private int imagenReal;
    private boolean estaVisible;

    public Carta(int imagenReal, int imagenTapada) {
        this.imagenReal = imagenReal;
        this.imagenTapada = imagenTapada;
        this.estaVisible = false;
    }

    public int getImagenTapada() {
        return imagenTapada;
    }

    public int getImagenReal() {
        return imagenReal;
    }

    public boolean isEstaVisible() {
        return estaVisible;
    }

    public void setEstaVisible(boolean estaVisible) {
        this.estaVisible = estaVisible;
    }
}
