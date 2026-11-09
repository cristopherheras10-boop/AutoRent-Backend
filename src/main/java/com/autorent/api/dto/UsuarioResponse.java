package com.autorent.api.dto;

import com.autorent.api.entity.*;
import java.time.LocalDateTime;

public record UsuarioResponse(
        Long id,
        String nombre,
        String cedula,
        String telefono,
        String correo,
        Rol rol,
        EstadoUsuario estado,
        boolean verificado,
        LocalDateTime fechaRegistro
) {
}
