package com.indra.catalog.products.web.mapper;

import com.indra.catalog.products.application.CreateProductCommand;
import com.indra.catalog.products.domain.model.Product;
import com.indra.catalog.products.web.dto.CreateProductRequest;
import com.indra.catalog.products.web.dto.ProductResponse;

public class ProductMapper {

    public CreateProductCommand toCommand(CreateProductRequest request) {
        return new CreateProductCommand(request.name(), request.price(), request.stock());
    }

    public ProductResponse toResponse(Product product) {
        return new ProductResponse(product.getId(), product.getName(), product.getPrice(), product.getStock());
    }
}
