package com.autorent.api.dto;

import jakarta.validation.constraints.*;

public record CategoriaRequest(
        @NotBlank @Size(min=3,max=60) String nombre,
        @NotBlank @Size(min=5,max=200) String descripcion,
        boolean activa
) {
}
