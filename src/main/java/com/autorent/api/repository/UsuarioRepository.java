package com.autorent.api.repository;

import com.autorent.api.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface UsuarioRepository extends JpaRepository<Usuario,Long> {
    Optional<Usuario> findByCorreo(String correo);
    boolean existsByCorreo(String correo);
    boolean existsByCedula(String cedula);
    List<Usuario> findByEstado(EstadoUsuario estado);
}
