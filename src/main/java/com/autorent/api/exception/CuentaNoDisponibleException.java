package com.autorent.api.exception;

public class CuentaNoDisponibleException extends RuntimeException {
    public CuentaNoDisponibleException(String m) {
        super(m);
    }
}
