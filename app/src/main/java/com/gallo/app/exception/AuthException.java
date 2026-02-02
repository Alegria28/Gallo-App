package com.gallo.app.exception;

public class AuthException extends RuntimeException {
    public AuthException(String mensaje) {
        super(mensaje);
    }
}