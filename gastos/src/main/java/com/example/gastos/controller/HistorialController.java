package com.example.gastos.controller;

import com.example.gastos.model.Gasto;
import com.example.gastos.repository.GastoRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class HistorialController {

    private final GastoRepository gastoRepository;

    public HistorialController(GastoRepository gastoRepository) {
        this.gastoRepository = gastoRepository;
    }

@GetMapping("/historial")
public String verHistorial(
        @RequestParam(required = false) Integer mes,
        @RequestParam(required = false) Integer anio,
        @RequestParam(required = false) String tipo,
        Model model) {

    List<Gasto> gastos;

    if (mes != null && anio != null && tipo != null && !tipo.isEmpty()) {
        gastos = gastoRepository.findByTipoAndMesAndAnio(tipo, mes, anio);
    } else if (mes != null && anio != null) {
        gastos = gastoRepository.findByMesAndAnio(mes, anio);
    } else if (tipo != null && !tipo.isEmpty()) {
        gastos = gastoRepository.findByTipo(tipo);
    } else {
        gastos = gastoRepository.findAll();
    }

    model.addAttribute("gastos", gastos);
    model.addAttribute("mes", mes);
    model.addAttribute("anio", anio);
    model.addAttribute("tipo", tipo);

    return "historial";
}

}
