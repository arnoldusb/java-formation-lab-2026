package com.indra.retail.orders.web.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.indra.retail.orders.model.Order;
import com.indra.retail.orders.model.OrderStatus;
import com.indra.retail.orders.web.dto.request.CreateOrderItemRequest;
import com.indra.retail.orders.web.dto.request.CreateOrderRequest;
import com.indra.retail.orders.web.dto.response.OrderResponse;

class OrderMapperTest {

    private final OrderItemMapper itemMapper = new OrderItemMapper();
    private final OrderMapper mapper = new OrderMapper(itemMapper);

    @Test
    void toOrder_shouldMapRequestToOrder() {
        CreateOrderRequest request = new CreateOrderRequest(
                "customer-001",
                List.of(new CreateOrderItemRequest("SKU-001", 2, 19.99)),
                "Calle 123 # 45-67");

        Order order = mapper.toOrder(request);

        assertNotNull(order);
        assertEquals("customer-001", order.getCustomerId());
        assertEquals("Calle 123 # 45-67", order.getDeliveryAddress());
        assertEquals(1, order.getItems().size());
        assertEquals("SKU-001", order.getItems().get(0).getSku());
        assertEquals(2, order.getItems().get(0).getQuantity());
        assertEquals(19.99, order.getItems().get(0).getUnitPrice());
        assertEquals(OrderStatus.CREATED, order.getStatus());
    }

    @Test
    void toOrderResponse_shouldMapOrderToResponse() {
        Order order = new Order("customer-001",
                List.of(new com.indra.retail.orders.model.OrderItem("SKU-001", 2, 19.99)),
                "Calle 123 # 45-67");

        OrderResponse response = mapper.toOrderResponse(order);

        assertNotNull(response);
        assertEquals(order.getId(), response.orderId());
        assertEquals(order.getStatus().name(), response.status());
        assertEquals(order.getTotalAmount(), response.totalAmount());
        assertEquals(order.getEstimatedDelivery().toString(), response.estimatedDelivery());
    }
}
