package com.example.gastos.repository;

import com.example.gastos.model.Presupuesto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PresupuestoRepository extends JpaRepository<Presupuesto, Long> {

    // Busca un presupuesto por mes y año
    Optional<Presupuesto> findByMesAndAnio(int mes, int anio);
}
