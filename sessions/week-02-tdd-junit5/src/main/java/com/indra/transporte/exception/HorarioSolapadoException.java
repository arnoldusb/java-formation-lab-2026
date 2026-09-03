package com.indra.transporte.exception;

public class HorarioSolapadoException extends RuntimeException {

    public HorarioSolapadoException() {
        this("El horario se solapa con otro horario existente");
    }

    public HorarioSolapadoException(String message) {
        super(message);
    }

}