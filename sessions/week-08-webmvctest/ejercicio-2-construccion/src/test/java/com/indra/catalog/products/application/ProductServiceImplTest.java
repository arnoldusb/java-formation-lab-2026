package com.indra.catalog.products.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import com.indra.catalog.products.domain.model.Product;
import com.indra.catalog.products.domain.port.ProductRepository;

class ProductServiceImplTest {

    private final ProductRepository productRepository = mock(ProductRepository.class);
    private final ProductServiceImpl productService = new ProductServiceImpl(productRepository);

    // Se genera un id al crear
    @Test
    void shouldCreateProductAndAssignId() {
        Product product = new Product("PRD-0001", "Laptop", new BigDecimal("3500000.00"), 10);
        when(productRepository.save(product)).thenReturn(product);
        CreateProductCommand command = new CreateProductCommand("Laptop", new BigDecimal("3500000.00"), 10);
        Product result = productService.create(command);

        assertThat(result.getId()).startsWith("PRD-");
        assertThat(result.getName()).isEqualTo("Laptop");
    }

     

}
