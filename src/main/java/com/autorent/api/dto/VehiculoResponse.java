package com.autorent.api.dto;

import com.autorent.api.entity.EstadoVehiculo;
import java.math.BigDecimal;
import java.util.List;

public record VehiculoResponse(
        Long id,
        String placa,
        String marca,
        String modelo,
        Integer anio,
        String color,
        BigDecimal tarifaDiaria,
        EstadoVehiculo estado,
        Long categoriaId,
        String categoriaNombre,
        String imagenUrl,
        List<String> imagenes
) {
}
