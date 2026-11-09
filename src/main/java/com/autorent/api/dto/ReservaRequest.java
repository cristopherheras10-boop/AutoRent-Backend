package com.autorent.api.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record ReservaRequest(
        @NotNull @Positive Long vehiculoId,
        @NotNull LocalDate fechaInicio,
        @NotNull LocalDate fechaFin
) {
}
