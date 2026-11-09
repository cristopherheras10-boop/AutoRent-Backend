package com.autorent.api.dto;

import com.autorent.api.entity.EstadoReserva;
import java.math.BigDecimal;
import java.time.*;

public record ReservaResponse(
        Long id,
        Long usuarioId,
        String usuarioNombre,
        String usuarioCorreo,
        Long vehiculoId,
        String vehiculo,
        String placa,
        LocalDate fechaInicio,
        LocalDate fechaFin,
        Integer dias,
        BigDecimal total,
        EstadoReserva estado,
        LocalDateTime fechaReserva
) {
}
