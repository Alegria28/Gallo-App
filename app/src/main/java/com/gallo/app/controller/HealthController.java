package com.gallo.app.controller;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/health") // Ruta base
public class HealthController {

    // -------- Rutas --------

    @GetMapping("/verificar")
    public ResponseEntity<Map<String, String>> checkHealth() {
        Map<String, String> respuesta = new LinkedHashMap<>();
        respuesta.put("mensaje", "Servidor funcionando correctamente");

        return ResponseEntity.status(HttpStatus.OK).body(respuesta);
    }
}
