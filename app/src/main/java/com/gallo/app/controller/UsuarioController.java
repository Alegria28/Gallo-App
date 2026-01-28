package com.gallo.app.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gallo.app.entity.Usuario;
import com.gallo.app.service.UsuarioService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
// Para los mapping y los HttpStatus
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/usuario") // Ruta base
public class UsuarioController {

    private final UsuarioService usuarioService;

    // Inyectamos el servicio
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // -------- Rutas --------

    @PostMapping("/registrar")
    public ResponseEntity<String> registrarUsuario(@RequestBody Usuario usuario) {

        // Llamamos al metodo del servicio para guardar al usuario
        usuarioService.registrarUsuario(usuario.getNombre(), usuario.getCorreo(), usuario.getContra());

        return ResponseEntity.status(HttpStatus.CREATED).body("Usuario creado");
    }

}