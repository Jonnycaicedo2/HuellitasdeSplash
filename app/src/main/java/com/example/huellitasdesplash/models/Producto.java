package com.example.huellitasdesplash.models;

public class Producto {
    private int id;
    private String nombre;
    private String descripcion;
    private int precio;
    private String categoria;
    private int stock;
    private String imagen;

    // Constructor completo
    public Producto(int id, String nombre, String descripcion, int precio,
                    String categoria, int stock, String imagen) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.categoria = categoria;
        this.stock = stock;
        this.imagen = imagen;
    }

    // Constructor vacío (útil para algunos casos)
    public Producto() {
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getPrecio() {
        return precio;
    }

    public String getCategoria() {
        return categoria;
    }

    public int getStock() {
        return stock;
    }

    public String getImagen() {
        return imagen;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    // Método toString para debugging
    @Override
    public String toString() {
        return "Producto{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", precio=" + precio +
                ", categoria='" + categoria + '\'' +
                ", stock=" + stock +
                ", imagen='" + imagen + '\'' +
                '}';
    }

    // Método para verificar si tiene stock
    public boolean tieneStock() {
        return stock > 0;
    }

    // Método para verificar si tiene stock suficiente
    public boolean tieneStockSuficiente(int cantidad) {
        return stock >= cantidad;
    }
}