package com.autorent.api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger log =
            LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;

    @Value("${app.mail.enabled:false}")
    private boolean enabled;

    @Value("${spring.mail.username:}")
    private String from;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void enviarCodigoVerificacion(
            String to,
            String nombre,
            String codigo
    ) {

        if (!enabled) {
            log.warn("El envio de correos esta deshabilitado");
            return;
        }

        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setFrom(from);
        mensaje.setTo(to);
        mensaje.setSubject("AutoRent - Codigo de verificacion");
        mensaje.setText(
                "Hola " + nombre + ",\n\n" +
                "Tu codigo de verificacion es: " + codigo +
                "\nCaduca en 15 minutos.\n\nAutoRent"
        );

        try {
            mailSender.send(mensaje);
            log.info("Correo de verificacion enviado correctamente");
        } catch (Exception e) {
            log.error("ERROR AL ENVIAR CORREO DE VERIFICACION", e);
            throw e;
        }
    }
}