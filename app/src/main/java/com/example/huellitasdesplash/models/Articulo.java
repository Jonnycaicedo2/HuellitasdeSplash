package com.example.huellitasdesplash.models;

public class Articulo {
    private String titulo;
    private String subtitulo;
    private String contenido;
    private int imagenResId;
    private String categoria;
    private int tiempoLectura; // en minutos

    public Articulo(String titulo, String subtitulo, String contenido,
                    int imagenResId, String categoria, int tiempoLectura) {
        this.titulo = titulo;
        this.subtitulo = subtitulo;
        this.contenido = contenido;
        this.imagenResId = imagenResId;
        this.categoria = categoria;
        this.tiempoLectura = tiempoLectura;
    }

    // Getters
    public String getTitulo() { return titulo; }
    public String getSubtitulo() { return subtitulo; }
    public String getContenido() { return contenido; }
    public int getImagenResId() { return imagenResId; }
    public String getCategoria() { return categoria; }
    public int getTiempoLectura() { return tiempoLectura; }
}