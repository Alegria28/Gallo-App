package com.gallo.app.filter;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.gallo.app.util.JwtUtil;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    // Metodo de OncePerRequestFilter
    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain chain) throws ServletException, IOException {

        // Obtenemos el header de la peticion
        String authorization = request.getHeader("Authorization");

        // Si la peticion no viene con el header o si no inicia con Bearer
        if (authorization == null || !authorization.startsWith("Bearer")) {
            // Seguimos pero sin darle permiso
            chain.doFilter(request, response);
            return;
        }

        // Si viene el token, entonces lo sacamos
        String token = authorization.substring(7); // Quitamos Bearer y su espacio

        // Verificamos el token
        if (!jwtUtil.validarToken(token)) {
            // Si este no es valido, entonces pasamos sin darle permiso
            chain.doFilter(request, response);
            return;
        }

        // Obtenemos los claims del token
        Claims claims = jwtUtil.obtenerClaimsToken(token);

        // Obtenemos el id
        String usuarioId = claims.getSubject();

        // Obtenemos el rol
        String rol = claims.get("rol", String.class);

        // Creamos una lista de authorities (Spring Security requiere una colección aunque solo tengamos un rol)
        // El rol se usa para verificar permisos con .hasAuthority() en SecurityConfiguration
        List<SimpleGrantedAuthority> permisos = List.of(new SimpleGrantedAuthority(rol));

        // Creamos objeto de autenticacion (id como principal)
        Authentication auth = new UsernamePasswordAuthenticationToken(usuarioId, null, permisos);

        // Lo agregamos al contexto de seguridad
        SecurityContextHolder.getContext().setAuthentication(auth);

        // Continuamos (hacia el controller)
        chain.doFilter(request, response);

    }

}
