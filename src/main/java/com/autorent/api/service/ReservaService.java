package com.autorent.api.service;

import com.autorent.api.dto.*;
import com.autorent.api.entity.*;
import com.autorent.api.exception.*;
import com.autorent.api.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.math.BigDecimal;
import java.util.*;

@Service
public class ReservaService {
    private final ReservaRepository r;
    private final VehiculoRepository vr;
    private final UsuarioRepository ur;
    private final NotificacionService notificaciones;
    private static final List<EstadoReserva> ACTIVAS=List.of(EstadoReserva.PENDIENTE,EstadoReserva.CONFIRMADA,EstadoReserva.EN_CURSO);
    public ReservaService(ReservaRepository r,VehiculoRepository vr,UsuarioRepository ur,NotificacionService notificaciones) {
        this.r=r;
        this.vr=vr;
        this.ur=ur;
        this.notificaciones=notificaciones;
    }
    @Transactional(readOnly=true) public List<ReservaResponse> todas() {
        return r.findAll().stream().map(this::dto).toList();
    }
    @Transactional(readOnly=true) public List<ReservaResponse> mias(String correo) {
        Usuario u=user(correo);
        return r.findByUsuarioIdOrderByFechaReservaDesc(u.getId()).stream().map(this::dto).toList();
    }
    public Reserva entidad(Long id) {
        return r.findById(id).orElseThrow(()->new RecursoNoEncontradoException("Reserva no encontrada: "+id));
    }
    @Transactional
    public ReservaResponse crear(ReservaRequest q,String correo) {
        Usuario u=user(correo);
        Vehiculo v=vr.findById(q.vehiculoId()).orElseThrow(()->new RecursoNoEncontradoException("Vehiculo no encontrado: "+q.vehiculoId()));
        if(v.getEstado()!=EstadoVehiculo.DISPONIBLE)throw new IllegalArgumentException("El vehiculo no esta disponible para reservas");
        validarFechas(q.fechaInicio(),q.fechaFin());
        if(r.existeSolapamiento(v.getId(),q.fechaInicio(),q.fechaFin(),ACTIVAS))throw new ConflictoException("El vehiculo ya tiene una reserva que se cruza con esas fechas");
        long dias=Math.max(1,ChronoUnit.DAYS.between(q.fechaInicio(),q.fechaFin()));
        Reserva x=new Reserva();
        x.setUsuario(u);
        x.setVehiculo(v);
        x.setFechaInicio(q.fechaInicio());
        x.setFechaFin(q.fechaFin());
        x.setDias((int)dias);
        x.setTotal(v.getTarifaDiaria().multiply(BigDecimal.valueOf(dias)));
        x.setEstado(EstadoReserva.PENDIENTE);
        Reserva guardada=r.save(x);
        notificaciones.avisarAdministradores("Nueva solicitud de reserva",u.getNombre()+" solicitó "+v.getMarca()+" "+v.getModelo()+" · $"+guardada.getTotal(),"RESERVA_NUEVA",guardada.getId());
        notificaciones.nueva(u,"Reserva recibida","Tu reserva #"+guardada.getId()+" está pendiente de confirmación.","RESERVA_CREADA",guardada.getId());
        return dto(guardada);
    }
    @Transactional
    public ReservaResponse cancelarPropia(Long id,String correo) {
        Reserva x=entidad(id);
        if(!x.getUsuario().getCorreo().equalsIgnoreCase(correo))throw new CuentaNoDisponibleException("No puede cancelar una reserva de otro usuario");
        if(x.getEstado()!=EstadoReserva.PENDIENTE&&x.getEstado()!=EstadoReserva.CONFIRMADA)throw new IllegalArgumentException("Solo se pueden cancelar reservas PENDIENTE o CONFIRMADA");
        x.setEstado(EstadoReserva.CANCELADA);
        notificaciones.avisarAdministradores("Reserva cancelada",x.getUsuario().getNombre()+" canceló la reserva #"+x.getId(),"RESERVA_CANCELADA",x.getId());
        notificaciones.nueva(x.getUsuario(),"Reserva cancelada","Tu reserva #"+x.getId()+" fue cancelada.","RESERVA_CANCELADA",x.getId());
        return dto(x);
    }
    @Transactional
    public ReservaResponse cambiarEstado(Long id,EstadoReserva e) {
        Reserva x=entidad(id);
        if(x.getEstado()!=e){
            x.setEstado(e);
            notificaciones.nueva(x.getUsuario(),"Actualización de tu reserva","Tu reserva #"+x.getId()+" cambió a "+e.name()+".","RESERVA_ESTADO",x.getId());
        }
        return dto(x);
    }
    private Usuario user(String c) {
        return ur.findByCorreo(c.toLowerCase()).orElseThrow(()->new RecursoNoEncontradoException("Usuario no encontrado"));
    }
    private void validarFechas(LocalDate i,LocalDate f) {
        if(i.isBefore(LocalDate.now()))throw new IllegalArgumentException("La fecha de inicio no puede estar en el pasado");
        if(!f.isAfter(i))throw new IllegalArgumentException("La fecha de fin debe ser posterior a la fecha de inicio");
    }
    public ReservaResponse dto(Reserva x) {
        Vehiculo v=x.getVehiculo();
        Usuario u=x.getUsuario();
        return new ReservaResponse(x.getId(),u.getId(),u.getNombre(),u.getCorreo(),v.getId(),v.getMarca()+" "+v.getModelo(),v.getPlaca(),x.getFechaInicio(),x.getFechaFin(),x.getDias(),x.getTotal(),x.getEstado(),x.getFechaReserva());
    }
}
