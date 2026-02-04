package com.gallo.app.util;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    // Contraseña para firmar los tokens
    @Value("${JWT_KEY}")
    private String JWT_KEY;

    public String generarToken(UserDetails userDetails) {

        // Generamos el token
        //! Configurar token con expiracion
        String token = Jwts.builder()
                .subject(userDetails.getId().toString())
                .claim("nombre", userDetails.getNombre())
                .claim("correo", userDetails.getCorreo())
                .claim("verificado", userDetails.isVerificado())
                .claim("rol", userDetails.getRol().toString())
                .issuedAt(new Date())
                .signWith(obtenerKey())
                .compact();

        // Retornamos el token
        return token;
    }

    public String generarTokenVerificacion(String correo) {

        // Generamos el token
        String token = Jwts.builder()
                .subject(correo)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 10 * 60 * 1000)) // 10 minutos
                .signWith(obtenerKey())
                .compact();

        // Regresamos el token
        return token;
    }

    public boolean validarToken(String token) {
        try {
            Jwts.parser().verifyWith(obtenerKey()).build().parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            // El token es invalido
            return false;
        }
    }

    public Claims obtenerClaimsToken(String token) {
        return Jwts.parser().verifyWith(obtenerKey()).build().parseSignedClaims(token).getPayload();
    }

    public String obtenerCorreoToken(String token) {
        Claims claims = Jwts.parser().verifyWith(obtenerKey()).build().parseSignedClaims(token).getPayload();
        return claims.getSubject();
    }

    private SecretKey obtenerKey() {
        return Keys.hmacShaKeyFor(JWT_KEY.getBytes(StandardCharsets.UTF_8));
    }

}
