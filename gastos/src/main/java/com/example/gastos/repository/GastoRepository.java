package com.example.gastos.repository;

import com.example.gastos.model.Gasto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface GastoRepository extends JpaRepository<Gasto, Long> {

    // Buscar por tipo
    List<Gasto> findByTipo(String tipo);

    // Filtra solo los gastos que tengan fecha y sean del tipo seleccionado para un mes y año específicos
    @Query("SELECT g FROM Gasto g WHERE g.fecha IS NOT NULL AND g.tipo = :tipo AND MONTH(g.fecha) = :mes AND YEAR(g.fecha) = :anio")
    List<Gasto> findByTipoAndMesAndAnio(@Param("tipo") String tipo, @Param("mes") int mes, @Param("anio") int anio);

    // Filtra solo los gastos que tengan fecha en un rango
    List<Gasto> findByFechaBetween(LocalDate start, LocalDate end);

    // Filtra gastos por tipo en un rango de fechas
    List<Gasto> findByTipoAndFechaBetween(String tipo, LocalDate start, LocalDate end);

    // Filtra solo los gastos que tengan fecha y coincidan con mes y año
    @Query("SELECT g FROM Gasto g WHERE g.fecha IS NOT NULL AND MONTH(g.fecha) = :mes AND YEAR(g.fecha) = :anio")
    List<Gasto> findByMesAndAnio(@Param("mes") int mes, @Param("anio") int anio);

    // Método genérico para filtrar por mes, año y tipo (puede ser nulo)
    @Query("SELECT g FROM Gasto g WHERE (:tipo IS NULL OR g.tipo = :tipo) " +
           "AND (:mes IS NULL OR MONTH(g.fecha) = :mes) " +
           "AND (:anio IS NULL OR YEAR(g.fecha) = :anio)")
    List<Gasto> findByMesAnioTipo(@Param("mes") Integer mes,
                                  @Param("anio") Integer anio,
                                  @Param("tipo") String tipo);
}
