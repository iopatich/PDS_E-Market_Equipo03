package com.emarket.exception;

public class UsuarioYaExisteException extends RuntimeException {
    public UsuarioYaExisteException(String message) {
        super(message);
    }
}
