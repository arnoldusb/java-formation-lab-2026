package com.indra.retail.orders.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record CreateOrderItemRequest(

        @NotBlank(message = "sku es obligatorio") 
        @Size(min = 1, max = 64, message = "sku debe tener entre 1 y 64 caracteres") 
        String sku,

        @NotNull(message = "quantity es obligatorio") 
        @Positive(message = "quantity debe ser mayor que 0") 
        Integer quantity,

        @NotNull(message = "unitPrice es obligatorio") 
        @Positive(message = "unitPrice debe ser mayor que 0") 
        Double unitPrice) {
}
