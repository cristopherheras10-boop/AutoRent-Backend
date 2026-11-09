package com.autorent.api.dto;

import com.autorent.api.entity.EstadoMantenimiento;
import java.math.BigDecimal;
import java.time.LocalDate;

public record MantenimientoResponse(
        Long id,
        Long vehiculoId,
        String vehiculo,
        String placa,
        LocalDate fechaInicio,
        LocalDate fechaFin,
        String descripcion,
        BigDecimal costo,
        EstadoMantenimiento estado
) {
}
