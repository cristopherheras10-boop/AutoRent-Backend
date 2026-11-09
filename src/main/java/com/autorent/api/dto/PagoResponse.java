package com.autorent.api.dto;

import com.autorent.api.entity.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public record PagoResponse(
        Long id,
        Long reservaId,
        String usuario,
        String vehiculo,
        LocalDate fechaPago,
        BigDecimal monto,
        MetodoPago metodoPago,
        EstadoPago estado
) {
}
