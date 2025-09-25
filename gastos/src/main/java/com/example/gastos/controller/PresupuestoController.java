package com.example.gastos.controller;

import com.example.gastos.model.Presupuesto;
import com.example.gastos.repository.PresupuestoRepository;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/presupuesto")
public class PresupuestoController {

    private final PresupuestoRepository presupuestoRepository;

    public PresupuestoController(PresupuestoRepository presupuestoRepository) {
        this.presupuestoRepository = presupuestoRepository;
    }

    // ✅ Permitir que "" en el form se convierta a null en Double
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Double.class, new CustomNumberEditor(Double.class, true));
    }

    // ✅ Ver presupuesto del mes actual
    @GetMapping
    public String verPresupuesto(Model model) {
        int mes = LocalDate.now().getMonthValue();
        int anio = LocalDate.now().getYear();

        // Buscar presupuesto existente o crear uno vacío
        Presupuesto presupuesto = presupuestoRepository.findByMesAndAnio(mes, anio)
                .orElseGet(() -> new Presupuesto(0.0, mes, anio));

        // 👇 Necesario para que Thymeleaf tenga el objeto del formulario
        model.addAttribute("presupuesto", presupuesto);

        return "presupuesto"; // Vista: presupuesto.html
    }

    // ✅ Guardar o actualizar presupuesto
    @PostMapping("/guardar")
    public String guardarPresupuesto(
            @ModelAttribute("presupuesto") Presupuesto presupuesto,
            BindingResult bindingResult) {

        // Si hubo error de binding en 'monto' o viene null → poner en 0.0
        if (bindingResult.hasFieldErrors("monto") || presupuesto.getMonto() == null) {
            presupuesto.setMonto(0.0);
        }

        // Buscar si ya existe presupuesto para el mismo mes y año
        Presupuesto existente = presupuestoRepository.findByMesAndAnio(
                presupuesto.getMes(), presupuesto.getAnio()).orElse(null);

        if (existente != null) {
            double montoExistente = existente.getMonto() != 0 ? existente.getMonto() : 0.0;
            double montoNuevo = presupuesto.getMonto() != 0 ? presupuesto.getMonto() : 0.0;
            existente.setMonto(montoExistente + montoNuevo);
            presupuestoRepository.save(existente);
        } else {
            presupuestoRepository.save(presupuesto);
        }

        return "redirect:/presupuesto";
    }

    // ✅ Aumentar monto del presupuesto actual
    @PostMapping("/aumentar")
    public String aumentarPresupuesto(@RequestParam double incremento) {
        int mes = LocalDate.now().getMonthValue();
        int anio = LocalDate.now().getYear();

        Presupuesto existente = presupuestoRepository.findByMesAndAnio(mes, anio)
                .orElseGet(() -> new Presupuesto(0.0, mes, anio));

        existente.setMonto(existente.getMonto() + incremento);
        presupuestoRepository.save(existente);

        return "redirect:/presupuesto";
    }
}
