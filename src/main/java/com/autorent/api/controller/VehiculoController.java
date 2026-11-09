package com.autorent.api.controller;

import com.autorent.api.dto.*;
import com.autorent.api.service.VehiculoService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController @RequestMapping("/api/vehiculos") @Validated public class VehiculoController {
    private final VehiculoService service;
    public VehiculoController(VehiculoService service) {
        this.service = service;
    }
    @GetMapping public List<VehiculoResponse> listar() {
        return service.listar();
    }
    @GetMapping("/disponibles") public List<VehiculoResponse> disponibles() {
        return service.disponibles();
    }
    @GetMapping("/{id}") public VehiculoResponse buscar(@PathVariable @Positive Long id) {
        return service.buscar(id);
    }
    @PostMapping public ResponseEntity<VehiculoResponse> crear(@Valid @RequestBody VehiculoRequest q) {
        return ResponseEntity.status(201).body(service.crear(q));
    }
    @PutMapping("/{id}") public VehiculoResponse actualizar(@PathVariable @Positive Long id, @Valid @RequestBody VehiculoRequest q) {
        return service.actualizar(id, q);
    }
    @PostMapping(value="/{id}/imagen", consumes=MediaType.MULTIPART_FORM_DATA_VALUE) public VehiculoResponse imagenPrincipal( @PathVariable @Positive Long id, @RequestPart("imagen") MultipartFile imagen) {
        return service.guardarImagen(id, imagen);
    }
    @PostMapping(value="/{id}/imagenes", consumes=MediaType.MULTIPART_FORM_DATA_VALUE) public VehiculoResponse agregarImagenes( @PathVariable @Positive Long id, @RequestPart("imagenes") List<MultipartFile> imagenes) {
        return service.agregarImagenes(id, imagenes);
    }
    @PutMapping("/{id}/imagenes/{indice}/portada")
    public VehiculoResponse seleccionarPortada(@PathVariable @Positive Long id, @PathVariable @PositiveOrZero int indice) {
        return service.seleccionarPortada(id, indice);
    }
    @DeleteMapping("/{id}/imagen") public VehiculoResponse eliminarImagenPrincipal(@PathVariable @Positive Long id) {
        return service.eliminarImagenPrincipal(id);
    }
    @DeleteMapping("/{id}/imagenes/{indice}") public VehiculoResponse eliminarImagen( @PathVariable @Positive Long id, @PathVariable @PositiveOrZero int indice) {
        return service.eliminarImagen(id, indice);
    }
    @DeleteMapping("/{id}") public ResponseEntity<Void> eliminar(@PathVariable @Positive Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
