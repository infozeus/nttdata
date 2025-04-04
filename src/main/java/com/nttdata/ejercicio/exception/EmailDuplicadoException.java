package com.nttdata.ejercicio.exception;

public class EmailDuplicadoException extends RuntimeException{

    public EmailDuplicadoException(String mensaje) {
        super(mensaje);
    }

}
