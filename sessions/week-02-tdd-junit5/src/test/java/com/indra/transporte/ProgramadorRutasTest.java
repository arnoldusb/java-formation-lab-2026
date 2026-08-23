package com.indra.transporte;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.indra.transporte.model.Bus;
import com.indra.transporte.model.Horario;
import com.indra.transporte.model.Ruta;

public class ProgramadorRutasTest {
    @Test
    @DisplayName("Debe registrar un horario")
    void debeRegistrarUnHorario() {
        Bus bus = new Bus("ABC123", "Diesel");
        Ruta ruta = new Ruta("Electric","R001", "Ciudad A", "Ciudad B");
        Horario horario = new Horario(bus, ruta,
                java.time.LocalTime.of(8, 0), java.time.LocalTime.of(10, 0));

        ProgramadorRutas programador = new ProgramadorRutas();

        programador.programar(horario);

        assertEquals(1, programador.getHorarios().size());
    }

    @Test
    @DisplayName("debe Validar Tipo Rutas Y Buses")
    void debeValidarTipoRutasYBuses() {
        // TODO: Implementar la prueba para validar el tipo de rutas y buses
        fail("No implementado aún");
    }

    //consultarHorariosPorBus
    @Test
    @DisplayName("Debe consultar horarios por bus")
    void debeConsultarHorariosPorTipoBus(Bus bus, String tipoBus) {
        // TODO: Implementar la prueba para consultar horarios por bus según el tipo de bus
        fail("No implementado aún");
    }
}
