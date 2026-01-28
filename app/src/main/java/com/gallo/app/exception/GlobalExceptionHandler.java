package com.gallo.app.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CorreoYaExisteException.class)
    public ResponseEntity<Map<String, String>> manejarCorreoYaExiste(CorreoYaExisteException ex) {

        Map<String, String> respuesta = new HashMap<>();
        respuesta.put("mensaje", ex.getMessage());

        // Devolvemos una respuesta personalizada
        return ResponseEntity.status(HttpStatus.CONFLICT).body(respuesta);
    }
}
