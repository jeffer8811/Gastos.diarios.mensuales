package com.example.gastos.model;

import jakarta.persistence.*;

@Entity
public class Presupuesto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double monto; // ✅ ahora acepta null
    private Integer mes;
    private Integer anio;

    // ✅ Constructor vacío (requerido por JPA)
    public Presupuesto() {
    }

    // ✅ Constructor con parámetros
    public Presupuesto(Double monto, Integer mes, Integer anio) {
        this.monto = monto;
        this.mes = mes;
        this.anio = anio;
    }

    // ✅ Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }

    public Integer getMes() {
        return mes;
    }

    public void setMes(Integer mes) {
        this.mes = mes;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }
}
