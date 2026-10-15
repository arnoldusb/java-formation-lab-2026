package com.indra.catalog.products.domain.model;

import java.math.BigDecimal;
import java.util.Objects;

public class Product {

    private final String id;
    private final String name;
    private final BigDecimal price;
    private final int stock;

    public Product(String id, String name, BigDecimal price, int stock) {
        validateId(id);
        validateName(name);
        validatePrice(price);
        validateStock(stock);

        this.id = id;
        this.name = name.trim();
        this.price = price;
        this.stock = stock;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    private void validateId(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("id is required");
        }
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name is required");
        }
        if (name.trim().length() > 120) {
            throw new IllegalArgumentException("name must not exceed 120 characters");
        }
    }

    private void validatePrice(BigDecimal price) {
        if (price == null) {
            throw new IllegalArgumentException("price is required");
        }
        if (price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("price must be greater than 0");
        }
    }

    private void validateStock(int stock) {
        if (stock < 0) {
            throw new IllegalArgumentException("stock must be greater than or equal to 0");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Product product = (Product) o;
        return stock == product.stock && Objects.equals(id, product.id) && Objects.equals(name, product.name)
                && Objects.equals(price, product.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, stock);
    }
}
