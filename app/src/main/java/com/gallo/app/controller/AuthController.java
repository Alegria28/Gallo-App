package com.gallo.app.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<Map<String, String>> registrarUsuario(
            @RequestBody DatosRegistrarUsuario datosRegistrarUsuario) {

        // Llamamos al metodo del servicio para guardar al usuario
        authService.registrarUsuario(datosRegistrarUsuario.getNombre(), datosRegistrarUsuario.getCorreo(),
                datosRegistrarUsuario.getContra());

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
    public ResponseEntity<Map<String, String>> verificarCuenta(@RequestBody DatosVerificarCuenta datosVerificarCuenta) {

        // Llamamos al metodo
        authService.verificarCuenta(datosVerificarCuenta.getToken());

        Map<String, String> respuesta = new LinkedHashMap<>();
        respuesta.put("mensaje", "Cuenta verificada");

        return ResponseEntity.status(HttpStatus.OK).body(respuesta);
    }

    @PostMapping("/solicitarRestablecerContra")
    public ResponseEntity<Map<String, String>> restablecerContra(
            @RequestBody DatosSolicitarRestablecerContra datosRestablecerContra) {

        // Llamamos al metodo
        authService.solicitarRestablecerContra(datosRestablecerContra.getCorreo());

        // Mandamos la respuesta respectiva
        Map<String, String> mensaje = new LinkedHashMap<>();
        mensaje.put("mensaje", "Correo enviado para restablecer contraseña");

        return ResponseEntity.status(HttpStatus.OK).body(mensaje);
    }

    @PostMapping("/restablecerContra")
    public ResponseEntity<Map<String, String>> restablecerContra(
            @RequestBody DatosRestablecerContra datosRestablecerContra) {

        // Llamamos al metodo
        authService.restablecerContra(datosRestablecerContra.getToken(),
                datosRestablecerContra.getContra());

        Map<String, String> mensaje = new LinkedHashMap<>();
        mensaje.put("mensaje", "Contraseña restablecida");

        return ResponseEntity.status(HttpStatus.OK).body(mensaje);
    }

    @Data
    private static class DatosRegistrarUsuario {
        private String nombre;
        private String correo;
        private String contra;
    }

    @Data
    private static class DatosIniciarSesion {
        private String correo;
        private String contra;
    }

    @Data
    private static class DatosVerificarCuenta {
        private String token;
    }

    @Data
    private static class DatosRestablecerContra {
        private String token;
        private String contra;
    }

    @Data
    private static class DatosSolicitarRestablecerContra {
        private String correo;
    }

}
