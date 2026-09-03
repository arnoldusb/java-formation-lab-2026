package com.indra.transporte.model;

import com.indra.transporte.exception.UnsupportedTypeException;

public enum TipoBus {
    ELECTRIC("Electric"),
    DIESEL("Diesel");

    private final String valor;

    TipoBus(String valor) {
        this.valor = valor;
    }

    public static TipoBus from(String valor) {
        for (TipoBus tipo : values()) {
            if (tipo.valor.equals(valor)) {
                return tipo;
            }
        }
        throw new UnsupportedTypeException("Tipo de bus no soportado: " + valor);
    }
}