package com.autorent.api.entity;
import jakarta.persistence.*;
import java.time.LocalDateTime;
@Entity @Table(name="notificaciones",indexes={@Index(name="idx_notificacion_destinatario",columnList="destinatario_id,fecha")})
public class Notificacion {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="destinatario_id",nullable=false) private Usuario destinatario;
 @Column(nullable=false,length=160) private String titulo;
 @Column(nullable=false,length=500) private String mensaje;
 @Column(nullable=false,length=40) private String tipo;
 private Long reservaId;
 @Column(length=40) private String destino;
 private Long referenciaId;
 @Column(nullable=false) private boolean leida=false;
 @Column(nullable=false) private LocalDateTime fecha=LocalDateTime.now();
 public Long getId(){return id;} public Usuario getDestinatario(){return destinatario;} public void setDestinatario(Usuario v){destinatario=v;}
 public String getTitulo(){return titulo;} public void setTitulo(String v){titulo=v;}
 public String getMensaje(){return mensaje;} public void setMensaje(String v){mensaje=v;}
 public String getTipo(){return tipo;} public void setTipo(String v){tipo=v;}
 public Long getReservaId(){return reservaId;} public void setReservaId(Long v){reservaId=v;}
 public String getDestino(){return destino;} public void setDestino(String v){destino=v;}
 public Long getReferenciaId(){return referenciaId;} public void setReferenciaId(Long v){referenciaId=v;}
 public boolean isLeida(){return leida;} public void setLeida(boolean v){leida=v;}
 public LocalDateTime getFecha(){return fecha;}
}
