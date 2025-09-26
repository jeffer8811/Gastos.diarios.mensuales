package com.example.gastos.controller;

import com.example.gastos.model.Gasto;
import com.example.gastos.model.Presupuesto;
import com.example.gastos.repository.GastoRepository;
import com.example.gastos.repository.PresupuestoRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class GastoController {

    @Autowired
    private GastoRepository gastoRepository;

    @Autowired
    private PresupuestoRepository presupuestoRepository;

    // Página principal
@GetMapping("/")
public String index(Model model) {
    int mes = LocalDate.now().getMonthValue();
    int anio = LocalDate.now().getYear();

    Presupuesto presupuesto = presupuestoRepository.findByMesAndAnio(mes, anio)
            .orElse(new Presupuesto(0.0, mes, anio));

    LocalDate inicioMes = LocalDate.of(anio, mes, 1);
    LocalDate hoy = LocalDate.now();

    double totalDiario = gastoRepository.findByTipoAndFechaBetween("DIARIO", inicioMes, hoy)
            .stream().mapToDouble(g -> g.getMonto() != null ? g.getMonto() : 0).sum();

    double totalMensual = gastoRepository.findByTipoAndFechaBetween("MENSUAL", inicioMes, hoy)
            .stream().mapToDouble(g -> g.getMonto() != null ? g.getMonto() : 0).sum();

    double saldoDisponible = presupuesto.getMonto() - totalDiario - totalMensual;

    model.addAttribute("totalDiario", totalDiario);
    model.addAttribute("totalMensual", totalMensual);
    model.addAttribute("saldoDisponible", saldoDisponible);
    model.addAttribute("presupuesto", presupuesto);

    // ---- NUEVO: Gastos por categoría ----
    List<Gasto> gastosMes = gastoRepository.findByFechaBetween(inicioMes, hoy);
    Map<String, Double> gastosPorCategoria = gastosMes.stream()
            .filter(g -> g.getCategoria() != null)
            .collect(Collectors.groupingBy(
                    Gasto::getCategoria,
                    Collectors.summingDouble(g -> g.getMonto() != null ? g.getMonto() : 0)
            ));
    model.addAttribute("gastosPorCategoria", gastosPorCategoria);

    return "index";
}


    // Formulario para nuevo gasto
    @GetMapping("/gastos/nuevo")
    public String mostrarFormulario(Model model) {
        Gasto gasto = new Gasto();
        gasto.setFecha(LocalDate.now());
        model.addAttribute("gasto", gasto);
        return "form";
    }

    // Guardar gasto
    @PostMapping("/gastos/agregar")
    public String guardar(@ModelAttribute Gasto gasto) {
        if (gasto.getFecha() == null) {
            gasto.setFecha(LocalDate.now());
        }
        gastoRepository.save(gasto);
        return "redirect:/gastos?tipo=" + gasto.getTipo();
    }

    // Listar gastos (DIARIO o MENSUAL)
    @GetMapping("/gastos")
    public String listarGastos(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
            @RequestParam(required = false) String tipo,
            Model model) {

        // Tipo por defecto
        if (tipo == null || tipo.isEmpty()) {
            tipo = "DIARIO";
        }

        // Si no se envían fechas, usar todo el mes actual
        LocalDate hoy = LocalDate.now();
        if (fechaInicio == null) fechaInicio = LocalDate.of(hoy.getYear(), hoy.getMonthValue(), 1);
        if (fechaFin == null) fechaFin = hoy;

        // Filtrar por tipo y fechas directamente desde el repositorio
        List<Gasto> gastos = gastoRepository.findByTipoAndFechaBetween(tipo, fechaInicio, fechaFin);

        // Totales
        double totalFiltrado = gastos.stream().mapToDouble(g -> g.getMonto() != null ? g.getMonto() : 0).sum();

        int mes = LocalDate.now().getMonthValue();
        int anio = LocalDate.now().getYear();
        LocalDate inicioMes = LocalDate.of(anio, mes, 1);

        double totalDiario = gastoRepository.findByTipoAndFechaBetween("DIARIO", inicioMes, hoy)
                .stream().mapToDouble(g -> g.getMonto() != null ? g.getMonto() : 0).sum();

        double totalMensual = gastoRepository.findByTipoAndFechaBetween("MENSUAL", inicioMes, hoy)
                .stream().mapToDouble(g -> g.getMonto() != null ? g.getMonto() : 0).sum();

        Presupuesto presupuesto = presupuestoRepository.findByMesAndAnio(mes, anio)
                .orElse(new Presupuesto(0.0, mes, anio));

        double saldoDisponible = presupuesto.getMonto() - totalDiario - totalMensual;

        model.addAttribute("gastos", gastos);
        model.addAttribute("totalFiltrado", totalFiltrado);
        model.addAttribute("totalDiario", totalDiario);
        model.addAttribute("totalMensual", totalMensual);
        model.addAttribute("presupuesto", presupuesto);
        model.addAttribute("saldoDisponible", saldoDisponible);
        model.addAttribute("fechaInicio", fechaInicio);
        model.addAttribute("fechaFin", fechaFin);
        model.addAttribute("tipo", tipo);

        if ("DIARIO".equalsIgnoreCase(tipo)) {
            return "gastos-diarios";
        } else if ("MENSUAL".equalsIgnoreCase(tipo)) {
            return "gastos-mensuales";
        } else {
            return "index";
        }
    }

    // Listar solo gastos mensuales
    @GetMapping("/gastos/mensuales")
    public String listarMensuales(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
            Model model) {

        LocalDate hoy = LocalDate.now();
        if (fechaInicio == null) fechaInicio = LocalDate.of(hoy.getYear(), hoy.getMonthValue(), 1);
        if (fechaFin == null) fechaFin = hoy;

        List<Gasto> gastosMensuales = gastoRepository.findByTipoAndFechaBetween("MENSUAL", fechaInicio, fechaFin);
        List<Gasto> gastosDiarios = gastoRepository.findByTipoAndFechaBetween("DIARIO", fechaInicio, fechaFin);

        double totalMensual = gastosMensuales.stream().mapToDouble(g -> g.getMonto() != null ? g.getMonto() : 0).sum();
        double totalDiario = gastosDiarios.stream().mapToDouble(g -> g.getMonto() != null ? g.getMonto() : 0).sum();

        int mes = LocalDate.now().getMonthValue();
        int anio = LocalDate.now().getYear();
        Presupuesto presupuesto = presupuestoRepository.findByMesAndAnio(mes, anio)
                .orElse(new Presupuesto(0.0, mes, anio));

        double saldoDisponible = presupuesto.getMonto() - totalMensual - totalDiario;

        model.addAttribute("gastos", gastosMensuales);
        model.addAttribute("totalMensual", totalMensual);
        model.addAttribute("totalDiario", totalDiario);
        model.addAttribute("presupuesto", presupuesto);
        model.addAttribute("saldoDisponible", saldoDisponible);
        model.addAttribute("fechaInicio", fechaInicio);
        model.addAttribute("fechaFin", fechaFin);

        return "gastos-mensuales";
    }
}
