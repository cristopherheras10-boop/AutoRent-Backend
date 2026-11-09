package com.autorent.api.repository;

import com.autorent.api.entity.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.*;

public interface ReservaRepository extends JpaRepository<Reserva,Long> {
    List<Reserva> findByUsuarioIdOrderByFechaReservaDesc(Long usuarioId);
    @Query("select count(r)>0 from Reserva r where r.vehiculo.id=:vehiculoId and r.estado in :estados and r.fechaInicio < :fin and r.fechaFin > :inicio") boolean existeSolapamiento(@Param("vehiculoId")Long vehiculoId,@Param("inicio")LocalDate inicio,@Param("fin")LocalDate fin,@Param("estados")Collection<EstadoReserva> estados);
}
