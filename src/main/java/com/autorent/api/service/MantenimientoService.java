package com.autorent.api.service;

import com.autorent.api.dto.*;
import com.autorent.api.entity.*;
import com.autorent.api.exception.*;
import com.autorent.api.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service
public class MantenimientoService {
    private final MantenimientoRepository r;
    private final VehiculoRepository vr;
    private final NotificacionService notificaciones;
    public MantenimientoService(MantenimientoRepository r,VehiculoRepository vr,NotificacionService notificaciones) {
        this.r=r;
        this.vr=vr;
        this.notificaciones=notificaciones;
    }
    @Transactional(readOnly=true) public List<MantenimientoResponse> listar() {
        return r.findAll().stream().map(this::dto).toList();
    }
    private Mantenimiento get(Long id) {
        return r.findById(id).orElseThrow(()->new RecursoNoEncontradoException("Mantenimiento no encontrado: "+id));
    }
    @Transactional
    public MantenimientoResponse crear(MantenimientoRequest q) {
        Mantenimiento m=new Mantenimiento();
        map(m,q);
        Mantenimiento guardado=r.save(m);
        avisarMantenimiento(guardado,"Mantenimiento registrado","MANTENIMIENTO_NUEVO");
        return dto(guardado);
    }
    @Transactional
    public MantenimientoResponse actualizar(Long id,MantenimientoRequest q) {
        Mantenimiento m=get(id);
        EstadoMantenimiento anterior=m.getEstado();
        map(m,q);
        if(anterior!=m.getEstado())
            avisarMantenimiento(m,"Estado de mantenimiento actualizado","MANTENIMIENTO_ESTADO");
        return dto(m);
    }
    @Transactional
    public void eliminar(Long id) {
        Mantenimiento m=get(id);
        Vehiculo v=m.getVehiculo();
        if(m.getEstado()==EstadoMantenimiento.EN_PROCESO&&v.getEstado()==EstadoVehiculo.MANTENIMIENTO)v.setEstado(EstadoVehiculo.DISPONIBLE);
        avisarMantenimiento(m,"Mantenimiento eliminado","MANTENIMIENTO_ELIMINADO");
        r.delete(m);
    }
    private void avisarMantenimiento(Mantenimiento m,String titulo,String tipo){
        Vehiculo v=m.getVehiculo();
        String mensaje=v.getMarca()+" "+v.getModelo()+" ("+v.getPlaca()+") · "+m.getEstado().name();
        notificaciones.avisarAdministradoresConDestino(titulo,mensaje,tipo,null,"mantenimientos",m.getId());
    }
    private void map(Mantenimiento m,MantenimientoRequest q) {
        Vehiculo v=vr.findById(q.vehiculoId()).orElseThrow(()->new RecursoNoEncontradoException("Vehiculo no encontrado: "+q.vehiculoId()));
        if(q.fechaFin()!=null&&q.fechaFin().isBefore(q.fechaInicio()))throw new IllegalArgumentException("La fecha fin no puede ser anterior a la fecha inicio");
        m.setVehiculo(v);
        m.setFechaInicio(q.fechaInicio());
        m.setFechaFin(q.fechaFin());
        m.setDescripcion(q.descripcion().trim());
        m.setCosto(q.costo());
        m.setEstado(q.estado());
        if(q.estado()==EstadoMantenimiento.EN_PROCESO)v.setEstado(EstadoVehiculo.MANTENIMIENTO);
        else if(q.estado()==EstadoMantenimiento.FINALIZADO&&v.getEstado()==EstadoVehiculo.MANTENIMIENTO)v.setEstado(EstadoVehiculo.DISPONIBLE);
    }
    private MantenimientoResponse dto(Mantenimiento m) {
        Vehiculo v=m.getVehiculo();
        return new MantenimientoResponse(m.getId(),v.getId(),v.getMarca()+" "+v.getModelo(),v.getPlaca(),m.getFechaInicio(),m.getFechaFin(),m.getDescripcion(),m.getCosto(),m.getEstado());
    }
}
