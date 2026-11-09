package com.autorent.api.controller;

import com.autorent.api.dto.*;
import com.autorent.api.service.PagoService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController @RequestMapping("/api/pagos") @Validated public class PagoController {
    private final PagoService s;
    public PagoController(PagoService s) {
        this.s=s;
    }
    @GetMapping List<PagoResponse> l() {
        return s.listar();
    }
    @GetMapping("/mios") List<PagoResponse> m(Authentication a) {
        return s.mios(a.getName());
    }
    @PostMapping ResponseEntity<PagoResponse> c(@Valid @RequestBody PagoRequest q) {
        return ResponseEntity.status(201).body(s.crear(q));
    }
    @PutMapping("/{id}") PagoResponse u(@PathVariable @Positive Long id,@Valid @RequestBody PagoRequest q) {
        return s.actualizar(id,q);
    }
    @DeleteMapping("/{id}") ResponseEntity<Void> d(@PathVariable @Positive Long id) {
        s.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
