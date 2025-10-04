package com.example.huellitasdesplash.models;

public class CarritoItem {
    private int id;
    private int usuarioId;
    private Producto producto;
    private int cantidad;

    public CarritoItem(int id, int usuarioId, Producto producto, int cantidad) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.producto = producto;
        this.cantidad = cantidad;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUsuarioId() { return usuarioId; }
    public void setUsuarioId(int usuarioId) { this.usuarioId = usuarioId; }

    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public int getSubtotal() {
        return producto.getPrecio() * cantidad;
    }
}