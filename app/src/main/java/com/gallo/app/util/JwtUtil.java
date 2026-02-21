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
                .claim("tipo", Tipo.SESION)
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

    // Para saber que tipo de tokens hay
    public enum Tipo {
        SESION, VERIFICAR, RESTABLECER
    }

    public String generarTokenTemporal(String correo, Tipo tipo) {

        // Generamos el token
        String token = Jwts.builder()
                .subject(correo)
                .claim("tipo", tipo)
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
            return false;
        }
    }

    public boolean validarTokenTipo(String token, Tipo tipo) {
        try {
            Claims claims = obtenerClaimsToken(token);

            // Verificamos que el tipo de token sea el esperado
            String tipoToken = claims.get("tipo", String.class);
            return tipoToken != null && tipoToken.equals(tipo.name());
        } catch (Exception e) {
            return false;
        }
    }

    public Claims obtenerClaimsToken(String token) {
        // Ademas de obtener los claims, esto valida el token, solo el getPayload() retorna los claims
        return Jwts.parser().verifyWith(obtenerKey()).build().parseSignedClaims(token).getPayload();
    }

    private SecretKey obtenerKey() {
        return Keys.hmacShaKeyFor(JWT_KEY.getBytes(StandardCharsets.UTF_8));
    }

}
