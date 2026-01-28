package com.gallo.app.exception;

public class CorreoYaExisteException extends RuntimeException {
    public CorreoYaExisteException(String mensaje) {
        super(mensaje);
    }
}
