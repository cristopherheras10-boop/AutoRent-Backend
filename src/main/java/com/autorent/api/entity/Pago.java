package com.autorent.api.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name="pagos") public class Pago {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @OneToOne(optional=false,fetch=FetchType.LAZY) @JoinColumn(name="reserva_id",nullable=false,unique=true) private Reserva reserva;
    @Column(nullable=false) private LocalDate fechaPago=LocalDate.now();
    @Column(nullable=false,precision=10,scale=2) private BigDecimal monto;
    @Enumerated(EnumType.STRING) @Column(nullable=false,length=20) private MetodoPago metodoPago;
    @Enumerated(EnumType.STRING) @Column(nullable=false,length=20) private EstadoPago estado=EstadoPago.PAGADO;
    public Long getId() {
        return id;
    }
    public void setId(Long v) {
        id=v;
    }
    public Reserva getReserva() {
        return reserva;
    }
    public void setReserva(Reserva v) {
        reserva=v;
    }
    public LocalDate getFechaPago() {
        return fechaPago;
    }
    public void setFechaPago(LocalDate v) {
        fechaPago=v;
    }
    public BigDecimal getMonto() {
        return monto;
    }
    public void setMonto(BigDecimal v) {
        monto=v;
    }
    public MetodoPago getMetodoPago() {
        return metodoPago;
    }
    public void setMetodoPago(MetodoPago v) {
        metodoPago=v;
    }
    public EstadoPago getEstado() {
        return estado;
    }
    public void setEstado(EstadoPago v) {
        estado=v;
    }
}
