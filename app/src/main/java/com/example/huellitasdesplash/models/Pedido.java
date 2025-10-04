package com.example.huellitasdesplash.models;

public class Pedido {
    private int id;
    private int usuarioId;
    private int total;
    private String metodoPago;
    private String estado;
    private String fechaPedido;

    public Pedido(int id, int usuarioId, int total, String metodoPago, String estado, String fechaPedido) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.total = total;
        this.metodoPago = metodoPago;
        this.estado = estado;
        this.fechaPedido = fechaPedido;
    }

    public Pedido() {
    }

    // Getters
    public int getId() { return id; }
    public int getUsuarioId() { return usuarioId; }
    public int getTotal() { return total; }
    public String getMetodoPago() { return metodoPago; }
    public String getEstado() { return estado; }
    public String getFechaPedido() { return fechaPedido; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setUsuarioId(int usuarioId) { this.usuarioId = usuarioId; }
    public void setTotal(int total) { this.total = total; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }
    public void setEstado(String estado) { this.estado = estado; }
    public void setFechaPedido(String fechaPedido) { this.fechaPedido = fechaPedido; }

    @Override
    public String toString() {
        return "Pedido{" +
                "id=" + id +
                ", usuarioId=" + usuarioId +
                ", total=" + total +
                ", metodoPago='" + metodoPago + '\'' +
                ", estado='" + estado + '\'' +
                ", fechaPedido='" + fechaPedido + '\'' +
                '}';
    }
}