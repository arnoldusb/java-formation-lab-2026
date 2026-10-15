package com.indra.catalog.products.web.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record CreateProductRequest(
        @NotBlank(message = "name is required")
        @Size(max = 120, message = "name must not exceed 120 characters")
        String name,

        @NotNull(message = "price is required")
        @DecimalMin(value = "0.01", inclusive = false, message = "price must be greater than 0")
        BigDecimal price,

        @NotNull(message = "stock is required")
        @Min(value = 0, message = "stock must be greater than or equal to 0")
        Integer stock) {
}
