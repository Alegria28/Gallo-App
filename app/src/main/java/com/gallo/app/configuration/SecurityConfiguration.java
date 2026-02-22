package com.gallo.app.configuration;

import java.util.List;

import com.gallo.app.exception.EntryPointException;
import com.gallo.app.filter.JwtFilter;
import com.gallo.app.util.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity // Para seguridad web personalizada
public class SecurityConfiguration {

    private final EntryPointException entryPointException;
    private final JwtUtil jwtUtil;

    SecurityConfiguration(EntryPointException entryPointException, JwtUtil jwtUtil) {
        this.entryPointException = entryPointException;
        this.jwtUtil = jwtUtil;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) {
        return httpSecurity
                // Desactivamos el CSRF
                .csrf(csrf -> csrf.disable())
                // Activamos el CORS
                .cors(cors -> cors.configurationSource(configuracionCors()))
                // Configuracion del entryPoint
                .exceptionHandling(exceptionHandling -> exceptionHandling.authenticationEntryPoint(entryPointException))
                // Filtro para JWT
                .addFilterBefore(new JwtFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class)
                // Configuracion permisos de las rutas
                .authorizeHttpRequests(authorize -> authorize
                        // Cualquiera puede acceder a estos endpoints
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/health/**").permitAll()
                        // Las otras rutas requieren todavia de autenticacion
                        .anyRequest().authenticated())
                // Servidor stateless
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();

    }

    private CorsConfigurationSource configuracionCors() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        // Origenes permitidos
        //! Importante cambiar
        corsConfiguration.setAllowedOrigins(List.of("*"));

        // Metodos permitidos
        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // Permitir cabeceras
        corsConfiguration.setAllowedHeaders(List.of("Authorization", "Content-Type"));

        // Configuracion para todas las rutas
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }
}
