package com.indra.logistics;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TrackingIdGeneratorTest {

    private final TrackingIdGenerator generator = new TrackingIdGenerator();

    @Test
    @DisplayName("El ID generado debe tener el formato ORIG-DEST-XXXXXXXX")
    void shouldGenerateIdWithCorrectFormat() {
        String trackingId = generator.generate("ORIG", "DEST");
        assertTrue(trackingId.matches("ORIG-DEST-[A-Z0-9]{8}"));
    }

    @Test
    @DisplayName("Debe lanzar excepción si origin es nulo")
    void shouldThrowWhenOriginIsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            generator.generate(null, "DEST");
        });
    }

    @Test
    @DisplayName("Debe lanzar excepción si destination es nulo")
    void shouldThrowWhenDestinationIsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            generator.generate("ORIG", null);
        });
    }
}