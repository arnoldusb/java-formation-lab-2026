package com.indra.catalog.products.application;

import com.indra.catalog.products.domain.exception.ProductNotFoundException;
import com.indra.catalog.products.domain.model.Product;
import com.indra.catalog.products.domain.port.ProductRepository;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final AtomicLong sequence = new AtomicLong(1L);

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product findById(String id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    @Override
    public Product create(CreateProductCommand command) {
        String generatedId = nextProductId();
        Product product = new Product(
                generatedId,
                command.name(),
                command.price(),
                command.stock());
        return productRepository.save(product);
    }

    @Override
    public void deleteById(String id) {
        if (productRepository.findById(id).isEmpty()) {
            throw new ProductNotFoundException(id);
        }
        productRepository.deleteById(id);
    }

    private String nextProductId() {
        long next = sequence.getAndIncrement();
        return String.format("PRD-%04d", next);
    }
}
