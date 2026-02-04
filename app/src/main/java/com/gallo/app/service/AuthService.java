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
    private final EmailService emailService;

    public AuthService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil,
            EmailService emailService) {
        // Inyeccion por constructor
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.emailService = emailService;
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
        Optional<Usuario> usuarioEncontado = usuarioRepository.findByCorreo(correo);

        // Verificamos si nuestro Optional tiene un Usuario
        if (usuarioEncontado.isPresent()) {

            // Si llegamos aqui es porque si hay un usuario
            Usuario usuario = usuarioEncontado.get();

            // Verificamos que la contraseña sea correcta
            if (passwordEncoder.matches(contra, usuario.getContra())) {

                // Verificamos si el usuario esta verificado
                if (!usuario.isVerificado()) {

                    // Si el usuario no esta verificado mandamos el correo y generamos el token con el correo como subject
                    emailService.codigoVerificacion(correo, jwtUtil.generarTokenVerificacion(correo));

                    throw new AuthException("Cuenta no verificada: Revisa tu correo institucional");
                } else {
                    // Dado que es la contraseña correcta y la cuenta esta verificada, creamos un JWT
                    String token = jwtUtil.generarToken(new UserDetails(usuario.getId(),
                            usuario.getNombre(),
                            usuario.getCorreo(), usuario.isVerificado(),
                            usuario.getRol()));

                    // Retornamos el token
                    return token;
                }

            }

            // Si no es la contraseña, mandamos la exception
            throw new AuthException("Correo o Contraseña incorrectos");
        }

        // Si no existe, mandamos el exception
        throw new AuthException("Correo o contraseña incorrectos");
    }

    public void verificarCuenta(String token) {

        if (!jwtUtil.validarToken(token)) {
            throw new AuthException("Token invalido o expirado");
        }

        // Obtenemos el correo del usuario
        String correoUsuario = jwtUtil.obtenerCorreoToken(token);

        // Obtenemos al usuario
        Optional<Usuario> usuarioEncontrado = usuarioRepository.findByCorreo(correoUsuario);
        Usuario usuario = usuarioEncontrado.get();

        // El usuario ya esta verificado
        usuario.setVerificado(true);
        // Lo guardamos
        usuarioRepository.save(usuario);
    }
}
