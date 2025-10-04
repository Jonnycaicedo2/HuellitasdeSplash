package com.example.huellitasdesplash.models;

public class Reserva {
    private int id;
    private int usuarioId;
    private String servicioNombre;
    private int precio;
    private String fecha;
    private String hora;
    private String estado;

    public Reserva(int id, int usuarioId, String servicioNombre, int precio, String fecha, String hora, String estado) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.servicioNombre = servicioNombre;
        this.precio = precio;
        this.fecha = fecha;
        this.hora = hora;
        this.estado = estado;
    }

    // Constructor sin ID (para nuevas reservas)
    public Reserva(int usuarioId, String servicioNombre, int precio, String fecha, String hora, String estado) {
        this.usuarioId = usuarioId;
        this.servicioNombre = servicioNombre;
        this.precio = precio;
        this.fecha = fecha;
        this.hora = hora;
        this.estado = estado;
    }

    // Getters
    public int getId() { return id; }
    public int getUsuarioId() { return usuarioId; }
    public String getServicioNombre() { return servicioNombre; }
    public int getPrecio() { return precio; }
    public String getFecha() { return fecha; }
    public String getHora() { return hora; }
    public String getEstado() { return estado; }
}