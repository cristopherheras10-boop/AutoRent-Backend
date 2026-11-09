package com.autorent.api.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender mailSender;
    @Value("${app.mail.enabled:false}") private boolean enabled;
    @Value("${spring.mail.username:}") private String from;
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }
    public void enviarCodigoVerificacion(String to, String nombre, String codigo) {
        if (!enabled) {
            System.out.println("[DEV] Codigo para " + to + ": " + codigo);
            return;
        }
        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setFrom(from);
        mensaje.setTo(to);
        mensaje.setSubject("AutoRent - Codigo de verificacion");
        mensaje.setText( "Hola " + nombre + ",\n\n" + "Tu codigo de verificacion es: " + codigo + "\n" + "Caduca en 15 minutos.\n\n" + "AutoRent" );
        mailSender.send(mensaje);
    }
}
