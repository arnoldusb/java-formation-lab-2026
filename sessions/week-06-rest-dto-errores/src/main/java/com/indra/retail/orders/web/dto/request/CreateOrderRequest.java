package com.indra.retail.orders.web.dto.request;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateOrderRequest(

        @NotBlank(message = "customerId es obligatorio") 
        @NotNull(message = "customerId es obligatorio") 
        String customerId,

        @NotEmpty(message = "items no puede estar vacío") 
        @Valid 
        List<CreateOrderItemRequest> items,

        @NotNull(message = "deliveryAddress es obligatoria") 
        @Size(min = 10, max = 100, message = "deliveryAddress debe tener entre 10 y 100 caracteres") 
        String deliveryAddress) {
}