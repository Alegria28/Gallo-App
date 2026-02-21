package com.gallo.app.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    //! Importante cambiar por un dominio de verdad
    @Value("${MAIL_FROM}")
    private String de;

    //! Implementar en la pagina respectiva
    @Value("${FRONTEND}")
    private String frontend;

    private final JavaMailSender javaMailSender;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void solicitarTokenVerificacion(String para, String token) {

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(para);
        email.setSubject("Gallo App - Codigo verificacion");
        email.setFrom(de);

        //! URL del frontend para verificar cuenta
        String urlVerificacion = token;

        email.setText("Bienvenido a Gallo App\n\n" +
                "Haz clic en el siguiente enlace para verificar tu cuenta:\n" +
                urlVerificacion + "\n\n" +
                "Este enlace expirará en 10 minutos.\n\n" +
                "Si no solicitaste este código, ignora este mensaje.");

        // Enviamos el correo
        javaMailSender.send(email);
    }

    public void solicitarRestablecerContra(String para, String token) {

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(para);
        email.setSubject("Gallo App - Restablecer contraseña");
        email.setFrom(de);

        //! URL del frontend para restablecer contraseña
        String urlVerificacion = token;

        email.setText("Bienvenido a Gallo App\n\n" +
                "Haz clic en el siguiente enlace para restablecer tu contraseña:\n" +
                urlVerificacion + "\n\n" +
                "Este enlace expirará en 10 minutos.\n\n" +
                "Si no solicitaste este código, ignora este mensaje.");

        // Enviamos el correo
        javaMailSender.send(email);
    }

}
