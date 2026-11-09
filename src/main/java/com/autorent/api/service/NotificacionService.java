package com.autorent.api.service;
import com.autorent.api.entity.*;
import com.autorent.api.dto.NotificacionResponse;
import com.autorent.api.repository.*;
import com.autorent.api.exception.RecursoNoEncontradoException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
@Service public class NotificacionService {
 private final NotificacionRepository repo;private final UsuarioRepository usuarios;
 public NotificacionService(NotificacionRepository repo,UsuarioRepository usuarios){this.repo=repo;this.usuarios=usuarios;}
 private Usuario actual(String correo){return usuarios.findByCorreo(correo.toLowerCase()).orElseThrow(()->new RecursoNoEncontradoException("Usuario no encontrado"));}
 @Transactional(readOnly=true) public List<NotificacionResponse> mias(String correo){return repo.findTop50ByDestinatarioIdOrderByFechaDesc(actual(correo).getId()).stream().map(this::dto).toList();}
 private NotificacionResponse dto(Notificacion n){return new NotificacionResponse(n.getId(),n.getTitulo(),n.getMensaje(),n.getTipo(),n.getReservaId(),n.isLeida(),n.getFecha(),n.getDestino(),n.getReferenciaId());}
 @Transactional public NotificacionResponse leer(Long id,String correo){Notificacion n=repo.findById(id).orElseThrow(()->new RecursoNoEncontradoException("Notificación no encontrada"));if(!n.getDestinatario().getId().equals(actual(correo).getId()))throw new org.springframework.security.access.AccessDeniedException("No puede acceder a esta notificación");n.setLeida(true);return dto(n);}
 @Transactional public void leerTodas(String correo){for(Notificacion n:repo.findTop50ByDestinatarioIdOrderByFechaDesc(actual(correo).getId()))n.setLeida(true);}
 @Transactional public void nueva(Usuario destinatario,String titulo,String mensaje,String tipo,Long reservaId){
   nuevaConDestino(destinatario,titulo,mensaje,tipo,reservaId,reservaId!=null?"reservas":null,reservaId);
 }
 @Transactional public void nuevaConDestino(Usuario destinatario,String titulo,String mensaje,String tipo,Long reservaId,String destino,Long referenciaId){
   Notificacion n=new Notificacion();n.setDestinatario(destinatario);n.setTitulo(titulo);n.setMensaje(mensaje);
   n.setTipo(tipo);n.setReservaId(reservaId);n.setDestino(destino);n.setReferenciaId(referenciaId);repo.save(n);
 }
 @Transactional public void avisarAdministradores(String titulo,String mensaje,String tipo,Long reservaId){
   avisarAdministradoresConDestino(titulo,mensaje,tipo,reservaId,reservaId!=null?"reservas":null,reservaId);
 }
 @Transactional public void avisarAdministradoresConDestino(String titulo,String mensaje,String tipo,Long reservaId,String destino,Long referenciaId){
   for(Usuario u:usuarios.findAll())if(u.getRol()==Rol.ADMIN && u.getEstado()==EstadoUsuario.APROBADO)
     nuevaConDestino(u,titulo,mensaje,tipo,reservaId,destino,referenciaId);
 }
}
