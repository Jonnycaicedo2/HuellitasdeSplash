package com.example.huellitasdesplash.models;

public class ServicioHistorial {
    private int id;
    private int usuarioId;
    private String nombreServicio;
    private String descripcion;
    private int precio;
    private int duracion; // en minutos
    private String fechaServicio;
    private String estado; // completado, pendiente, cancelado
    private String fechaCreacion;

    public ServicioHistorial(int id, int usuarioId, String nombreServicio, String descripcion,
                             int precio, int duracion, String fechaServicio, String estado, String fechaCreacion) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.nombreServicio = nombreServicio;
        this.descripcion = descripcion;
        this.precio = precio;
        this.duracion = duracion;
        this.fechaServicio = fechaServicio;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
    }

    public ServicioHistorial() {
    }

    // Getters
    public int getId() { return id; }
    public int getUsuarioId() { return usuarioId; }
    public String getNombreServicio() { return nombreServicio; }
    public String getDescripcion() { return descripcion; }
    public int getPrecio() { return precio; }
    public int getDuracion() { return duracion; }
    public String getFechaServicio() { return fechaServicio; }
    public String getEstado() { return estado; }
    public String getFechaCreacion() { return fechaCreacion; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setUsuarioId(int usuarioId) { this.usuarioId = usuarioId; }
    public void setNombreServicio(String nombreServicio) { this.nombreServicio = nombreServicio; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setPrecio(int precio) { this.precio = precio; }
    public void setDuracion(int duracion) { this.duracion = duracion; }
    public void setFechaServicio(String fechaServicio) { this.fechaServicio = fechaServicio; }
    public void setEstado(String estado) { this.estado = estado; }
    public void setFechaCreacion(String fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    @Override
    public String toString() {
        return "ServicioHistorial{" +
                "id=" + id +
                ", usuarioId=" + usuarioId +
                ", nombreServicio='" + nombreServicio + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", precio=" + precio +
                ", duracion=" + duracion +
                ", fechaServicio='" + fechaServicio + '\'' +
                ", estado='" + estado + '\'' +
                ", fechaCreacion='" + fechaCreacion + '\'' +
                '}';
    }

    // Método para obtener duración formateada
    public String getDuracionFormateada() {
        if (duracion < 60) {
            return duracion + " min";
        } else {
            int horas = duracion / 60;
            int minutos = duracion % 60;
            if (minutos == 0) {
                return horas + " h";
            } else {
                return horas + " h " + minutos + " min";
            }
        }
    }

    // Método para obtener precio formateado
    public String getPrecioFormateado() {
        return String.format("$%,d", precio);
    }

    // Método para obtener color según estado
    public String getColorEstado() {
        switch (estado.toLowerCase()) {
            case "completado":
                return "#4CAF50"; // Verde
            case "pendiente":
                return "#FF9800"; // Naranja
            case "cancelado":
                return "#F44336"; // Rojo
            default:
                return "#9E9E9E"; // Gris
        }
    }
}