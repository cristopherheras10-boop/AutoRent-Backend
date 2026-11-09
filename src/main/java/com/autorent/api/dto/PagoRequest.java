package com.autorent.api.dto;

import com.autorent.api.entity.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public record PagoRequest(
        @NotNull @Positive Long reservaId,
        LocalDate fechaPago,
        @DecimalMin("0.01") @Digits(integer=8,fraction=2) BigDecimal monto,
        @NotNull MetodoPago metodoPago,
        @NotNull EstadoPago estado
) {
}
