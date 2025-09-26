package com.example.gastos.controller;

import com.example.gastos.model.Presupuesto;
import com.example.gastos.repository.PresupuestoRepository;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
@RequestMapping("/presupuesto")
public class PresupuestoController {

    private final PresupuestoRepository presupuestoRepository;

    public PresupuestoController(PresupuestoRepository presupuestoRepository) {
        this.presupuestoRepository = presupuestoRepository;
    }

    // Permitir que "" en el form se convierta a null en Double
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Double.class, new CustomNumberEditor(Double.class, true));
    }

    // Ver presupuesto del mes actual
    @GetMapping
    public String verPresupuesto(Model model, @ModelAttribute("mensaje") String mensaje) {
        int mes = LocalDate.now().getMonthValue();
        int anio = LocalDate.now().getYear();

        // Buscar presupuesto existente o crear uno vacío
        Presupuesto presupuesto = presupuestoRepository.findByMesAndAnio(mes, anio)
                .orElseGet(() -> new Presupuesto(0.0, mes, anio));

        model.addAttribute("presupuesto", presupuesto);
        model.addAttribute("mensaje", mensaje); // Para mostrar alertas en Thymeleaf
        return "presupuesto"; // Vista: presupuesto.html
    }

    // Guardar o actualizar presupuesto
    @PostMapping("/guardar")
    public String guardarPresupuesto(
            @ModelAttribute("presupuesto") Presupuesto presupuesto,
            BindingResult bindingResult,
            RedirectAttributes redirectAttrs) {

        if (bindingResult.hasFieldErrors("monto") || presupuesto.getMonto() == null) {
            presupuesto.setMonto(0.0);
        }

        Presupuesto existente = presupuestoRepository.findByMesAndAnio(
                presupuesto.getMes(), presupuesto.getAnio()).orElse(null);

        if (existente != null) {
            existente.setMonto(presupuesto.getMonto()); // Reemplaza monto
            presupuestoRepository.save(existente);
        } else {
            presupuestoRepository.save(presupuesto);
        }

        redirectAttrs.addFlashAttribute("mensaje",
                "✅ Tu presupuesto para " + presupuesto.getMes() + "/" + presupuesto.getAnio()
                        + " es S/. " + presupuesto.getMonto());

        return "redirect:/presupuesto";
    }

    // Aumentar monto del presupuesto actual
    @PostMapping("/aumentar")
    public String aumentarPresupuesto(@RequestParam double incremento,
                                      RedirectAttributes redirectAttrs) {
        int mes = LocalDate.now().getMonthValue();
        int anio = LocalDate.now().getYear();

        Presupuesto existente = presupuestoRepository.findByMesAndAnio(mes, anio)
                .orElseGet(() -> new Presupuesto(0.0, mes, anio));

        existente.setMonto(existente.getMonto() + incremento);
        presupuestoRepository.save(existente);

        redirectAttrs.addFlashAttribute("mensaje",
                "⬆️ Tu presupuesto se incrementó en S/. " + incremento
                        + ". Total: S/. " + existente.getMonto());

        return "redirect:/presupuesto";
    }
}
