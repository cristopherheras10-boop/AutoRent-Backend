package com.autorent.api.controller;

import com.autorent.api.dto.*;
import com.autorent.api.service.MantenimientoService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController @RequestMapping("/api/mantenimientos") @Validated public class MantenimientoController {
    private final MantenimientoService s;
    public MantenimientoController(MantenimientoService s) {
        this.s=s;
    }
    @GetMapping List<MantenimientoResponse> l() {
        return s.listar();
    }
    @PostMapping ResponseEntity<MantenimientoResponse> c(@Valid @RequestBody MantenimientoRequest q) {
        return ResponseEntity.status(201).body(s.crear(q));
    }
    @PutMapping("/{id}") MantenimientoResponse u(@PathVariable @Positive Long id,@Valid @RequestBody MantenimientoRequest q) {
        return s.actualizar(id,q);
    }
    @DeleteMapping("/{id}") ResponseEntity<Void> d(@PathVariable @Positive Long id) {
        s.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
