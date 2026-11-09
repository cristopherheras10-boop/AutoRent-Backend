package com.autorent.api.dto;

import com.autorent.api.entity.Rol;
import jakarta.validation.constraints.NotNull;

public record CambioRolRequest(
        @NotNull Rol rol
) {
}
