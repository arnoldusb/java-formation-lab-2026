package com.indra.catalog.products.domain.model;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class ProductTest {

    //El precio debe ser > 0 
    @Test
    void shouldRejectNonPositivePrice() {
        assertThatThrownBy(() -> new Product("PRD-0001", "Laptop", BigDecimal.ZERO, 3))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("price");
    }

    
}
