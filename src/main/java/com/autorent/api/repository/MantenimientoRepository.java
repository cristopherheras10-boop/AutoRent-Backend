package com.autorent.api.repository;

import com.autorent.api.entity.Mantenimiento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MantenimientoRepository extends JpaRepository<Mantenimiento,Long> {
}
