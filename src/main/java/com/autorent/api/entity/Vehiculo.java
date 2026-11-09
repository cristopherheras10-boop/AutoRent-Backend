package com.autorent.api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="vehiculos") public class Vehiculo {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @NotBlank @Pattern(regexp="^[A-Z]{3}-?\\d{3,4}$", message="La placa debe tener formato ABC-1234 o ABC1234") @Column(nullable=false, unique=true, length=8) private String placa;
    @NotBlank @Size(min=2,max=60) @Column(nullable=false,length=60) private String marca;
    @NotBlank @Size(min=1,max=60) @Column(nullable=false,length=60) private String modelo;
    @Min(1990) @Max(2035) @Column(nullable=false) private Integer anio;
    @NotBlank @Size(min=3,max=40) @Column(nullable=false,length=40) private String color;
    @NotNull @DecimalMin(value="0.01") @Digits(integer=8,fraction=2) @Column(nullable=false,precision=10,scale=2) private BigDecimal tarifaDiaria;
    @Enumerated(EnumType.STRING) @Column(nullable=false,length=20) private EstadoVehiculo estado=EstadoVehiculo.DISPONIBLE;
    @ManyToOne(optional=false,fetch=FetchType.LAZY) @JoinColumn(name="categoria_id",nullable=false) private CategoriaVehiculo categoria;
    @Column(columnDefinition="TEXT") private String imagenUrl;
    @ElementCollection(fetch = FetchType.LAZY) @CollectionTable(name = "vehiculo_imagenes", joinColumns = @JoinColumn(name = "vehiculo_id")) @OrderColumn(name = "orden") @Column(name = "imagen_url", columnDefinition = "TEXT", nullable = false) private List<String> imagenes = new ArrayList<>();
    public Long getId() {
        return id;
    }
    public void setId(Long v) {
        id=v;
    }
    public String getPlaca() {
        return placa;
    }
    public void setPlaca(String v) {
        placa=v;
    }
    public String getMarca() {
        return marca;
    }
    public void setMarca(String v) {
        marca=v;
    }
    public String getModelo() {
        return modelo;
    }
    public void setModelo(String v) {
        modelo=v;
    }
    public Integer getAnio() {
        return anio;
    }
    public void setAnio(Integer v) {
        anio=v;
    }
    public String getColor() {
        return color;
    }
    public void setColor(String v) {
        color=v;
    }
    public BigDecimal getTarifaDiaria() {
        return tarifaDiaria;
    }
    public void setTarifaDiaria(BigDecimal v) {
        tarifaDiaria=v;
    }
    public EstadoVehiculo getEstado() {
        return estado;
    }
    public void setEstado(EstadoVehiculo v) {
        estado=v;
    }
    public CategoriaVehiculo getCategoria() {
        return categoria;
    }
    public void setCategoria(CategoriaVehiculo v) {
        categoria=v;
    }
    public String getImagenUrl() {
        return imagenUrl;
    }
    public void setImagenUrl(String v) {
        imagenUrl=v;
    }
    public List<String> getImagenes() {
        return imagenes;
    }
    public void setImagenes(List<String> v) {
        imagenes=v==null?new ArrayList<>():v;
    }
}
