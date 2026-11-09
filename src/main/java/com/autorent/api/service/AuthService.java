package com.autorent.api.service;

import com.autorent.api.dto.*;
import com.autorent.api.entity.*;
import com.autorent.api.exception.*;
import com.autorent.api.repository.UsuarioRepository;
import com.autorent.api.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
public class AuthService {
    private final UsuarioRepository repo;
    private final PasswordEncoder encoder;
    private final JwtService jwt;
    private final EmailService mail;
    private final NotificacionService notificaciones;
    private final SecureRandom random = new SecureRandom();
    public AuthService(UsuarioRepository repo, PasswordEncoder encoder, JwtService jwt, EmailService mail, NotificacionService notificaciones) {
        this.repo = repo;
        this.encoder = encoder;
        this.jwt = jwt;
        this.mail = mail;
        this.notificaciones = notificaciones;
    }
    @Transactional
    public RegistroResponse registrar(RegistroRequest q) {
        String correo = q.correo().trim().toLowerCase();
        String cedula = q.cedula().trim();
        if (repo.existsByCorreo(correo)) throw new ConflictoException("El correo ya esta registrado");
        if (repo.existsByCedula(cedula)) throw new ConflictoException("La cedula ya esta registrada");
        Usuario u = new Usuario();
        u.setNombre(q.nombre().trim());
        u.setCedula(cedula);
        u.setTelefono(q.telefono().trim());
        u.setCorreo(correo);
        u.setPassword(encoder.encode(q.password()));
        u.setRol(Rol.USER);
        u.setEstado(EstadoUsuario.PENDIENTE);
        u.setVerificado(false);
        generarCodigo(u);
        repo.save(u);
        mail.enviarCodigoVerificacion(correo, u.getNombre(), u.getCodigoVerificacion());
        return new RegistroResponse("Registro correcto. Revise su correo e ingrese el codigo de verificacion.");
    }
    @Transactional
    public String verificar(VerificacionRequest q) {
        Usuario u = repo.findByCorreo(q.correo().trim().toLowerCase()) .orElseThrow(() -> new RecursoNoEncontradoException("No existe un usuario con ese correo"));
        if (u.isVerificado()) {
            if (u.getEstado() == EstadoUsuario.APROBADO) {
                return "La cuenta ya esta verificada y aprobada. Puede iniciar sesion.";
            }
            return "El correo ya esta verificado. Puede iniciar sesion si su cuenta esta activa.";
        }
        if (u.getCodigoVerificacion() == null || !u.getCodigoVerificacion().equals(q.codigo().trim())) {
            throw new IllegalArgumentException("El codigo de verificacion es incorrecto");
        }
        if (u.getCodigoExpiraEn() == null || u.getCodigoExpiraEn().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("El codigo ha caducado. Solicite uno nuevo");
        }
        u.setVerificado(true);
        u.setEstado(EstadoUsuario.APROBADO);
        u.setCodigoVerificacion(null);
        u.setCodigoExpiraEn(null);
        notificaciones.nuevaConDestino(u,"Cuenta verificada","Tu correo fue verificado y tu cuenta está activa.","CUENTA_VERIFICADA",null,"catalogo",null);
        notificaciones.avisarAdministradoresConDestino("Nuevo usuario verificado",u.getNombre()+" completó su registro.","USUARIO_VERIFICADO",null,"usuarios",u.getId());
        return "Correo verificado. Su cuenta esta activa y ya puede iniciar sesion.";
    }
    @Transactional
    public String reenviar(ReenviarCodigoRequest q) {
        Usuario u = repo.findByCorreo(q.correo().trim().toLowerCase()) .orElseThrow(() -> new RecursoNoEncontradoException("No existe un usuario con ese correo"));
        if (u.isVerificado()) return "La cuenta ya fue verificada";
        if (u.getEstado() == EstadoUsuario.BLOQUEADO) {
            throw new CuentaNoDisponibleException("La cuenta esta bloqueada");
        }
        generarCodigo(u);
        mail.enviarCodigoVerificacion(u.getCorreo(), u.getNombre(), u.getCodigoVerificacion());
        return "Se envio un nuevo codigo";
    }
    public AuthResponse login(LoginRequest q) {
        Usuario u = repo.findByCorreo(q.correo().trim().toLowerCase()) .orElseThrow(() -> new AutenticacionException("Correo o contrasena incorrectos"));
        if (!encoder.matches(q.password(), u.getPassword())) {
            throw new AutenticacionException("Correo o contrasena incorrectos");
        }
        if (!u.isVerificado()) {
            throw new CuentaNoDisponibleException("Debe verificar su correo con el codigo enviado");
        }
        if (u.getEstado() == EstadoUsuario.PENDIENTE) {
            throw new CuentaNoDisponibleException("La cuenta aun no esta activa. Contacte con soporte si el problema persiste");
        }
        if (u.getEstado() == EstadoUsuario.BLOQUEADO) {
            throw new CuentaNoDisponibleException("Su cuenta se encuentra bloqueada por el administrador");
        }
        if (u.getEstado() != EstadoUsuario.APROBADO) {
            throw new CuentaNoDisponibleException("La cuenta no esta disponible");
        }
        return new AuthResponse( jwt.generarToken(u.getCorreo(), u.getRol().name()), u.getRol().name(), u.getId(), u.getNombre(), u.getCorreo() );
    }
    private void generarCodigo(Usuario u) {
        u.setCodigoVerificacion(String.format("%06d", random.nextInt(1_000_000)));
        u.setCodigoExpiraEn(LocalDateTime.now().plusMinutes(15));
    }
}
