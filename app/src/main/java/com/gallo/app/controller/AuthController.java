package com.gallo.app.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gallo.app.entity.Usuario;
import com.gallo.app.service.AuthService;

import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

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

        Map<String, String> respuesta = new LinkedHashMap<>();
        respuesta.put("mensaje", "Usuario creado: Revisa tu correo institucional para verificar la cuenta");

        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    }

    @PostMapping("/iniciarSesion")
    public ResponseEntity<Map<String, String>> iniciarSesion(@RequestBody DatosIniciarSesion datosIniciarSesion) {

        // Llamamos al metodo para iniciar sesion
        String token = authService.iniciarSesion(datosIniciarSesion.getCorreo(), datosIniciarSesion.getContra());

        Map<String, String> respuesta = new LinkedHashMap<>();
        respuesta.put("token", token);
        respuesta.put("mensaje", "Autenticacion valida");

        return ResponseEntity.status(HttpStatus.OK).body(respuesta);
    }

    @PostMapping("/verificarCuenta")
    public ResponseEntity<Map<String, String>> verificarCuenta(@RequestBody TokenVerificacion tokenVerificacion) {

        // Llamamos al metodo
        authService.verificarCuenta(tokenVerificacion.getToken());

        Map<String, String> respuesta = new LinkedHashMap<>();
        respuesta.put("mensaje", "Cuenta verificada");

        return ResponseEntity.status(HttpStatus.OK).body(respuesta);
    }

    @Data
    private static class DatosIniciarSesion {
        private String correo;
        private String contra;
    }

    @Data
    private static class TokenVerificacion {
        private String token;
    }

}