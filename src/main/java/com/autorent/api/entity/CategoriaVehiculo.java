package com.autorent.api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name="categorias_vehiculo") public class CategoriaVehiculo {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @NotBlank @Size(min=3,max=60) @Column(nullable=false,unique=true,length=60) private String nombre;
    @NotBlank @Size(min=5,max=200) @Column(nullable=false,length=200) private String descripcion;
    @Column(nullable=false) private boolean activa=true;
    public Long getId() {
        return id;
    }
    public void setId(Long v) {
        id=v;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String v) {
        nombre=v;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String v) {
        descripcion=v;
    }
    public boolean isActiva() {
        return activa;
    }
    public void setActiva(boolean v) {
        activa=v;
    }
}
