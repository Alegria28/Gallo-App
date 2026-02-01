package com.gallo.app.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.gallo.app.entity.Usuario;
import com.gallo.app.exception.CorreoInvalidoException;
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

        // Verificamos si el correo es valido
        if (!correo.matches("^al\\d{6}@edu\\.uaa\\.mx$")){
            throw new CorreoInvalidoException("Correo invalido");
        }

        // Verificamos si ya existe este correo
        if (usuarioRepository.existsByCorreo(correo)) {
            // Si este existe, lanzamos nuestra excepcion
            throw new CorreoYaExisteException("Correo ya registrado");
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