package com.autorent.api.service;

import com.autorent.api.dto.*;
import com.autorent.api.entity.*;
import com.autorent.api.exception.*;
import com.autorent.api.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.*;

@Service
public class PagoService {
    private final PagoRepository r;
    private final ReservaRepository rr;
    private final UsuarioRepository ur;
    private final NotificacionService notificaciones;
    public PagoService(PagoRepository r,ReservaRepository rr,UsuarioRepository ur,NotificacionService notificaciones) {
        this.r=r;
        this.rr=rr;
        this.ur=ur;
        this.notificaciones=notificaciones;
    }
    @Transactional(readOnly=true) public List<PagoResponse> listar() {
        return r.findAll().stream().map(this::dto).toList();
    }
    @Transactional(readOnly=true) public List<PagoResponse> mios(String correo) {
        Usuario u=ur.findByCorreo(correo.toLowerCase()).orElseThrow(()->new RecursoNoEncontradoException("Usuario no encontrado"));
        return r.findByReservaUsuarioIdOrderByFechaPagoDesc(u.getId()).stream().map(this::dto).toList();
    }
    private Pago get(Long id) {
        return r.findById(id).orElseThrow(()->new RecursoNoEncontradoException("Pago no encontrado: "+id));
    }
    @Transactional
    public PagoResponse crear(PagoRequest q) {
        Reserva re=rr.findById(q.reservaId()).orElseThrow(()->new RecursoNoEncontradoException("Reserva no encontrada: "+q.reservaId()));
        if(r.existsByReservaId(re.getId()))throw new ConflictoException("La reserva ya tiene un pago registrado");
        Pago p=new Pago();
        map(p,q,re);
        Pago guardado=r.save(p);
        avisarPago(guardado,"Pago registrado","PAGO_NUEVO");
        return dto(guardado);
    }
    @Transactional
    public PagoResponse actualizar(Long id,PagoRequest q) {
        Pago p=get(id);
        Reserva re=rr.findById(q.reservaId()).orElseThrow(()->new RecursoNoEncontradoException("Reserva no encontrada: "+q.reservaId()));
        if(!p.getReserva().getId().equals(re.getId())&&r.existsByReservaId(re.getId()))throw new ConflictoException("La reserva ya tiene un pago registrado");
        EstadoPago anterior=p.getEstado();
        Long reservaAnterior=p.getReserva().getId();
        map(p,q,re);
        if(anterior!=p.getEstado() || !reservaAnterior.equals(re.getId()))
            avisarPago(p,"Estado de pago actualizado","PAGO_ESTADO");
        return dto(p);
    }
    @Transactional
    public void eliminar(Long id) {
        Pago p=get(id);
        // Guardar la notificación dentro de la misma transacción que elimina el pago.
        avisarPago(p,"Pago eliminado","PAGO_ELIMINADO");
        r.delete(p);
    }
    private void avisarPago(Pago p,String titulo,String tipo){
        Reserva re=p.getReserva();
        String mensaje="Reserva #"+re.getId()+" · "+re.getVehiculo().getMarca()+" "+re.getVehiculo().getModelo()+" · estado "+p.getEstado().name();
        notificaciones.nuevaConDestino(re.getUsuario(),titulo,mensaje,tipo,null,"pagos",p.getId());
        notificaciones.avisarAdministradoresConDestino(titulo,mensaje,tipo,null,"pagos",p.getId());
    }
    private void map(Pago p,PagoRequest q,Reserva re) {
        p.setReserva(re);
        p.setFechaPago(q.fechaPago()==null?LocalDate.now():q.fechaPago());
        // El total lo determina la reserva; nunca se acepta un importe enviado por el cliente.
        p.setMonto(re.getTotal());
        p.setMetodoPago(q.metodoPago());
        p.setEstado(q.estado());
    }
    private PagoResponse dto(Pago p) {
        Reserva re=p.getReserva();
        return new PagoResponse(p.getId(),re.getId(),re.getUsuario().getNombre(),re.getVehiculo().getMarca()+" "+re.getVehiculo().getModelo(),p.getFechaPago(),p.getMonto(),p.getMetodoPago(),p.getEstado());
    }
}
