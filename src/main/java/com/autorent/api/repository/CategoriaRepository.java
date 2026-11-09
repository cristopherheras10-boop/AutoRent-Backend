package com.autorent.api.repository;

import com.autorent.api.entity.CategoriaVehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CategoriaRepository extends JpaRepository<CategoriaVehiculo,Long> {
    boolean existsByNombreIgnoreCase(String nombre);
    Optional<CategoriaVehiculo> findByNombreIgnoreCase(String nombre);
}
