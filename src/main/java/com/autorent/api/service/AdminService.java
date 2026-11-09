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
    public AdminService(UsuarioRepository repo,NotificacionService notificaciones) {
        this.repo = repo;
        this.notificaciones = notificaciones;
    }
    public List<UsuarioResponse> listar() {
        return repo.findAll().stream().map(this::dto).toList();
    }
    public List<UsuarioResponse> pendientes() {
        return repo.findAll().stream() .filter(u -> u.getRol() == Rol.USER && u.isVerificado() && u.getEstado() == EstadoUsuario.PENDIENTE) .map(this::dto) .toList();
    }
    @Transactional
    public UsuarioResponse aprobar(Long id) {
        Usuario u = get(id);
        if (u.getRol() == Rol.ADMIN) {
            throw new IllegalArgumentException("La cuenta ADMIN ya esta protegida y aprobada");
        }
        if (!u.isVerificado()) {
            throw new IllegalArgumentException("El usuario debe verificar primero el codigo enviado a su correo");
        }
        if (u.getEstado() == EstadoUsuario.BLOQUEADO) {
            throw new IllegalArgumentException("Primero debe reactivar la cuenta bloqueada");
        }
        u.setEstado(EstadoUsuario.APROBADO);
        notificaciones.nuevaConDestino(u,"Cuenta aprobada","Tu cuenta fue aprobada. Ya puedes iniciar sesión.","CUENTA_APROBADA",null,"catalogo",null);
        return dto(u);
    }
    @Transactional
    public UsuarioResponse bloquear(Long id) {
        Usuario u = get(id);
        if (u.getRol() == Rol.ADMIN) {
            throw new IllegalArgumentException("La cuenta ADMIN principal no puede ser bloqueada");
        }
        u.setEstado(EstadoUsuario.BLOQUEADO);
        notificaciones.nuevaConDestino(u,"Cuenta bloqueada","Un administrador bloqueó tu cuenta.","CUENTA_BLOQUEADA",null,"catalogo",null);
        return dto(u);
    }
    @Transactional
    public UsuarioResponse activar(Long id) {
        Usuario u = get(id);
        if (u.getRol() == Rol.ADMIN) {
            return dto(u);
        }
        if (!u.isVerificado()) {
            throw new IllegalArgumentException("No se puede activar una cuenta que no ha verificado su correo");
        }
        u.setEstado(EstadoUsuario.APROBADO);
        notificaciones.nuevaConDestino(u,"Cuenta aprobada","Tu cuenta fue aprobada. Ya puedes iniciar sesión.","CUENTA_APROBADA",null,"catalogo",null);
        return dto(u);
    }
    private Usuario get(Long id) {
        return repo.findById(id) .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado: " + id));
    }
    private UsuarioResponse dto(Usuario u) {
        return new UsuarioResponse( u.getId(), u.getNombre(), u.getCedula(), u.getTelefono(), u.getCorreo(), u.getRol(), u.getEstado(), u.isVerificado(), u.getFechaRegistro() );
    }
}
