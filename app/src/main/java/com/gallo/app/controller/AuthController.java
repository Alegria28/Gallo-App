package com.gallo.app.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gallo.app.entity.Usuario;
import com.gallo.app.service.AuthService;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
// Para los mapping y los HttpStatus
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth") // Ruta base
public class AuthController {

    private final AuthService authService;

    // Inyectamos el servicio
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // -------- Rutas --------

    @PostMapping("/registrar")
    public ResponseEntity<Map<String, String>> registrarUsuario(@RequestBody Usuario usuario) {

        // Llamamos al metodo del servicio para guardar al usuario
        authService.registrarUsuario(usuario.getNombre(), usuario.getCorreo(), usuario.getContra());

        Map<String, String> respuesta = new HashMap<>();
        respuesta.put("mensaje", "Usuario creado");

        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    }

}