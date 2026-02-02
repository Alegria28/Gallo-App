package com.gallo.app.exception;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class EntryPointException implements AuthenticationEntryPoint {

    // Metodo de AuthenticationEntryPoint (se ejecuta cuando hay AuthenticationException)
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException)
            throws IOException, ServletException {

        // El tipo de respuesta va a ser JSON
        response.setContentType("application/json");

        // Codigo de respuesta
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        // Mensaje
        Map<String, String> respuesta = new HashMap<>();
        respuesta.put("mensaje", "Autenticacion invalida");

        // Creamos JSON
        Gson gson = new Gson();
        Type typeObject = new TypeToken<HashMap<String, String>>() {
        }.getType();
        String gsonData = gson.toJson(respuesta, typeObject);

        // Agregamos el mensaje a la respuesta
        response.getWriter().write(gsonData);
    }
}
