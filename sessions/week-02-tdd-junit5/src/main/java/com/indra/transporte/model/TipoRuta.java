package com.indra.transporte.model;

import com.indra.transporte.exception.UnsupportedTypeException;

public enum TipoRuta {
    ELECTRIC("Electric"),
    GENERAL("General");

    private final String valor;

    TipoRuta(String valor) {
        this.valor = valor;
    }

    public static TipoRuta from(String valor) {
        for (TipoRuta tipo : values()) {
            if (tipo.valor.equals(valor)) {
                return tipo;
            }
        }
        throw new UnsupportedTypeException("Tipo de ruta no soportado: " + valor);
    }
}