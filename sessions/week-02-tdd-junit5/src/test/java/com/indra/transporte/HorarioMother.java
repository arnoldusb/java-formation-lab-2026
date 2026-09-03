package com.indra.transporte;

import java.time.LocalTime;

import com.indra.transporte.model.Bus;
import com.indra.transporte.model.Horario;
import com.indra.transporte.model.Ruta;

public final class HorarioMother {

    public static Horario conHora_08_10(Bus bus, Ruta ruta) {
        return new Horario(bus, ruta, LocalTime.of(8, 0), LocalTime.of(10, 0));
    }

    public static Horario conHora_0830_1030(Bus bus, Ruta ruta) {
        return new Horario(bus, ruta, LocalTime.of(8, 30), LocalTime.of(10, 30));
    }

    public static Horario conHora_1000_0800(Bus bus, Ruta ruta) {
        return new Horario(bus, ruta, LocalTime.of(10, 00), LocalTime.of(8, 00));
    }

}
