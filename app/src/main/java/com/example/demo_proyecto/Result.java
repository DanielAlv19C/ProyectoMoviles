package com.example.demo_proyecto;
public class Result {
    private String nombre;
    private int puntaje;
    private String tiempo;

    public Result(String nombre, int puntaje, String tiempo) {
        this.nombre = nombre;
        this.puntaje = puntaje;
        this.tiempo = tiempo;
    }

    public String getNombre() {
        return nombre;
    }

    public int getPuntaje() {
        return puntaje;
    }

    public String getTiempo() {
        return tiempo;
    }
}
