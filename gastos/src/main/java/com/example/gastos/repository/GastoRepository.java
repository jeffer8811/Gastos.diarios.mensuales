package com.example.gastos.repository;

import com.example.gastos.model.Gasto;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GastoRepository extends JpaRepository<Gasto, Long> {

    List<Gasto> findByTipo(String tipo);

    // Filtra solo los gastos que tengan fecha y sean del tipo seleccionado
    @Query("SELECT g FROM Gasto g WHERE g.fecha IS NOT NULL AND g.tipo = :tipo AND MONTH(g.fecha) = :mes AND YEAR(g.fecha) = :anio")
    List<Gasto> findByTipoAndMesAndAnio(@Param("tipo") String tipo, @Param("mes") int mes, @Param("anio") int anio);

    // Filtra solo los gastos que tengan fecha en el rango
    List<Gasto> findByFechaBetween(LocalDate start, LocalDate end);

    List<Gasto> findByTipoAndFechaBetween(String tipo, LocalDate start, LocalDate end);

    // Filtra solo los gastos que tengan fecha y coincidan con mes y año
    @Query("SELECT g FROM Gasto g WHERE g.fecha IS NOT NULL AND MONTH(g.fecha) = :mes AND YEAR(g.fecha) = :anio")
    List<Gasto> findByMesAndAnio(@Param("mes") int mes, @Param("anio") int anio);
}
