package com.indra.transporte;

import java.util.ArrayList;
import java.util.List;

import com.indra.transporte.model.Horario;
import com.indra.transporte.model.TipoBus;
import com.indra.transporte.model.TipoRuta;

import lombok.Data;

@Data
public class ProgramadorRutas {

    List<Horario> horarios = new ArrayList<>();

    public void programar(Horario horario) {
        if (horario == null) {
            throw new IllegalArgumentException("El horario no puede ser nulo");
        }
        horarios.add(horario);
    }

    public boolean debeValidarTipoRutasYBuses(Horario horario) {
        if (horario == null) {
            throw new IllegalArgumentException("El horario no puede ser nulo");
        }
        TipoBus tipoBus = TipoBus.from(horario.getBus().getTipo());
        TipoRuta tipoRuta = TipoRuta.from(horario.getRuta().getTipo());

        if (tipoBus == TipoBus.ELECTRIC && tipoRuta != TipoRuta.ELECTRIC) {
            throw new IllegalArgumentException("Los buses eléctricos solo pueden ir a rutas eléctricas");
        }
        return true;
    }

    public List<Horario> consultarHorariosPorTipoBus(String busId, String tipoBus) {
        if (busId == null || tipoBus == null) {
            throw new IllegalArgumentException("El busId y el tipoBus no pueden ser nulos");
        }
        if (busId.isEmpty() || tipoBus.isEmpty()) {
            throw new IllegalArgumentException("El busId y el tipoBus no pueden estar vacíos");
        }
        TipoBus tipoBusSolicitado = TipoBus.from(tipoBus);
        List<Horario> horariosFiltrados = new ArrayList<>();
        boolean busEncontrado = false;
        for (Horario horario : horarios) {
            if (horario.getBus().getPlaca().equals(busId)) {
                busEncontrado = true;
            }
            if (horario.getBus().getPlaca().equals(busId)
                    && TipoBus.from(horario.getBus().getTipo()) == tipoBusSolicitado) {
                horariosFiltrados.add(horario);
            }
        }
        if (!busEncontrado) {
            throw new IllegalArgumentException("El bus no existe: " + busId);
        }
        return horariosFiltrados;
    }

}
