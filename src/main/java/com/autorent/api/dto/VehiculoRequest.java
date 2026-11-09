package com.autorent.api.dto;

import com.autorent.api.entity.EstadoVehiculo;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record VehiculoRequest(
        @NotBlank @Pattern(regexp="^[A-Za-z]{3}-?\\d{3,4}$",message="La placa debe tener formato ABC-1234 o ABC1234") String placa,
        @NotBlank @Size(min=2,max=60) String marca,
        @NotBlank @Size(min=1,max=60) String modelo,
        @NotNull @Min(1990) @Max(2035) Integer anio,
        @NotBlank @Size(min=3,max=40) String color,
        @NotNull @DecimalMin("0.01") @Digits(integer=8,fraction=2) BigDecimal tarifaDiaria,
        @NotNull EstadoVehiculo estado,
        @NotNull @Positive Long categoriaId
) {
}
