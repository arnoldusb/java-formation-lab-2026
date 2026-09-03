package com.indra.transporte;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import com.indra.transporte.exception.HorarioRangoInvalidoException;
import com.indra.transporte.exception.HorarioSolapadoException;
import com.indra.transporte.exception.UnsupportedTypeException;
import com.indra.transporte.model.Bus;
import com.indra.transporte.model.Horario;
import com.indra.transporte.model.Ruta;

public class ProgramadorRutasTest {
    private final ProgramadorRutas programador = new ProgramadorRutas();
    private final String BUS_PLACA = "ABC123";
    private final String RUTA_CODIGO = "R001";
    private final String RUTA_ORIGEN = "Ciudad A";
    private final String RUTA_DESTINO = "Ciudad B";
    private final String RUTA_ELECTRICA = "Electric";
    private final String RUTA_GENERAL = "General";
    private final String BUS_ELECTRICO = "Electric";
    private final String BUS_DIESEL = "Diesel";

    @Test
    @DisplayName("Debe registrar un horario")
    void debeRegistrarUnHorario() {
        Bus bus = new Bus(BUS_PLACA, BUS_DIESEL);
        Ruta ruta = new Ruta(RUTA_ELECTRICA, RUTA_CODIGO, RUTA_ORIGEN, RUTA_DESTINO);
        Horario horario = HorarioMother.conHora_08_10(bus, ruta);

        programador.programar(horario);

        assertEquals(1, programador.getHorarios().size());
    }

    @Test
    @DisplayName("Debe generar IllegalArgumentException cuando el horario es nulo")
    void debeGenerarIllegalArgumentExceptionCuandoElHorarioEsNulo() {
        assertThrows(IllegalArgumentException.class, () -> {
            programador.programar(null);
        });
    }

  
    @Nested
    @DisplayName("Cuando el bus es eléctrico")
    class CuandoBusEsElectrico {

        @Test
        @DisplayName("Debe rechazar rutas no eléctricas")
        void debeRechazarRutasNoElectricas() {
            Bus bus = new Bus(BUS_PLACA, BUS_ELECTRICO);
            Ruta ruta = new Ruta(RUTA_GENERAL, RUTA_CODIGO, RUTA_ORIGEN, RUTA_DESTINO);
            Horario horario = HorarioMother.conHora_08_10(bus, ruta);

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                programador.debeValidarTipoRutasYBuses(horario);
            });

            assertEquals("Los buses eléctricos solo pueden ir a rutas eléctricas", exception.getMessage());
        }

        @Test
        @DisplayName("Debe permitir rutas eléctricas")
        void debePermitirRutasElectricas() {
            Bus bus = new Bus(BUS_PLACA, "Electric");
            Ruta ruta = new Ruta("Electric", RUTA_CODIGO, RUTA_ORIGEN, RUTA_DESTINO);
            Horario horario = HorarioMother.conHora_08_10(bus, ruta);

            assertDoesNotThrow(() -> programador.debeValidarTipoRutasYBuses(horario));
        }
    }

    @Nested
    @DisplayName("Cuando el bus no es eléctrico")
    class CuandoBusNoEsElectrico {

        @ParameterizedTest
        @CsvSource({
                "Diesel, General",
                "Diesel, Electric"
        })
        @Test
        @DisplayName("Debe permitir cualquier tipo de ruta")
        void debePermitirCualquierTipoDeRuta(String tipoBus, String tipoRuta) {
            Bus bus = new Bus(BUS_PLACA, tipoBus);
            Ruta ruta = new Ruta(tipoRuta, RUTA_CODIGO, RUTA_ORIGEN, RUTA_DESTINO);
            Horario horario = HorarioMother.conHora_08_10(bus, ruta);

            assertDoesNotThrow(() -> programador.debeValidarTipoRutasYBuses(horario));
        }
    }

    @Nested
    @DisplayName("Debe devolver los horarios del tipo solicitado")
    class debeDevolverLosHorariosDelTipoSolicitado {

        @Test
        @DisplayName("Horario vacio")
        void debeDevolverHorariosVacios() {
            List<Horario> horarios = programador.consultarHorariosPorTipoBus(BUS_PLACA, "Electric");
            assertEquals(0, horarios.size(), "Se esperaba que no hubiera horarios para el bus solicitado");
        }

        @Test
        @DisplayName("Horario con buses eléctricos")
        void debeDevolverHorariosConBusesElectricos() {
            Bus busElectrico = new Bus(BUS_PLACA, BUS_ELECTRICO);
            Ruta rutaElectrica = new Ruta(RUTA_ELECTRICA, RUTA_CODIGO, RUTA_ORIGEN, RUTA_DESTINO);
            Horario horarioElectrico = HorarioMother.conHora_08_10(busElectrico, rutaElectrica);
            programador.programar(horarioElectrico);

            List<Horario> horarios = programador.consultarHorariosPorTipoBus(BUS_PLACA, BUS_ELECTRICO);
            assertEquals(1, horarios.size(), "Se esperaba que hubiera un horario para el bus eléctrico");

        }

    }

    @ParameterizedTest
    @ValueSource(strings = { "", "DEF456" })
    @Test
    @DisplayName("Debe lanzar IllegalArgumentException cuando el bus es desconocido")
    void debeLanzarIllegalArgumentExceptionCuandoBusEsDesconocido(String bus) {

        assertThrows(IllegalArgumentException.class, () -> {
            programador.consultarHorariosPorTipoBus(bus, "Electric");
        });
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException cuando el bus es null")
    void debeLanzarIllegalArgumentExceptionCuandoBusEsNull() {

        assertThrows(IllegalArgumentException.class, () -> {
            programador.consultarHorariosPorTipoBus(null, "Electric");
        });
    }

    @ParameterizedTest
    @ValueSource(strings = { "", "ImaginarioType" })
    @Test
    @DisplayName("Debe lanzar UnsupportedTypeException cuando el tipo es desconocido")
    void debeLanzarUnsupportedTypeExceptionCuandoTipoEsDesconocido(String tipo) {
        assertThrows(UnsupportedTypeException.class, () -> {
            programador.consultarHorariosPorTipoBus("ABC123", tipo);
        });
    }

    @Test
    @DisplayName("Debe lanzar UnsupportedTypeException cuando el tipo es null")
    void debeLanzarUnsupportedTypeExceptionCuandoTipoEsNull() {
        assertThrows(UnsupportedTypeException.class, () -> {
            programador.consultarHorariosPorTipoBus("ABC123", null);
        });
    }

    @Test
    @DisplayName("Debe rechazar horarios solapados")
    void debeRechazarHorarioSolapado() {

        Bus busElectrico = new Bus(BUS_PLACA, BUS_ELECTRICO);
        Ruta rutaElectrica = new Ruta(RUTA_ELECTRICA, RUTA_CODIGO, RUTA_ORIGEN, RUTA_DESTINO);
        Horario horarioElectrico = HorarioMother.conHora_08_10(busElectrico, rutaElectrica);
        programador.programar(horarioElectrico);
        Horario horarioElectrico2 = HorarioMother.conHora_0830_1030(busElectrico, rutaElectrica);

        assertThrows(HorarioSolapadoException.class, () -> {
            programador.programar(horarioElectrico2);
        });
    }

    @Test
    @DisplayName("Debe rechazar horarios con rango inválido")
    void debeRechazarHorarioRangoInvalido() {

        Bus busElectrico = new Bus(BUS_PLACA, BUS_ELECTRICO);
        Ruta rutaElectrica = new Ruta(RUTA_ELECTRICA, RUTA_CODIGO, RUTA_ORIGEN, RUTA_DESTINO);
        Horario horarioElectrico = HorarioMother.conHora_1000_0800(busElectrico, rutaElectrica);

        assertThrows(HorarioRangoInvalidoException.class, () -> {
            programador.programar(horarioElectrico);
        });
    }
}
