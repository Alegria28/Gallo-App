package com.gallo.app.service;

import java.util.Optional;

import com.gallo.app.util.JwtUtil;
import com.gallo.app.util.UserDetails;
import com.gallo.app.util.JwtUtil.Tipo;

import io.jsonwebtoken.Claims;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.gallo.app.entity.Usuario;
import com.gallo.app.entity.UsuarioDetalle;
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

        // Creamos las entidades
        UsuarioDetalle usuarioDetalle = new UsuarioDetalle();
        usuarioDetalle.setNombre(nombre);

        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setCorreo(correo);

        // Hasheamos la contraseña
        String contraHasheada = passwordEncoder.encode(contra);
        nuevoUsuario.setContra(contraHasheada);

        // Establecemos relacion bidireccional
        nuevoUsuario.setUsuarioDetalle(usuarioDetalle);
        usuarioDetalle.setUsuario(nuevoUsuario);

        // Guardamos al usuario (gracias al cascade, solo debemos guardar el usuario)
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

                // Obtenemos el detalle del usuario para verificar si esta verificado
                UsuarioDetalle usuarioDetalle = usuario.getUsuarioDetalle();

                // Verificamos si el usuario esta verificado
                if (!usuarioDetalle.isVerificado()) {

                    // Si el usuario no esta verificado mandamos el correo y generamos el token con el correo como subject
                    emailService.solicitarTokenVerificacion(correo,
                            jwtUtil.generarTokenTemporal(correo, Tipo.VERIFICAR));

                    throw new AuthException("Cuenta no verificada: Revisa tu correo institucional");
                } else {
                    // Dado que es la contraseña correcta y la cuenta esta verificada, creamos un JWT
                    String token = jwtUtil.generarToken(
                            new UserDetails(usuario.getIdUsuario(), usuario.getUsuarioDetalle().getNombre(),
                                    usuario.getCorreo(), usuario.getUsuarioDetalle().isVerificado(), usuario.getRol()));

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

        if (!jwtUtil.validarTokenTipo(token, Tipo.VERIFICAR)) {
            throw new AuthException("Token invalido o expirado");
        }

        // Obtenemos los claims del token
        Claims claims = jwtUtil.obtenerClaimsToken(token);

        // Obtenemos al usuario
        Optional<Usuario> usuarioEncontrado = usuarioRepository.findByCorreo(claims.getSubject());
        Usuario usuario = usuarioEncontrado.get();

        // Obtenemos el detalle del usuario para verificar si esta verificado
        UsuarioDetalle usuarioDetalle = usuario.getUsuarioDetalle();

        // El usuario ya esta verificado
        usuarioDetalle.setVerificado(true);
        // Lo guardamos
        usuarioRepository.save(usuario);

    }

    public void solicitarRestablecerContra(String correo) {

        // Enviamos correo para restablecer contra
        emailService.solicitarRestablecerContra(correo, jwtUtil.generarTokenTemporal(correo, Tipo.RESTABLECER));

    }

    public void restablecerContra(String token, String contra) {

        // Validamos el token
        if (!jwtUtil.validarTokenTipo(token, Tipo.RESTABLECER)) {
            throw new AuthException("Token invalido o expirado");
        }

        // Obtenemos los claims del token
        Claims claims = jwtUtil.obtenerClaimsToken(token);

        // Obtenemos al usuario
        Optional<Usuario> usuarioEncontrado = usuarioRepository.findByCorreo(claims.getSubject());

        Usuario usuario = usuarioEncontrado.get();

        // Hasheamos la contraseña
        String contraHasheada = passwordEncoder.encode(contra);
        // Le cambiamos la contraseña al usuario
        usuario.setContra(contraHasheada);

        // Lo guardamos
        usuarioRepository.save(usuario);
    }
}
