package com.example.gastos.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Gasto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descripcion;
    private Double monto;
    private String categoria;
    private LocalDate fecha;

    // 🔹 Nuevo campo para tipo de gasto
    private String tipo; // Puede ser "DIARIO" o "MENSUAL"

    // 🔹 Constructor vacío
    public Gasto() {
    }

    // 🔹 Constructor con parámetros
    public Gasto(String descripcion, Double monto, String categoria, LocalDate fecha, String tipo) {
        this.descripcion = descripcion;
        this.monto = monto;
        this.categoria = categoria;
        this.fecha = fecha;
        this.tipo = tipo;
    }

    // 🔹 Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
