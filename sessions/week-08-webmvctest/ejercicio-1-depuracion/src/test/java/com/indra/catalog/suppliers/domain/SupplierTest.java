package com.indra.catalog.suppliers.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class SupplierTest {

    @Test
    void shouldNormalizeSupplierData() {
        Supplier supplier = new Supplier("  acme ltda ", "900.123.456-7", "compras@acme.co");

        assertEquals("ACME LTDA", supplier.getName());
        assertEquals("900123456-7", supplier.getTaxId());
        assertEquals("compras@acme.co", supplier.getEmail());
    }

    @Test
    void shouldRejectBlankName() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Supplier("   ", "900123456-7", "compras@acme.co"));

        assertEquals("la razon social es obligatoria", exception.getMessage());
    }
}