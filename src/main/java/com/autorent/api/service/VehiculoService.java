package com.autorent.api.service;

import com.autorent.api.dto.*;
import com.autorent.api.entity.*;
import com.autorent.api.exception.*;
import com.autorent.api.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.*;
import java.util.Base64;

@Service
public class VehiculoService {
    private final VehiculoRepository repo;
    private final CategoriaRepository categoriaRepo;
    private final NotificacionService notificaciones;
    public VehiculoService(VehiculoRepository repo, CategoriaRepository categoriaRepo, NotificacionService notificaciones) {
        this.repo = repo;
        this.categoriaRepo = categoriaRepo;
        this.notificaciones = notificaciones;
    }
    @Transactional(readOnly = true) public List<VehiculoResponse> listar() {
        return repo.findAll().stream().map(this::dto).toList();
    }
    @Transactional(readOnly = true) public Vehiculo buscarEntidad(Long id) {
        return repo.findById(id) .orElseThrow(() -> new RecursoNoEncontradoException("Vehiculo no encontrado: " + id));
    }
    @Transactional(readOnly = true) public VehiculoResponse buscar(Long id) {
        Vehiculo v = repo.findById(id).orElseThrow(() -> new RecursoNoEncontradoException("Vehiculo no encontrado: " + id));
        return dto(v);
    }
    @Transactional(readOnly = true) public List<VehiculoResponse> disponibles() {
        return repo.findByEstado(EstadoVehiculo.DISPONIBLE).stream().map(this::dto).toList();
    }
    @Transactional
    public VehiculoResponse crear(VehiculoRequest q) {
        String placa = normalizarPlaca(q.placa());
        if (repo.existsByPlacaIgnoreCase(placa)) throw new ConflictoException("La placa ya esta registrada");
        Vehiculo v = new Vehiculo();
        map(v, q, placa);
        Vehiculo guardado=repo.save(v);
        notificaciones.avisarAdministradoresConDestino("Vehículo agregado",v.getMarca()+" "+v.getModelo()+" disponible en el catálogo.","VEHICULO_NUEVO",null,"catalogo",guardado.getId());
        return dto(guardado);
    }
    @Transactional
    public VehiculoResponse actualizar(Long id, VehiculoRequest q) {
        Vehiculo v = buscarEntidad(id);
        String placa = normalizarPlaca(q.placa());
        if (!v.getPlaca().equalsIgnoreCase(placa) && repo.existsByPlacaIgnoreCase(placa)) {
            throw new ConflictoException("La placa ya esta registrada");
        }
        EstadoVehiculo anterior=v.getEstado();
        map(v, q, placa);
        if(anterior!=v.getEstado())
            notificaciones.avisarAdministradoresConDestino("Disponibilidad de vehículo actualizada",v.getMarca()+" "+v.getModelo()+" cambió a "+v.getEstado().name(),"VEHICULO_ESTADO",null,"catalogo",v.getId());
        return dto(v);
    }
    @Transactional
    public VehiculoResponse guardarImagen(Long id, MultipartFile archivo) {
        Vehiculo v = buscarEntidad(id);
        v.setImagenUrl(procesarImagen(archivo));
        return dto(v);
    }
    @Transactional
    public VehiculoResponse agregarImagenes(Long id, List<MultipartFile> archivos) {
        Vehiculo v = buscarEntidad(id);
        if (archivos == null || archivos.isEmpty()) {
            throw new IllegalArgumentException("Debe seleccionar al menos una imagen");
        }
        if (v.getImagenes().size() + archivos.size() > 8) {
            throw new IllegalArgumentException("Puede guardar máximo 8 fotos adicionales por vehículo");
        }
        for (MultipartFile archivo : archivos) {
            v.getImagenes().add(procesarImagen(archivo));
        }
        return dto(v);
    }
    /** Intercambia la portada con una fotografía existente, sin perder ninguna. */
    @Transactional
    public VehiculoResponse seleccionarPortada(Long id, int indice) {
        Vehiculo v = buscarEntidad(id);
        if (indice < 0 || indice >= v.getImagenes().size()) {
            throw new IllegalArgumentException("La fotografía seleccionada no existe");
        }
        String nuevaPortada = v.getImagenes().get(indice);
        String portadaAnterior = v.getImagenUrl();
        v.setImagenUrl(nuevaPortada);
        v.getImagenes().remove(indice);
        if (portadaAnterior != null && !portadaAnterior.isBlank()) {
            v.getImagenes().add(indice, portadaAnterior);
        }
        repo.flush();
        return dto(v);
    }
    @Transactional
    public VehiculoResponse eliminarImagenPrincipal(Long id) {
        Vehiculo v = buscarEntidad(id);
        v.setImagenUrl(null);
        repo.flush();
        return dto(v);
    }
    @Transactional
    public VehiculoResponse eliminarImagen(Long id, int indice) {
        Vehiculo v = buscarEntidad(id);
        if (indice < 0 || indice >= v.getImagenes().size()) {
            throw new IllegalArgumentException("La imagen indicada no existe");
        }
        v.getImagenes().remove(indice);
        repo.flush();
        return dto(v);
    }
    @Transactional
    public void eliminar(Long id) {
        repo.delete(buscarEntidad(id));
    }
    private String procesarImagen(MultipartFile archivo) {
        if (archivo == null || archivo.isEmpty()) {
            throw new IllegalArgumentException("Debe seleccionar una imagen");
        }
        if (archivo.getContentType() == null || !archivo.getContentType().startsWith("image/")) {
            throw new IllegalArgumentException("El archivo debe ser una imagen");
        }
        if (archivo.getSize() > 5 * 1024 * 1024) {
            throw new IllegalArgumentException("Cada imagen no puede superar 5 MB");
        }
        try {
            String base64 = Base64.getEncoder().encodeToString(archivo.getBytes());
            return "data:" + archivo.getContentType() + ";base64," + base64;
        } catch (IOException e) {
            throw new IllegalArgumentException("No se pudo procesar la imagen");
        }
    }
    private void map(Vehiculo v, VehiculoRequest q, String placa) {
        CategoriaVehiculo c = categoriaRepo.findById(q.categoriaId()) .orElseThrow(() -> new RecursoNoEncontradoException("Categoria no encontrada: " + q.categoriaId()));
        if (!c.isActiva()) throw new IllegalArgumentException("No puede asignar una categoria inactiva");
        v.setPlaca(placa);
        v.setMarca(q.marca().trim());
        v.setModelo(q.modelo().trim());
        v.setAnio(q.anio());
        v.setColor(q.color().trim());
        v.setTarifaDiaria(q.tarifaDiaria());
        v.setEstado(q.estado());
        v.setCategoria(c);
    }
    private String normalizarPlaca(String p) {
        return p.trim().toUpperCase();
    }
    private VehiculoResponse dto(Vehiculo v) {
        return new VehiculoResponse( v.getId(), v.getPlaca(), v.getMarca(), v.getModelo(), v.getAnio(), v.getColor(), v.getTarifaDiaria(), v.getEstado(), v.getCategoria().getId(), v.getCategoria().getNombre(), v.getImagenUrl(), List.copyOf(v.getImagenes()) );
    }
}
