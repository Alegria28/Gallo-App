package com.gallo.app.service;

import java.util.Optional;

import com.gallo.app.util.JwtUtil;
import com.gallo.app.util.UserDetails;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.gallo.app.entity.Usuario;
import com.gallo.app.exception.AuthException;
import com.gallo.app.exception.CorreoInvalidoException;
import com.gallo.app.exception.CorreoYaExisteException;
import com.gallo.app.repository.UsuarioRepository;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        // Inyeccion por constructor
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public void registrarUsuario(String nombre, String correo, String contra) {

        // Verificamos si el correo es valido
        if (!correo.matches("^al\\d{6}@edu\\.uaa\\.mx$")) {
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
        usuarioRepository.save(nuevoUsuario);

    }

    public String iniciarSesion(String correo, String contra) {

        // Verificamos si existe algun usuario con ese correo
        Optional<Usuario> usuario = usuarioRepository.findByCorreo(correo);

        // Verificamos si nuestro Optional tiene un Usuario
        if (usuario.isPresent()) {

            // Si llegamos aqui es porque si hay un usuario
            Usuario usuarioEncontrado = usuario.get();

            // Verificamos que la contraseña sea correcta
            if (passwordEncoder.matches(contra, usuarioEncontrado.getContra())) {
                // Dado que es la contraseña correcta, creamos un JWT
                String token = jwtUtil.generarToken(new UserDetails(usuarioEncontrado.getId(),
                        usuarioEncontrado.getNombre(),
                        usuarioEncontrado.getCorreo(), usuarioEncontrado.isVerificado(), usuarioEncontrado.getRol()));

                // Retornamos el token
                return token;

            }

            // Si no es la contraseña, mandamos la exception
            throw new AuthException("Correo o Contraseña incorrectos");
        }

        // Si no existe, mandamos el exception
        throw new AuthException("Correo o contraseña incorrectos");
    }
}
