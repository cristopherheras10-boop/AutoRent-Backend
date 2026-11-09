package com.autorent.api.dto;
import java.time.LocalDateTime;
// Los primeros campos mantienen compatibilidad con la campana de Angular V12.
public record NotificacionResponse(Long id,String titulo,String mensaje,String tipo,Long reservaId,boolean leida,LocalDateTime fecha,String destino,Long referenciaId){}
