package com.autorent.api.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name="mantenimientos") public class Mantenimiento {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne(optional=false,fetch=FetchType.LAZY) @JoinColumn(name="vehiculo_id",nullable=false) private Vehiculo vehiculo;
    @Column(nullable=false) private LocalDate fechaInicio;
    private LocalDate fechaFin;
    @Column(nullable=false,length=300) private String descripcion;
    @Column(nullable=false,precision=10,scale=2) private BigDecimal costo;
    @Enumerated(EnumType.STRING) @Column(nullable=false,length=20) private EstadoMantenimiento estado=EstadoMantenimiento.PROGRAMADO;
    public Long getId() {
        return id;
    }
    public void setId(Long v) {
        id=v;
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
    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String v) {
        descripcion=v;
    }
    public BigDecimal getCosto() {
        return costo;
    }
    public void setCosto(BigDecimal v) {
        costo=v;
    }
    public EstadoMantenimiento getEstado() {
        return estado;
    }
    public void setEstado(EstadoMantenimiento v) {
        estado=v;
    }
}
