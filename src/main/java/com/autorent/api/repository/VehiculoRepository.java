package com.autorent.api.repository;

import com.autorent.api.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface VehiculoRepository extends JpaRepository<Vehiculo,Long> {
    boolean existsByPlacaIgnoreCase(String placa);
    Optional<Vehiculo> findByPlacaIgnoreCase(String placa);
    List<Vehiculo> findByEstado(EstadoVehiculo estado);
}
