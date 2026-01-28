package com.gallo.app.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.gallo.app.entity.Usuario;
import com.gallo.app.exception.CorreoYaExisteException;
import com.gallo.app.repository.UsuarioRepository;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        // Inyeccion por constructor
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Usuario registrarUsuario(String nombre, String correo, String contra) {

        // Verificamos si ya existe este correo
        if (usuarioRepository.existsByCorreo(correo)) {
            // Si este existe, lanzamos nuestra excepcion
            throw new CorreoYaExisteException("El correo ya esta registrado");
        }

        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombre(nombre);
        nuevoUsuario.setCorreo(correo);

        // Hasheamos la contraseña
        String contraHasheada = passwordEncoder.encode(contra);
        nuevoUsuario.setContra(contraHasheada);

        // Guardamos al usuario
        return usuarioRepository.save(nuevoUsuario);

    }

}