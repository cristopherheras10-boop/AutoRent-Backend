package com.autorent.api.repository;

import com.autorent.api.entity.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface PagoRepository extends JpaRepository<Pago,Long> {
    boolean existsByReservaId(Long id);
    List<Pago> findByReservaUsuarioIdOrderByFechaPagoDesc(Long usuarioId);
}
