package com.indra.transporte.exception;

public class HorarioRangoInvalidoException extends RuntimeException {

    public HorarioRangoInvalidoException() {
        this("La hora de llegada no puede ser anterior a la hora de salida");
    }

    public HorarioRangoInvalidoException(String message) {
        super(message);
    }

}