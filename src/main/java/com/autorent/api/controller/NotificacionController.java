package com.autorent.api.controller;
import com.autorent.api.dto.NotificacionResponse;
import com.autorent.api.service.NotificacionService;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import java.util.List;
@RestController @RequestMapping("/api/notificaciones") public class NotificacionController{
 private final NotificacionService servicio;public NotificacionController(NotificacionService s){servicio=s;}
 @GetMapping public List<NotificacionResponse> mias(Authentication a){return servicio.mias(a.getName());}
 @PutMapping("/{id}/leer") public NotificacionResponse leer(@PathVariable Long id,Authentication a){return servicio.leer(id,a.getName());}
 @PutMapping("/leer-todas") public void todas(Authentication a){servicio.leerTodas(a.getName());}
}
