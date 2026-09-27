package com.indra.retail.orders.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.indra.retail.orders.web.dto.request.CreateOrderItemRequest;
import com.indra.retail.orders.web.dto.request.CreateOrderRequest;
import com.indra.retail.orders.web.dto.response.OrderResponse;
import com.indra.retail.orders.web.mapper.OrderItemMapper;
import com.indra.retail.orders.web.mapper.OrderMapper;

class OrderServiceTest {

    private final OrderMapper orderMapper = new OrderMapper(new OrderItemMapper());
    private final OrderService service = new OrderService(orderMapper);

    @Test
    void create_shouldPersistOrderAndReturnResponse() {
        CreateOrderRequest request = new CreateOrderRequest(
                "customer-001",
                List.of(new CreateOrderItemRequest("SKU-001", 2, 19.99)),
                "Calle 123 # 45-67");

        OrderResponse response = service.create(request);

        assertNotNull(response);
        assertNotNull(response.orderId());
        assertEquals("CREATED", response.status());
        assertEquals(39.98, response.totalAmount());
    }

    @Test
    void findById_shouldThrow_whenOrderDoesNotExist() {
        OrderNotFoundException exception = assertThrows(
                OrderNotFoundException.class,
                () -> service.findById("missing-order"));

        assertEquals("Pedido no encontrado: missing-order", exception.getMessage());
    }
}
