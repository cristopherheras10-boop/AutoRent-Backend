package com.autorent.api.dto;

import jakarta.validation.constraints.*;

public record ReenviarCodigoRequest(
        @NotBlank @Email String correo
) {
}
