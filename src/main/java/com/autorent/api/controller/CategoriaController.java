package com.autorent.api.controller;

import com.autorent.api.dto.CategoriaRequest;
import com.autorent.api.entity.CategoriaVehiculo;
import com.autorent.api.service.CategoriaService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController @RequestMapping("/api/categorias") @Validated public class CategoriaController {
    private final CategoriaService s;
    public CategoriaController(CategoriaService s) {
        this.s=s;
    }
    @GetMapping List<CategoriaVehiculo> l() {
        return s.listar();
    }
    @GetMapping("/{id}") CategoriaVehiculo b(@PathVariable @Positive Long id) {
        return s.buscar(id);
    }
    @PostMapping ResponseEntity<CategoriaVehiculo> c(@Valid @RequestBody CategoriaRequest q) {
        return ResponseEntity.status(201).body(s.crear(q));
    }
    @PutMapping("/{id}") CategoriaVehiculo u(@PathVariable @Positive Long id,@Valid @RequestBody CategoriaRequest q) {
        return s.actualizar(id,q);
    }
    @DeleteMapping("/{id}") ResponseEntity<Void> d(@PathVariable @Positive Long id) {
        s.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
