package com.example.gastos.service;

import com.example.gastos.model.Gasto;
import com.example.gastos.repository.GastoRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class GastoService {
    private final GastoRepository repo;

    public GastoService(GastoRepository repo) {
        this.repo = repo;
    }

    public List<Gasto> listar() {
        return repo.findAll();
    }

    public void guardar(Gasto gasto) {
        repo.save(gasto);
    }
}
