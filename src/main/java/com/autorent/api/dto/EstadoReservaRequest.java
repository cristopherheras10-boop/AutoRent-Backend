package com.autorent.api.dto;

import com.autorent.api.entity.EstadoReserva;
import jakarta.validation.constraints.NotNull;

public record EstadoReservaRequest(
        @NotNull EstadoReserva estado
) {
}
