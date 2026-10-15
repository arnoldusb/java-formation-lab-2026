package com.indra.catalog.products.domain.port;

import com.indra.catalog.products.domain.model.Product;
import java.util.Optional;

public interface ProductRepository {

    Optional<Product> findById(String id);

    Product save(Product product);

    void deleteById(String id);
}
