package com.autorent.api.dto;

import com.autorent.api.entity.EstadoMantenimiento;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public record MantenimientoRequest(
        @NotNull @Positive Long vehiculoId,
        @NotNull LocalDate fechaInicio,
        LocalDate fechaFin,
        @NotBlank @Size(min=5,max=300) String descripcion,
        @NotNull @DecimalMin("0.00") @Digits(integer=8,fraction=2) BigDecimal costo,
        @NotNull EstadoMantenimiento estado
) {
}
