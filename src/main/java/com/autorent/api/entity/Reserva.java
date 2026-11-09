package com.autorent.api.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.*;

@Entity
@Table(name="reservas") public class Reserva {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne(optional=false,fetch=FetchType.LAZY) @JoinColumn(name="usuario_id",nullable=false) private Usuario usuario;
    @ManyToOne(optional=false,fetch=FetchType.LAZY) @JoinColumn(name="vehiculo_id",nullable=false) private Vehiculo vehiculo;
    @Column(nullable=false) private LocalDate fechaInicio;
    @Column(nullable=false) private LocalDate fechaFin;
    @Column(nullable=false) private Integer dias;
    @Column(nullable=false,precision=10,scale=2) private BigDecimal total;
    @Enumerated(EnumType.STRING) @Column(nullable=false,length=20) private EstadoReserva estado=EstadoReserva.PENDIENTE;
    @Column(nullable=false) private LocalDateTime fechaReserva=LocalDateTime.now();
    public Long getId() {
        return id;
    }
    public void setId(Long v) {
        id=v;
    }
    public Usuario getUsuario() {
        return usuario;
    }
    public void setUsuario(Usuario v) {
        usuario=v;
    }
    public Vehiculo getVehiculo() {
        return vehiculo;
    }
    public void setVehiculo(Vehiculo v) {
        vehiculo=v;
    }
    public LocalDate getFechaInicio() {
        return fechaInicio;
    }
    public void setFechaInicio(LocalDate v) {
        fechaInicio=v;
    }
    public LocalDate getFechaFin() {
        return fechaFin;
    }
    public void setFechaFin(LocalDate v) {
        fechaFin=v;
    }
    public Integer getDias() {
        return dias;
    }
    public void setDias(Integer v) {
        dias=v;
    }
    public BigDecimal getTotal() {
        return total;
    }
    public void setTotal(BigDecimal v) {
        total=v;
    }
    public EstadoReserva getEstado() {
        return estado;
    }
    public void setEstado(EstadoReserva v) {
        estado=v;
    }
    public LocalDateTime getFechaReserva() {
        return fechaReserva;
    }
    public void setFechaReserva(LocalDateTime v) {
        fechaReserva=v;
    }
}
