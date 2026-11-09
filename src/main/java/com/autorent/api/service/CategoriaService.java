package com.autorent.api.service;

import com.autorent.api.dto.CategoriaRequest;
import com.autorent.api.entity.CategoriaVehiculo;
import com.autorent.api.exception.*;
import com.autorent.api.repository.CategoriaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service
public class CategoriaService {
    private final CategoriaRepository r;
    private final NotificacionService notificaciones;
    public CategoriaService(CategoriaRepository r,NotificacionService notificaciones) {
        this.r=r;
        this.notificaciones=notificaciones;
    }
    public List<CategoriaVehiculo> listar() {
        return r.findAll();
    }
    public CategoriaVehiculo buscar(Long id) {
        return r.findById(id).orElseThrow(()->new RecursoNoEncontradoException("Categoria no encontrada: "+id));
    }
    @Transactional
    public CategoriaVehiculo crear(CategoriaRequest q) {
        if(r.existsByNombreIgnoreCase(q.nombre().trim()))throw new ConflictoException("Ya existe una categoria con ese nombre");
        CategoriaVehiculo c=new CategoriaVehiculo();
        map(c,q);
        CategoriaVehiculo guardada=r.save(c);
        notificaciones.avisarAdministradoresConDestino("Categoría creada","Se agregó la categoría "+c.getNombre()+".","CATEGORIA_NUEVA",null,"categorias",guardada.getId());
        return guardada;
    }
    @Transactional
    public CategoriaVehiculo actualizar(Long id,CategoriaRequest q) {
        CategoriaVehiculo c=buscar(id);
        if(!c.getNombre().equalsIgnoreCase(q.nombre().trim())&&r.existsByNombreIgnoreCase(q.nombre().trim()))throw new ConflictoException("Ya existe una categoria con ese nombre");
        map(c,q);
        return c;
    }
    @Transactional
    public void eliminar(Long id) {
        r.delete(buscar(id));
    }
    private void map(CategoriaVehiculo c,CategoriaRequest q) {
        c.setNombre(q.nombre().trim());
        c.setDescripcion(q.descripcion().trim());
        c.setActiva(q.activa());
    }
}
