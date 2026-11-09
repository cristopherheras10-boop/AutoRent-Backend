
package com.autorent.api.service;

import com.autorent.api.dto.UsuarioResponse;
import com.autorent.api.entity.*;
import com.autorent.api.exception.RecursoNoEncontradoException;
import com.autorent.api.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AdminService {

    private final UsuarioRepository repo;
    private final NotificacionService notificaciones;

    public AdminService(
            UsuarioRepository repo,
            NotificacionService notificaciones
    ) {
        this.repo = repo;
        this.notificaciones = notificaciones;
    }

    // LISTAR TODOS LOS USUARIOS
    public List<UsuarioResponse> listar() {
        return repo.findAll()
                .stream()
                .map(this::dto)
                .toList();
    }

    // LISTAR USUARIOS PENDIENTES
    // Incluye usuarios que no verificaron su correo
    public List<UsuarioResponse> pendientes() {
        return repo.findAll()
                .stream()
                .filter(u ->
                        u.getRol() == Rol.USER &&
                        u.getEstado() == EstadoUsuario.PENDIENTE
                )
                .map(this::dto)
                .toList();
    }

    // APROBAR Y ACTIVAR USUARIO MANUALMENTE
    @Transactional
    public UsuarioResponse aprobar(Long id) {

        Usuario u = get(id);

        if (u.getRol() == Rol.ADMIN) {
            throw new IllegalArgumentException(
                    "La cuenta ADMIN no puede modificarse"
            );
        }

        if (u.getEstado() == EstadoUsuario.BLOQUEADO) {
            throw new IllegalArgumentException(
                    "Primero debe reactivar la cuenta bloqueada"
            );
        }

        u.setVerificado(true);
        u.setEstado(EstadoUsuario.APROBADO);
        u.setCodigoVerificacion(null);
        u.setCodigoExpiraEn(null);

        notificaciones.nuevaConDestino(
                u,
                "Cuenta aprobada",
                "Un administrador activó tu cuenta. Ya puedes iniciar sesión.",
                "CUENTA_APROBADA",
                null,
                "catalogo",
                null
        );

        return dto(u);
    }

    // BLOQUEAR USUARIO
    @Transactional
    public UsuarioResponse bloquear(Long id) {

        Usuario u = get(id);

        if (u.getRol() == Rol.ADMIN) {
            throw new IllegalArgumentException(
                    "La cuenta ADMIN principal no puede ser bloqueada"
            );
        }

        u.setEstado(EstadoUsuario.BLOQUEADO);

        notificaciones.nuevaConDestino(
                u,
                "Cuenta bloqueada",
                "Un administrador bloqueó tu cuenta.",
                "CUENTA_BLOQUEADA",
                null,
                "catalogo",
                null
        );

        return dto(u);
    }

    // REACTIVAR USUARIO BLOQUEADO
    @Transactional
    public UsuarioResponse activar(Long id) {

        Usuario u = get(id);

        if (u.getRol() == Rol.ADMIN) {
            throw new IllegalArgumentException(
                    "La cuenta ADMIN no puede modificarse"
            );
        }

        u.setVerificado(true);
        u.setEstado(EstadoUsuario.APROBADO);
        u.setCodigoVerificacion(null);
        u.setCodigoExpiraEn(null);

        notificaciones.nuevaConDestino(
                u,
                "Cuenta activada",
                "Un administrador activó tu cuenta. Ya puedes iniciar sesión.",
                "CUENTA_APROBADA",
                null,
                "catalogo",
                null
        );

        return dto(u);
    }

    // BUSCAR USUARIO POR ID
    private Usuario get(Long id) {
        return repo.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException(
                                "Usuario no encontrado: " + id
                        )
                );
    }

    // CONVERTIR ENTIDAD A RESPUESTA
    private UsuarioResponse dto(Usuario u) {
        return new UsuarioResponse(
                u.getId(),
                u.getNombre(),
                u.getCedula(),
                u.getTelefono(),
                u.getCorreo(),
                u.getRol(),
                u.getEstado(),
                u.isVerificado(),
                u.getFechaRegistro()
        );
    }
}
