package com.gallo.app.configuration;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity // Para seguridad web personalizada
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) {
        return httpSecurity
                // Desactivamos el CSRF
                .csrf(csrf -> csrf.disable())
                // Activamos el CORS
                .cors(cors -> cors.configurationSource(configurationSource()))
                // Configuracion permisos de las rutas
                .authorizeHttpRequests(authorize -> authorize
                        // Cualquiera se puede registrar
                        .requestMatchers("/api/usuario/registrar").permitAll()
                        // Las otras rutas requieren todavia de autenticacion
                        .anyRequest().authenticated())
                .build();

    }

    @Bean
    public CorsConfigurationSource configurationSource() {
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
