package com.indra.retail.orders.web.dto.response;

public record OrderResponse(
        String orderId,
        String status,
        Double totalAmount,
        String estimatedDelivery) {
}
