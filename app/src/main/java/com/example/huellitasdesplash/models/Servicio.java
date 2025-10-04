package com.example.huellitasdesplash.models;

public class Servicio {
    private String nombre;
    private String descripcion;
    private int precio;
    private int duracion;

    public Servicio(String nombre, String descripcion, int precio, int duracion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.duracion = duracion;
    }

    // Getters
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public int getPrecio() { return precio; }
    public int getDuracion() { return duracion; }
}