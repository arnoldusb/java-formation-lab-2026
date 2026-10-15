package com.indra.catalog.products;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.indra.catalog.products.application.CreateProductCommand;
import com.indra.catalog.products.application.ProductService;
import com.indra.catalog.products.domain.exception.ProductNotFoundException;
import com.indra.catalog.products.domain.model.Product;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ProductLayersWiringTest {

    @Autowired
    private ProductService productService;

    //Las capas quedan cableadas
    @Test
    void wiresApplicationServiceToRepository() {
        Product created = productService.create(
                new CreateProductCommand("Wired product", new BigDecimal("19.99"), 3));

        assertThat(created.getId()).isNotBlank();
        assertThat(productService.findById(created.getId()))
                .usingRecursiveComparison()
                .isEqualTo(created);

        productService.deleteById(created.getId());

        assertThatThrownBy(() -> productService.findById(created.getId()))
                .isInstanceOf(ProductNotFoundException.class);
    }
}
