package com.autorent.api.controller;

import com.autorent.api.dto.*;
import com.autorent.api.service.ReservaService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController @RequestMapping("/api/reservas") @Validated public class ReservaController {
    private final ReservaService s;
    public ReservaController(ReservaService s) {
        this.s=s;
    }
    @GetMapping List<ReservaResponse> todas() {
        return s.todas();
    }
    @GetMapping("/mias") List<ReservaResponse> mias(Authentication a) {
        return s.mias(a.getName());
    }
    @PostMapping ResponseEntity<ReservaResponse> crear(@Valid @RequestBody ReservaRequest q,Authentication a) {
        return ResponseEntity.status(201).body(s.crear(q,a.getName()));
    }
    @PutMapping("/{id}/cancelar") ReservaResponse cancelar(@PathVariable @Positive Long id,Authentication a) {
        return s.cancelarPropia(id,a.getName());
    }
    @PutMapping("/{id}/estado") ReservaResponse estado(@PathVariable @Positive Long id,@Valid @RequestBody EstadoReservaRequest q) {
        return s.cambiarEstado(id,q.estado());
    }
}
