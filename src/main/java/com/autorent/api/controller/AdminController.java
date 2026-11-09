package com.autorent.api.controller;

import com.autorent.api.dto.UsuarioResponse;
import com.autorent.api.service.AdminService;
import jakarta.validation.constraints.Positive;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController @RequestMapping("/api/admin/usuarios") @Validated public class AdminController {
    private final AdminService service;
    public AdminController(AdminService service) {
        this.service = service;
    }
    @GetMapping public List<UsuarioResponse> listar() {
        return service.listar();
    }
    @GetMapping("/pendientes") public List<UsuarioResponse> pendientes() {
        return service.pendientes();
    }
    @PutMapping("/{id}/aprobar") public UsuarioResponse aprobar(@PathVariable @Positive Long id) {
        return service.aprobar(id);
    }
    @PutMapping("/{id}/bloquear") public UsuarioResponse bloquear(@PathVariable @Positive Long id) {
        return service.bloquear(id);
    }
    @PutMapping("/{id}/activar") public UsuarioResponse activar(@PathVariable @Positive Long id) {
        return service.activar(id);
    }
}
