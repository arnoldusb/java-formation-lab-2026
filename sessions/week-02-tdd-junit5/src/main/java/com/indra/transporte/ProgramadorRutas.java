package com.indra.transporte;

import java.util.ArrayList;
import java.util.List;

import com.indra.transporte.model.Horario;

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

}
