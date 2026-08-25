package com.indra.transporte;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.indra.transporte.exception.UnsupportedTypeException;
import com.indra.transporte.model.Bus;
import com.indra.transporte.model.Horario;

import lombok.Data;

@Data
public class ProgramadorRutas {

    List<Horario> horarios = new ArrayList<>();

    public void programar(Horario horario) {
        if (horario == null) {
            throw new IllegalArgumentException("El horario no puede ser nulo");
        }
        if (debeValidarTipoRutasYBuses(horario)) {
            horarios.add(horario);
        }
    }

    public boolean debeValidarTipoRutasYBuses(Horario horario) {
        if (horario == null) {
            throw new IllegalArgumentException("El horario no puede ser nulo");
        }
        String tipoBus = horario.getBus().getTipo();
        String tipoRuta = horario.getRuta().getTipo();

        if ("Electric".equals(tipoBus) && !"Electric".equals(tipoRuta)) {
            throw new IllegalArgumentException("Los buses eléctricos solo pueden ir a rutas eléctricas");
        }
        return true;
    }

    public List<Horario> consultarHorariosPorTipoBus(Bus bus, String tipo) {
        if (bus == null) {
            throw new IllegalArgumentException("Bus desconocido");
        }

        if (tipo == null || !("Electric".equals(tipo) || "Diesel".equals(tipo))) {
            throw new UnsupportedTypeException("Tipo de bus no soportado");
        }

        boolean busExistente = horarios.stream()
                .map(Horario::getBus)
                .anyMatch(b -> Objects.equals(b.getPlaca(), bus.getPlaca())
                        && Objects.equals(b.getTipo(), bus.getTipo()));

        if (!busExistente) {
            throw new IllegalArgumentException("Bus desconocido");
        }

        return horarios.stream()
                .filter(h -> Objects.equals(h.getBus().getPlaca(), bus.getPlaca()))
                .filter(h -> Objects.equals(h.getBus().getTipo(), tipo))
                .toList();
    }

}
