package com.indra.retail;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OrderProcessorTest {

    private final OrderProcessor orderProcessor = new OrderProcessor(
            new StockValidator(),
            new OrderNotifier(),
            new StandardDiscountPolicy());

    @Test
    @DisplayName("Debe procesar el pedido cuando hay stock suficiente")
    void shouldProcessOrderWhenStockIsAvailable() {
        Order order = new Order(
                "ORD-001",
                new BigDecimal("100.00"),
                DiscountType.STANDARD,
                2,
                "cliente@indra.com");

        BigDecimal finalPrice = orderProcessor.process(order, 5);

        assertEquals(new BigDecimal("95.0000"), finalPrice);
    }

    @Test
    @DisplayName("Debe rechazar el pedido cuando no hay stock suficiente")
    void shouldRejectOrderWhenStockIsInsufficient() {
        Order order = new Order(
                "ORD-002",
                new BigDecimal("100.00"),
                DiscountType.STANDARD,
                6,
                "cliente@indra.com");

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> orderProcessor.process(order, 5));

        assertEquals("Stock insuficiente para el pedido ORD-002", exception.getMessage());
    }
}