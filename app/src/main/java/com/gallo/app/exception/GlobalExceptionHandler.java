package com.gallo.app.exception;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CorreoYaExisteException.class)
    private ResponseEntity<Map<String, String>> manejarCorreoYaExiste(CorreoYaExisteException ex) {

        Map<String, String> respuesta = new LinkedHashMap<>();
        respuesta.put("mensaje", ex.getMessage());

        // Devolvemos una respuesta personalizada
        return ResponseEntity.status(HttpStatus.CONFLICT).body(respuesta);
    }

    @ExceptionHandler(CorreoInvalidoException.class)
    private ResponseEntity<Map<String, String>> manejarCorreoInvalido(CorreoInvalidoException ex) {
        Map<String, String> respuesta = new LinkedHashMap<>();
        respuesta.put("mensaje", ex.getMessage());

        // Devolvemos una respuesta personalizada
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
    }

    @ExceptionHandler(AuthException.class)
    private ResponseEntity<Map<String, String>> manejarAuth(AuthException ex) {
        Map<String, String> respuesta = new LinkedHashMap<>();
        respuesta.put("mensaje", ex.getMessage());

        // Devolvemos una respuesta personalizada
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(respuesta);
    }
}